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

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;

@TeleOp(name = "testMineral", group = "13330 Pulsar")
public class TestMineralDetect extends LinearOpMode {

    private MineralDetection mineralDetection;
    private Console console;

    @Override
    public void runOpMode() {

        this.console = new Console(this);
        mineralDetection = new MineralDetection(this);

        mineralDetection.vuforiaInit();
        MineralPosition mineralPosition = null;
        AssetManager am = hardwareMap.appContext.getAssets();
        Bitmap sample = null;


        try {
           sample = BitmapFactory.decodeStream(am.open("sample2.jpg"));
        } catch (IOException e){}


        waitForStart();

        if(gamepad1.a){
            try {
                sample = mineralDetection.getImage();
            } catch (InterruptedException e){
                console.Log("Broke", "");
            }
        } else if (gamepad1.b){
            try {
                sample = BitmapFactory.decodeStream(am.open("sample2.jpg"));
            } catch (IOException e){}
        }

        console.Log("color profile", sample.getConfig());

        mineralPosition = mineralDetection.getPosition(sample);

        console.Log("mineral", mineralPosition);

        console.Update();
        idle();

    }
}
