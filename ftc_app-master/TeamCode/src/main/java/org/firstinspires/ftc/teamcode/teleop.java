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

    private double speed  = 0.5;

    private boolean g2LeftBumperState = false;

    @Override
    public void runOpMode() {

        //initializes other classes
        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.mecanumChassis = new MecanumChassis(this, hardware.zeroPowerBehavior);


        waitForStart();

        runBackground();

        while (opModeIsActive()) {

            //yup
            mecanumChassis.driveS(-gamepad1.left_stick_x * speed, gamepad1.left_stick_y * speed, -gamepad1.right_stick_x * speed);

            console.Status("Angle: " + mecanumChassis.getControllerAngle(gamepad1.left_stick_x, gamepad1.left_stick_y));

            //helps robot performance (rids of unnecessary loops)
            idle();

            if (gamepad2.left_bumper) {
                g2LeftBumperState = true;
            } else {
                g2LeftBumperState = false;
            }
        }
    }

//    public void runMultiple(Method[] methods, final LinearOpMode context){
//        //array list of threads
//        ArrayList<Thread> threads = new ArrayList<>();
//
//        //creates a thread for each method
//        for (final Method method : methods) {
//
//            //adds the thread
//            threads.add(new Thread() {
//                public void run() {
//                    try {
//                        //invokes the method on context
//                        method.invoke(context);
//                    } catch (Exception ignored){}
//                }
//            });
//        }
//
//        //for each thread
//        for (Thread thread : threads){
//
//            //start it
//            thread.start();
//        }
//    }

    public void runBackground(){
        Thread thread1 = new Thread(){
            public void run() {
                extendCollector(hardware.extenderWheel, hardware.collectorHinge, hardware.collector);
            }
        };

        thread1.start();

        Thread thread2 = new Thread(){
            public void run() {
                liftFlip(hardware.flipServo1, hardware.flipServo2, hardware.flipLimit);
            }
        };

        thread2.start();
    }

    public void extendCollector(DcMotor extenderWheel, DcMotor collectorHinge, DcMotor collector){
        while(opModeIsActive()) {
            CollectorPosition collectorPosition = CollectorPosition.UP;

            extenderWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            collectorHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            collector.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            extenderWheel.setPower(-gamepad2.left_stick_y);

            if (gamepad2.left_bumper) {
                collectorPosition = CollectorPosition.values()[(collectorPosition.ordinal() + 1) % CollectorPosition.values().length];
                sleep(100);
            }

            switch (collectorPosition) {
                case UP:
                    collectorHinge.setTargetPosition(0);
                    break;
                case DOWN:
                    collectorHinge.setTargetPosition(1000);
                    break;
                case CENTER:
                    collectorHinge.setTargetPosition(500);
                    break;
            }

            collectorHinge.setPower(1);

            if (collectorPosition != CollectorPosition.UP) {
                collector.setPower(gamepad2.right_stick_y);
            }
        }
    }

    public void liftFlip(Servo flipServo1, Servo flipServo2, DigitalChannel flipLimit){
        while(opModeIsActive()) {

            double upDegree = 90;

            if (gamepad2.a) {
                setServos(flipServo1, flipServo2, upDegree);

                while (opModeIsActive() && !flipLimit.getState() && gamepad2.a) {
                    idle();
                }

                jitter(new Servo[] {flipServo1, flipServo2}, 3, 100);

            } else {
                setServos(flipServo1, flipServo2, 0);
            }
        }
    }

    public void jitter(Servo[] servos, double numIntervals, int intevalAmount){
        for(int i = 0; i < numIntervals; i++) {
            setServos(servos[0], servos[1], servos[0].getPosition() - intevalAmount);

            sleep(100 * intevalAmount/5);

            setServos(servos[0], servos[1], servos[0].getPosition() + intevalAmount);
        }
    }

    public void setServos(Servo correct, Servo incorrect, double degree){
        correct.setPosition(degree/180);
        incorrect.setPosition(degree/180 - 1);
    }

}
