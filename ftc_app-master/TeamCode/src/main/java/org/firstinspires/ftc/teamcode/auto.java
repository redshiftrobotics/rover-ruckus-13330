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

    // ones = after which comp
    // hundredths = which practice
    public double version = 1.00;

    // instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;

    private Integer POSITION = null;



    @Override
    public void runOpMode() {

        // initializes other classes
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.console = new Console(this.hardware, this.robot, this);


        console.Log("Action:", "waiting for IMU initialization");
        console.Update();


        //wait for the IMU to be initiated
        while (!hardware.imu.isGyroCalibrated())
            idle();


        console.Log("Action:", "waiting for POSITION input");
        console.Update();


        while (POSITION == null) {
            if(gamepad1.dpad_left) {
                POSITION = 0;
                console.Log("Depo Position", "");
                console.Update();
            }
            else if(gamepad1.dpad_right) {
                POSITION = 1;
                console.Log("Crater Position", "");
                console.Update();
            }
            else if(gamepad1.dpad_down){
                POSITION = 3;
                console.Log("Test Mode Enabled", "");
                console.Update();
            }

            idle();
        }

        sleep(500);

        // prints out various statistics for debugging
        console.initStats(version, "Autonomous");
        console.Update();


        // waits until the program is started
        waitForStart();


    // AUTO SCRIPT

        // runs different code depending on starting position
        switch(POSITION) {

            // depo position
            case (0):

                //code me...

                break;


            // crater position
            case (1):

                //code me...

                break;


            default:

                //code me...

                break;

        }
    }
}
