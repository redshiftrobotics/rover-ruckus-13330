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


/**
 * Code that allows the Drivers to use controllers and drive the robot as well as its mechanisms.
 */

@TeleOp(name = "TeleOp", group = "FINAL")
public class teleop extends LinearOpMode {

    // instances of hardware, robot, and text
    private Hardware hardware;
    private MecanumChassis mecanumChassis;
    private Console console;
    private PID pid;

    private double speed = 0.45;

    double timer = 0;


    double flipUpDegree = 10;
    double flipReadyDegree = 90;
    double flipDownDegree = 110;
    double sorterUpDegree = 110;
    double sorterReadyDegree = 80;
    double timerWait = 100;
    int masterPower = 1;


    CollectorPosition collectorPosition = CollectorPosition.UP;


    @Override
    public void runOpMode() {

        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.mecanumChassis = new MecanumChassis(this, hardware.zeroPowerBehavior);
        this.pid = new PID();

        //initializes other classes
        Thread driveThread = new DriveThread();
        Thread collectorThread = new CollectorThread();
        Thread flipThread = new FlipThread();

        console.Status("Ready to start");

        waitForStart();


        driveThread.start();
        collectorThread.start();
        flipThread.start();


        while (opModeIsActive()) {
            console.Log("Status", "Running");
            console.Log("Timer", timer);
            console.Log("Collector Hinge Position", collectorPosition + " : " + hardware.collectorHinge.getCurrentPosition());
            console.Log("Lifter Position", hardware.lifter.getCurrentPosition());
            console.Log("Collector Hinge Position", hardware.collectorHinge.getCurrentPosition());
            console.Log("Flipper Position", hardware.flipper.getCurrentPosition());
            console.Update();

            idle();
        }

        driveThread.interrupt();
        collectorThread.interrupt();
        flipThread.interrupt();
    }


    //Thread that allows the driver to drive the mecanum chassis.
    private class FlipThread extends Thread {

        //sets the name of the thread to be referenced later.
        public FlipThread() {
            this.setName("FlipThread");

        }

        @Override
        public void run() {

            try {
                while (!isInterrupted()) {


                    //helps robot performance (rids of unnecessary loops)
                    //Alows the thread to detect if the 'a' button is pressed, and then sets the
                    //desired servo to a certain position.
                    if (gamepad2.a) {

                        setServos(hardware.topFlipL, hardware.topFlipR, sorterUpDegree, true);

                    } else {
                        //If the 'a' button is not pressed, then return the servos to their default position.

                        setServos(hardware.topFlipL, hardware.topFlipR, sorterReadyDegree, true);


                        timer = 0;
                    }


                }
            }
            // an error occurred in the run loop.
            catch (Exception e) {
                e.printStackTrace();
            }
        }
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


                    if(gamepad1.dpad_up)
                        masterPower = -1;
                    else if (gamepad1.dpad_down)
                        masterPower = 1;


                    mecanumChassis.driveS(gamepad1.left_stick_x * speed * masterPower, -gamepad1.left_stick_y * speed * masterPower, -gamepad1.right_stick_x * speed);


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
        public CollectorThread() {
            this.setName("CollectorThread");

        }

        @Override
        public void run() {
            hardware.collectorHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            try {
                while (!isInterrupted()) {

                    //Allows the thread to detect if the left bumper has been pressed, which will then set
                    //the position of the servo motor to a certain position.
                    if (gamepad2.left_bumper) {
                        collectorPosition = CollectorPosition.values()[(collectorPosition.ordinal() + 1) % CollectorPosition.values().length];
                        sleep(200);
                    }


                    //Based on the collector position, allows the Thread to set a specific position for
                    //the collector hinge.
                    switch (collectorPosition) {
                        case UP:
                            hardware.collectorHinge.setTargetPosition(-34);
                            break;
                        case DOWN:
                            hardware.collectorHinge.setTargetPosition(-115);
                            break;
                        case CENTER:
                            hardware.collectorHinge.setTargetPosition(-83);
                            break;
                    }

                    hardware.collectorHinge.setPower(-gamepad2.left_stick_y);
                    hardware.extenderWheel.setPower(-gamepad2.right_stick_y);


                    //If that position is equal to anything but UP, the collector's power will be set
                    //to the power of the right joystick.
                    //if (collectorPosition != CollectorPosition.UP) {

                    //}

                    int power = 0;
                    power += gamepad2.dpad_up ? 1 : 0;
                    power -= gamepad2.dpad_down ? 1 : 0;

                    hardware.lifter.setPower(power * 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //A method that sets the desired Servos to a specific position.
    public void setServos(Servo servo1, Servo servo2, double degree, boolean inverse) {
        if (inverse) {
            servo1.setPosition((degree) / 180);
            servo2.setPosition(1 - ((degree) / 180));
        } else {
            servo1.setPosition(degree / 180);
            servo2.setPosition(degree / 180);
        }
    }

}