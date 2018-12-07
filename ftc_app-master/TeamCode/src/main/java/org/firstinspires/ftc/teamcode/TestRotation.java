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

@TeleOp(name = "testRotation", group = "13330 Pulsar")
public class TestRotation extends LinearOpMode {

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

        robot.setMineralKickerPosition(0.37);

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
            switch (launchPosition){
                case CRATER:
                    if(gamepad1.dpad_left){ // mineral left position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-45, 1, 0.90); // turns toward left mineral.
                        sleep(100);
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;

                        robot.setZeroPowerBehavior();
                        robot.drive(0.4 , 1000 ); // drives into it.
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
                        robot.setZeroPowerBehavior();
                        robot.setMineralKickerPosition(0.37);



                    } else if( gamepad1.dpad_up){ // mineral middle position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-85, 1, 0.90); //turns towards middle mineral.
                        sleep(100);
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
                        robot.setZeroPowerBehavior();
                        robot.drive(0.4 , 1000 ); //drives into it.
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
                        robot.setZeroPowerBehavior();
                        robot.setMineralKickerPosition(0.37);




                    } else if( gamepad1.dpad_right){// mineral right position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-80, 1, 0.90); //turns all the way around.
                        sleep(100);
                        robot.drive(0.4 ,100 ); // moves out a little bit.
                        sleep(100);
                        robot.rotate(-25, 1, 0.85); //turns toward right mineral.
                        sleep(100);
                        robot.drive(0.4 , 900 ); // drives into mineral.
                        robot.setMineralKickerPosition(0.37);

                    }
                    break;

                case DEPO:
                    if(gamepad1.dpad_left){ // mineral left position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-38, 1, 0.90); // turns toward left mineral.
                        sleep(100);
                        robot.drive(0.4 , 3000); // drives into it and into depo.
                        robot.setMineralKickerPosition(0.37);
                        sleep(100);
                        robot.rotate(-30, 1, 0.9); // turns slightly toward the wall.
                        sleep(100);
                        robot.drive(0.4 , 2200 ); // drives across edge of depo.
                        sleep(100);
                        robot.rotate(-80, 1, 0.9); // turns toward crater.
                        sleep(100);
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
                        robot.setZeroPowerBehavior();
                        robot.drive(0.6 , 4000 ); // drives into it.
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
                        robot.setZeroPowerBehavior();


                    } else if( gamepad1.dpad_up){ // mineral middle position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-75, 1, 0.9); //turns towards middle mineral.
                        sleep(100);
                        robot.drive(0.4 , 1000); // drives into mineral and in front of depo.
                        sleep(100);
                        robot.setMineralKickerPosition(0.37);
                        robot.drive(-0.4 , 2100); //drives backward to original position.
                        sleep(100);
                        robot.rotate(-75, 1, 0.90); // turns toward crater.
                        sleep(100);
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
                        robot.setZeroPowerBehavior();
                        robot.drive(0.4 , 5000 ); //drives into crater.
                        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
                        robot.setZeroPowerBehavior();


                    } else if( gamepad1.dpad_right){// mineral right position.
                        robot.setMineralKickerPosition(0);
                        robot.rotate(-90, 0.5, 0.90); //turns all the way around.
                        sleep(100);
                        robot.drive(0.5 ,100); // moves out a little bit.
                        sleep(100);
                        robot.rotate(-20, 1, 0.85); //turns toward right mineral.
                        sleep(100);
                        robot.drive(0.5, 2000); // drives into mineral.
                        sleep(100);
                        robot.setMineralKickerPosition(0.37);
                        robot.rotate(75, 0.5, 0.90);
                        sleep(100);
                        robot.drive(0.05, 3000);
                        sleep(100);
                        robot.drive(-0.08, 2000);

                    }
            }

            console.Log("angle", robot.getAngle());
            console.Update();
        }
    }
}
