/*
 * Copyright (c) 2018. RED SHIFT ROBOTICS. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Code that allows the Drivers to use controllers and drive the robot as well as its mechanisms.
 */

@TeleOp(name = "TeleOp", group = "13330 Pulsar")
public class teleop extends LinearOpMode {

    // instances of hardware, robot, and text
    private Hardware hardware;
    private MecanumChassis mecanumChassis;
    private Robot robot;
    private Console console;
    private Imu imu;

    private double speed = 0.45;
    private double horizontalSensitivity = 5;
    private double verticalSensitivity = 5;
    private double rotationSensitivity = 5;

    private boolean g2LeftBumperState = false;

    @Override
    public void runOpMode() {

        //initializes other classes
        Thread driveThread = new DriveThread();

        this.hardware = new Hardware(this);
        this.imu = new Imu(this);
        this.console = new Console(this);

        while (!imu.imu.isGyroCalibrated()) {
            console.Status("Calibrating Gyro");
            idle();
        }

        this.mecanumChassis = new MecanumChassis(this, hardware.zeroPowerBehavior, this.imu);

        console.Status("Ready to start");

        waitForStart();

        imu.resetAngle();

        console.Status("Reset Angle");

        driveThread.start();

        while (opModeIsActive()) {

            //helps robot performance (rids of unnecessary loops)


            telemetry.addData("Mode", mecanumChassis.getStickSensitivity(gamepad1.left_stick_x, horizontalSensitivity));
            telemetry.addData("Hypot", Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y));
            telemetry.addData("x", gamepad1.left_stick_x * speed);
            telemetry.addData("y", gamepad1.left_stick_y * speed);
            telemetry.addData("sticks", "  left=" + gamepad1.left_stick_y + "  right=" + gamepad1.right_stick_y);
            telemetry.update();

            if (gamepad2.left_bumper) {
                g2LeftBumperState = true;
            } else {
                g2LeftBumperState = false;
            }

            idle();

        }

        driveThread.interrupt();
    }

    //Thread that allows the driver to drive the mecanum chassis.
    private class DriveThread extends Thread {

        //sets the name of the thread to be referenced later.
        public DriveThread() {
            this.setName("DriveThread");

        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {

                    //Allows the thread to detect if the right bumper is pressed, which will change
                    //the driving style from a locked forward to free drive.

                    //mecanumChassis.driveS(mecanumChassis.getStickSensitivity(gamepad1.left_stick_x * speed, horizontalSensitivity), mecanumChassis.getStickSensitivity(gamepad1.left_stick_y * speed, verticalSensitivity), mecanumChassis.getStickSensitivity(gamepad1.right_stick_x * speed, rotationSensitivity));
                    if (gamepad1.right_bumper) {
                        mecanumChassis.driveGlobal(-gamepad1.left_stick_x * speed, -gamepad1.left_stick_y * speed, -gamepad1.right_stick_x * speed);
                    } else {
                        mecanumChassis.driveS(gamepad1.left_stick_x * speed, -gamepad1.left_stick_y * speed, -gamepad1.right_stick_x * speed);
                    }

                    //Allows the thread to detect if 'a' is pressed, which will reset the global angle.
                    if (gamepad1.a)
                        imu.resetAngle();
                    idle();
                }
            }
            // an error occurred in the run loop.
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Thread that allows the driver to run the collector mechanisms.
    private class CollectorThread extends Thread {

        //sets the name of the thread to be referenced later.
        public CollectorThread(){
            this.setName("CollectorThread");

        }

        @Override
        public void run(){
            try{
                CollectorPosition collectorPosition = CollectorPosition.UP;

                hardware.extenderWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.collectorHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.collector.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                while(!isInterrupted()){

                    hardware.extenderWheel.setPower(-gamepad2.left_stick_y);

                    //Allows the thread to detect if the left bumper has been pressed, which will then set
                    //the position of the servo motor to a certain position.
                    if (gamepad2.left_bumper) {
                        collectorPosition = CollectorPosition.values()[(collectorPosition.ordinal() + 1) % CollectorPosition.values().length];
                        sleep(100);
                    }


                    //Based on the collector position, allows the Thread to set a specific position for
                    //the collector hinge.
                    switch (collectorPosition) {
                        case UP:
                            hardware.collectorHinge.setTargetPosition(0);
                            break;
                        case DOWN:
                            hardware.collectorHinge.setTargetPosition(1000);
                            break;
                        case CENTER:
                            hardware.collectorHinge.setTargetPosition(500);
                            break;
                    }

                    hardware.collectorHinge.setPower(1);

                    //If that position is equal to anything but UP, the collector's power will be set
                    //to the power of the right joystick.
                    if (collectorPosition != CollectorPosition.UP) {
                        hardware.collector.setPower(gamepad2.right_stick_y);
                    }
                }
            } catch(Exception e){
                    e.printStackTrace();
            }
        }
    }

    //A thread that allows the driver to use the flipping mechanisms.
    private class FlipThread extends Thread {

        //Sets the name of the Thread to be referenced later.
        public FlipThread() {
            this.setName("FlipThread");

        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    double upDegree = 90;
                    double sorterUpDegree = 10;

                    //Alows the thread to detect if the 'a' button is pressed, and then sets the
                    //desired servo to a certain position.
                    if (gamepad2.a) {
                        setServos(hardware.flipServo1, hardware.flipServo2, upDegree);

                        //While hardware.flipLimit is not defined and the a button is pressed, then idle.
                        while (opModeIsActive() && !hardware.flipLimit.getState()) {
                            idle();
                        }

                        setServos(hardware.sorterServo1, hardware.sorterServo2, sorterUpDegree);

                    } else {

                        //If the 'a' button is not pressed, then return the servos to their default position.
                        setServos(hardware.flipServo1, hardware.flipServo2, 0);
                        setServos(hardware.sorterServo1, hardware.sorterServo2, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //A method that jiters Servos in order to Aid with mineral depositing.
    public void jitter(Servo[] servos, double numIntervals, int intevalAmount) {

        //For the desired length of the shaking (numIntervals), then offset each servo by the desired
        //magnitude (intervalAmount) rapidly.
        for (int i = 0; i < numIntervals; i++) {
            setServos(servos[0], servos[1], servos[0].getPosition() - intevalAmount);

            sleep(100 * intevalAmount / 5);

            setServos(servos[0], servos[1], servos[0].getPosition() + intevalAmount);
        }
    }

    //A method that sets the desired Servos to a specific position.
    public void setServos(Servo correct, Servo incorrect, double degree) {
        correct.setPosition(degree / 180);
        incorrect.setPosition(degree / 180 - 1);
    }
}