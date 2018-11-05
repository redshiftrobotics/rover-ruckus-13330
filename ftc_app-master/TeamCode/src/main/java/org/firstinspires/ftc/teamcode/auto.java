/* Copyright (c) 2017 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name = "Autonomous", group = "13330 Pulsar")
public class auto extends LinearOpMode {

    public double version = 0.05; // version name for organization.



    //this.instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;

    private Integer POSITION = null;



    @Override
    public void runOpMode() {

        //makes an instance of hardware with this LinearOpMode as its context
        this.hardware = new Hardware(this);
        //makes in instance or robot with this hardware as context
        this.robot = new Robot(this.hardware, this);
        //makes an instance of Text with Hardware and Robot as its context
        this.console = new Console(this.hardware, this.robot, this);


        console.Log("Action:", "waiting for IMU initialization");
        console.Update();


        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated() || POSITION == null) {

            idle();
            if(gamepad1.dpad_right) {
                POSITION = 1;
                console.Log("Crater Position", "");
                console.Update();
            }
            else if(gamepad1.dpad_left) {
                POSITION = 0;
                console.Log("Depo Position", "");
                console.Update();
            }
            else if(gamepad1.dpad_down){
                POSITION = 3;
                console.Log("Test Mode Enabled", "");
                console.Update();
            }
        }

        //makes sure the color sensor is enabled

        //prints out various statistics for debugging purposes
        console.initStats(version, "Autonomous");
        console.Update();


        //waits until the program is started
        waitForStart();

        hardware.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE; //makes it so the motors can brake.


        //robot.moveMineralKicker(1,500);
        telemetry.addData("KickerPos", hardware.mineralKicker.getPosition());


        switch(POSITION) { //changes the auto based on our starting position.
            case (0):
                robot.drive(0.1, 1000); //moves forward
                robot.rotate(-135, 0.5, 0.90); //turns right
                robot.drive( 0.3, 1000); //moves forward to hit the mineral and then brakes to release the team marker.
                robot.setPowerLeft(0);
                robot.setPowerRight(0);
                break;
            case (1):
                robot.drive(1, 1000); //moves forward and then brakes to release the marker.
                robot.setPowerLeft(0);
                robot.setPowerRight(0);
                break;
            default:

                break;

        }

        robot.rotate(90,0.1,0.90);

        telemetry.addData("Auto Completed.", "");
        console.Update();

    }
}
