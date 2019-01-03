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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ControllerMethods {
    private Hardware hardware;
    private LinearOpMode context;
    private Robot robot;
    private Console console;


    //constructor that allows the Robot class to use opModes and hardware
    public ControllerMethods(Hardware hardware, LinearOpMode context, Robot robot) {
        this.hardware = hardware;
        this.context = context;
        this.robot = robot;
        this.console = new Console(hardware, this.robot, context);
    }

    public void speedToggle(float speed){
        if(speed == 0){
            robot.fastMode = true;
        } else if (speed == 1){
            robot.fastMode = false;
        }
    }

    public void rightDrivePower(float power){
        robot.setPowerRight(-power);
    }

    public void leftDrivePower(float power){
        robot.setPowerLeft(-power);
    }

    public void setArmPower(float position){
        robot.setArmPower((int)position);
    }

    public void openArm(float nothing){
        robot.openArm();
    }

    public void setArmPowerManual(float power){
        hardware.arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.arm1.setPower(power * 0.2);

    }

    public void setArmPosition(float position){
        robot.setArmPosition(position + 0.5);
    }

    public void setMineralKickerPosition(float degrees){
        robot.setMineralKickerPosition(degrees);
    }

    public void setLifterPosition(float position){
        robot.setLifterPositions((int) position, 1);
    }

    public void setLifterPositionManual(float position){
        hardware.lifter.setPower(position);
    }

    public void initArm(float nothing){
        robot.initArm();
    }

    public void setCollector(float power){
        robot.collect(power);
    }
}
