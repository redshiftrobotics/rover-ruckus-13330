/* Copyright (c) 2018 FIRST. All rights reserved.
*
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

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "AutoTesting", group = "13330 Pulsar")
public class AutoTesting extends LinearOpMode {

    // ones = after which comp
    // hundredths = which practice
    public double version = 1.05;

    // instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;
    public MineralDetection mineralDetection;

    public MineralPosition mineralPosition = null;
    public Bitmap sample = null;

    private LaunchPosition launchPosition = LaunchPosition.NULL;

    public double driveSpeed = 1; // dictates the total drive speed.

    @Override
    public void runOpMode() {

        //initializes other classes
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.console = new Console(this.hardware, this.robot, this);


        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated())
            idle();

        console.Status("imu inited, waiting for launch position. Drive Speed: " + driveSpeed);



        // waits until the program is started
        while ( launchPosition == LaunchPosition.NULL) {
            if(gamepad1.right_bumper) {
                launchPosition = LaunchPosition.DEPO;
                console.Status("Depo Position");
            }
            else if(gamepad1.left_bumper) {
                launchPosition = LaunchPosition.CRATER;
                console.Status("Crater Position");

            }

            idle();
        }
        waitForStart();

        while(opModeIsActive()){

            hardware.depositor.setPosition(0);

            switch (launchPosition){
                case CRATER:
                    if(gamepad1.dpad_left){ // mineral left position.
                        robot.setLifterPositions(-29201, 1);
                        sleep(11500);
                        rotate(18);
                        robot.setMineralKickerPosition(1);
                        drive(3000, true);
                        robot.setMineralKickerPosition(0);
                        drive(1250, false);
                        rotate(40);
                        drive(1300, true, 0.5);
                        rotate(50);
                        drive(4500, true);
                        rotate(80);
                        robot.depositMineral();
                        rotate(-80);
                        drive( 2700, false, 0.5);

                    } else if( gamepad1.dpad_up){ // mineral middle position.
                        robot.setLifterPositions(-29201, 1);
                        sleep(11500);
                        robot.setMineralKickerPosition(1);
                        drive(3150, true);
                        robot.setMineralKickerPosition(0);
                        drive(1500, false);
                        rotate(85);
                        drive(1700, true, 0.5);
                        rotate(42);
                        drive(4000, true);
                        rotate(80);
                        robot.depositMineral();
                        rotate(-80);
                        drive(2700, false, 0.5);

                    } else if( gamepad1.dpad_right){// mineral right position.
                        robot.setLifterPositions(-29201, 1);
                        sleep(11500);
                        rotate(-15);
                        robot.setMineralKickerPosition(1);
                        drive(3000, true);
                        robot.setMineralKickerPosition(0);
                        drive(1250, false);
                        rotate(105);
                        drive(2000, true, 0.5);
                        rotate(42);
                        drive(4000, true);
                        rotate(80);
                        robot.depositMineral();
                        rotate(-80);
                        drive( 2700, false, 0.5);

                    }
                    break;

                case DEPO:
                    if(gamepad1.dpad_left){ // mineral left position.
                        robot.setLifterPositions(robot.lifterPositions[2], 1);
                        sleep(11500);
                        rotate(18);
                        robot.setMineralKickerPosition(1);
                        drive(4800, true);
                        robot.setMineralKickerPosition(0);
                        rotate(-60);
                        drive(1500, true);
                        rotate(80);
                        robot.depositMineral();
                        rotate(-83);
                        drive(2500, false, 0.5);

                    } else if( gamepad1.dpad_up){ // mineral middle position.
                        robot.setLifterPositions(robot.lifterPositions[2], 1);
                        sleep(11500);
                        robot.setMineralKickerPosition(1);
                        drive(5000, true);
                        robot.setMineralKickerPosition(0);
                        rotate(80);
                        robot.depositMineral();
                        drive(1500, true);
                        rotate(40);
                        drive(2500, true, 0.5);

                    } else if( gamepad1.dpad_right){// mineral right position.
                        robot.setLifterPositions(robot.lifterPositions[2], 1);
                        sleep(11500);
                        rotate(-15);
                        robot.setMineralKickerPosition(1);
                        drive(4500, true);
                        robot.setMineralKickerPosition(0);
                        rotate(80);
                        drive(1900, true);
                        rotate(13);
                        robot.depositMineral();
                        drive(1450, true);
                        rotate(40);
                        drive(2500, true, 0.5);

                    }
            }
        }

        robot.setLifterPositions(0,1);
    }

    public void drive(double time, boolean forward){
        if(forward)
            robot.drive(robot.drivePower, time * robot.timeMultiplier);
        else
            robot.drive(-robot.drivePower, time* robot.timeMultiplier);

        sleep((long)robot.sleepTime);

    }
    public void drive(double time, boolean forward, double speed){
        if(forward)
            robot.drive(speed, time * robot.timeMultiplier);
        else
            robot.drive(-speed, time * robot.timeMultiplier);

        sleep((long)robot.sleepTime);

    }

    public void rotate(int degrees){
        robot.rotate(degrees, 0.7, robot.turnThreashold);
        sleep((long)robot.sleepTime);

    }
}
