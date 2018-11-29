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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

    private Integer POSITION = null;

    @Override
    public void runOpMode() {

        //initializes other classes
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.console = new Console(this.hardware, this.robot, this);

        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated())
            idle();

        console.Status("imu inited");

        // waits until the program is started
        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.dpad_left){ // mineral left position.

                robot.rotate(-50, 0.1, 0.90); // turns toward left mineral.
                sleep(100);
                robot.drive(0.25, 1100); // drives into it.
                sleep(100);
                robot.rotate(80, 0.1, 0.90); //turns toward depo.
                sleep(100);
                robot.drive(0.50, 3000); //TODO: Is .5 power too high?
                sleep(100);
                robot.drive(-0.50, 3200); //drives backward into crater.

            } else if( gamepad1.dpad_up){ // mineral middle position.

                robot.rotate(-75, 0.1, 0.90); //turns towards middle mineral.
                sleep(100);
                robot.drive(0.25, 1100); //drives into it.
                sleep(100);
                robot.rotate(75, 0.1, 0.90); // turns to original rotation.
                sleep(100);
                robot.drive(0.25, 200); // moves slightly forward after turning.
                sleep(100);
                robot.rotate(30, 0.1, 0.90); //turns toward depo.
                sleep(100);
                robot.drive(0.50, 3000); //drives forward into depo.
                sleep(100);
                robot.drive(-0.50, 3200); //drives backward into crater.

            } else if( gamepad1.dpad_right){// mineral right position.

                robot.rotate(-80, 0.1, 0.90); //turns all the way around.
                sleep(100);
                robot.drive(0.5,100); // moves out a little bit.
                sleep(100);
                robot.rotate(-23, 0.15, 0.85); //turns toward right mineral.
                sleep(100);
                robot.drive(0.25, 1100); // drives into mineral.
                sleep(100);
                robot.rotate(103, 0.1, 0.90); //turns back to original rotation.
                sleep(100);
                robot.drive(0.25, 300); // drives forward a bit after driving.
                sleep(100);
                robot.rotate(80, 0.1, 0.90); //turns toward depo.
                sleep(100);
                robot.drive(0.50, 3000); //drives forward into depo.
                sleep(100);
                robot.drive(-0.50, 3200); //drives backward into crater.

            }
            console.Log("angle", robot.getAngle());
            console.Update();
        }
    }
}
