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
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Crater Autonomous", group = "13330 Pulsar")
public class autoCrater extends LinearOpMode {

    // ones = after which comp
    // hundredths = which practice
    public double version = 1.04;

    // instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;
    public MineralDetection mineralDetection;

    public MineralPosition mineralPosition = null;
    public Bitmap sample = null;


    @Override
    public void runOpMode() {

        //initializes other classes
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.console = new Console(this.hardware, this.robot, this);
        this.mineralDetection = new MineralDetection(this);

        //initializes vuforia
        mineralDetection.vuforiaInit(hardwareMap);


        //region Initialization

        console.Status("waiting for IMU initialization");

        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated())
            idle();


        // prints out various statistics for debugging
        console.initStats(version, "Autonomous");
        console.Update();

        //endregion
        hardware.depositor.setPosition(0);

        // waits until the program is started
        waitForStart();

        //region Autonomous

        try {
            sample = mineralDetection.getImage();
        } catch (InterruptedException e) {
            console.Log("Broke", "");
        }

        mineralPosition = mineralDetection.getPosition(sample, 2);

        switch (mineralPosition) {
            case RIGHT:
                hardware.lifter.setPower(-1);
                sleep(12000);

                hardware.lifter.setPower(0);


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
                break;
            case CENTER:
                hardware.lifter.setPower(-1);

                sleep(12000);
                robot.setMineralKickerPosition(1);
                hardware.lifter.setPower(0);

                drive(3150, true);
                robot.setMineralKickerPosition(0);
                drive(1500, false);
                rotate(85);
                drive(2000, true, 0.5);
                rotate(42);
                drive(4000, true);
                rotate(80);
                robot.depositMineral();
                rotate(-80);
                drive(2700, false, 0.5);
                break;
            case LEFT:
                hardware.lifter.setPower(-1);

                sleep(12000);

                hardware.lifter.setPower(0);

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
                break;
        }
        //endregion
    }
    public void drive(double time, boolean forward){
        if(forward)
            robot.drive(robot.drivePower, time);
        else
            robot.drive(-robot.drivePower, time);

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

