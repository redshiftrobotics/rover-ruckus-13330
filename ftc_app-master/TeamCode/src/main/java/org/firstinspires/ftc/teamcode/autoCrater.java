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

    public double drivePower = 0.2;
    public double turnThreashold = 0.9;
    public double sleepTime = 50;

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

        // waits until the program is started
        waitForStart();

        /*
        Crater
	Center
		F 3250 0.2
		F 1000 -0.21304
		R 82 0.9
		F 3800 0.2
		R 40 0.9
		F 4000 0.2
		R 80 0.9
		R -80 0.9
		F 8000 -0.201
	Right
		R -15 0.9
		F 3000 0.2
		F 1250 -0.2
		R 105 0.9
		F 4800 0.2
		R 30 0.9
		F 4500 0.2
		R 80 0.9
		R -78 0.9
		F 6300 0.2
	Left
		R 15 0.9
		F 3100 0.2
		F 1300 -0.2
		R 50 0.9
		F 3000 0.2
		R 40 0.9
		F 4500 0.2
		R 80 0.9
		R -80 0.9
		F 6300 -0.2
         */

        //region Autonomous

        //attempts take photo
        try {
            sample = mineralDetection.getImage();
        } catch (InterruptedException e) {
            console.Log("Broke", "");
        }

        mineralPosition = mineralDetection.getPosition(sample, 2);

        switch (mineralPosition) {
            case RIGHT:
                rotate(-15);
                drive(3000, true);
                drive(1250, false);
                rotate(105);
                drive(4800, true);
                rotate(30);
                drive(4500, true);
                rotate(80);
                //TODO deposit mineral
                rotate(78);
                break;
            case CENTER:
                drive(3250, true);
                drive(1000, false);
                rotate(82);
                drive(3800, true);
                rotate(40);
                drive(4000, true);
                rotate(80);
                //TODO deposit mineral
                rotate(-78);
                drive(6300, false);
                break;
            case LEFT:
                rotate(15);
                drive(3100, true);
                drive(1300, false);
                rotate(50);
                drive(3000, true);
                rotate(40);
                drive(4500, true);
                rotate(80);
                //TODO deposit mineral
                rotate(-80);
                drive(6300, false);
                break;
        }
    }
    //endregion

    public void drive(double time, boolean forward){
        if(forward)
            robot.drive(drivePower, time);
        else
            robot.drive(-drivePower, time);

        sleep(50);

    }

    public void rotate(int degrees){
        robot.rotate(degrees, 0.7, turnThreashold);
        sleep(50);
    }
}

