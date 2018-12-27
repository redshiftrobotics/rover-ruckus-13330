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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TeleOp", group = "13330 Pulsar")
public class teleop extends LinearOpMode {

    // ones = after which comp
    // hundredths = which practice
    public double version = 1.04;

    // instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;
    private ReadConfig rc;

    @Override
    public void runOpMode() {

        //initializes other classes
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.console = new Console(this.hardware, this.robot, this);
        this.rc = new ReadConfig(this.hardware, this.robot, console, this);


        console.Log("Action:", "waiting for the IMU initialization");
        console.Update();

        //reads config file

        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated())
            idle();

        console.initStats(version, "TeleOp");
        console.Update();

        hardware.depositor.setPosition(0);
        robot.setMineralKickerPosition(1);

        waitForStart();

        //region Run Loop
        while (opModeIsActive()) {

            robot.updateSpeed();
            robot.checkDirection();
            robot.setZeroPowerBehavior();

            robot.setPowerRight(-gamepad1.right_stick_y);
            robot.setPowerLeft(-gamepad1.left_stick_y);

            if(gamepad1.a)
                robot.fastMode = false;
            else if (gamepad1.b)
                robot.fastMode = true;

            hardware.lifter.setPower(gamepad2.left_stick_y);

            if(gamepad1.dpad_up)
                robot.setLifterPositions(3, 1);
            if(gamepad1.dpad_down)
                robot.setLifterPositions(4, 1);
            if(gamepad1.dpad_right)
                robot.setLifterPositions(3, 1);
            if(gamepad1.dpad_left)
                robot.setLifterPositions(1, 1);

            hardware.arm2.setPosition(((Range.clip(gamepad2.right_stick_x, 0,1))-0.5)*2);


            console.Log("LIFT", hardware.lifter.getCurrentPosition());
            console.Log("Power", hardware.lifter.getPower());
            console.Update();

            idle();
        }

        //endregion
    }
}
