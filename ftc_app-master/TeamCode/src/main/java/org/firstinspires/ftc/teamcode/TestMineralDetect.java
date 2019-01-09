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

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;

/**
 * Tests the detection and recognition of minerals.
 */

@TeleOp(name = "testMineralDetect", group = "13330 Pulsar")
public class TestMineralDetect extends LinearOpMode {

    private MineralDetection mineralDetection;
    private Console console;
    private Hardware hardware;
    private Imu imu;

    @Override
    public void runOpMode() {

        this.console = new Console(this); // initialization of Console class
        mineralDetection = new MineralDetection(this); // initialization of Mineral Detection class
        hardware = new Hardware(this);
        imu = new Imu(this);

        mineralDetection.vuforiaInit(hardwareMap); // initialization of vuforia

        AssetManager am = hardwareMap.appContext.getAssets(); // gets android assets [FtcRobotController>Assets]
        MineralPosition mineralPosition = null;
        Bitmap sample = null;

        while (!imu.imu.isGyroCalibrated())
            idle();

        waitForStart(); // waits until play is pressed

        while (opModeIsActive()) {

            if (gamepad1.a) { // attempts to take photo
                try {
                    sample = mineralDetection.getImage();
                } catch (InterruptedException e) {
                }

                console.Log("color profile", sample.getConfig());
            } else if (gamepad1.b) { // attempts to get photo from assets
                try {
                    sample = BitmapFactory.decodeStream(am.open("sample2.jpg"));
                } catch (IOException e) {
                }

                console.Log("color profile", sample.getConfig());
            }

            if (gamepad1.start) { // analyzes images
                mineralPosition = mineralDetection.getPosition(sample, 2);
                console.Log("mineral", mineralPosition);
            }

            console.Update(); // updates log

            idle(); // idles robot
        }
    }
}
