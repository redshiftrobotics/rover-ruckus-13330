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
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReadConfig {
    private Hardware hardware; // get the context of other classes to use
    private Robot robot;
    private LinearOpMode context;
    private Console console;
    private ControllerMethods cm;


    public ReadConfig(Hardware hardware, Robot robot, Console console, LinearOpMode context) { // creates the context in this class
        this.hardware = hardware;
        this.robot = robot;
        this.context = context;
        this.console = console;
        this.cm = new ControllerMethods(hardware, context, robot);
    }


    private static int gA = 0;
    private static int gB = 1;
    private static int gX = 2;
    private static int gY = 3;
    private static int gright_bumper = 4;
    private static int gleft_bumper = 5;
    private static int gright_trigger = 6;
    private static int gleft_trigger = 7;
    private static int gright_stick_y = 8;
    private static int gleft_stick_y = 9;
    private static int gright_stick_x = 10;
    private static int gleft_stick_x = 11;
    private static int gdpad_right = 12;
    private static int gdpad_left = 13;
    private static int gdpad_up = 14;
    private static int gdpad_down = 15;

    //region Method/Params arrays
    private Method[] deviceMethods = {
            // gamepad 1 Assigned Methods
            null,       // A
            null,       // B
            null,       // X
            null,       // Y
            null,       // right bumper
            null,       // left bumper
            null,       // right trigger
            null,       // left trigger
            null,       // right stick y
            null,       // left stick y
            null,       // right stick x
            null,       // left stick x
            null,       // dpad right
            null,       // dpad left
            null,       // dpad up
            null,       // dpad down

            // gamepad 2 Assigned Methods
            null,       // A
            null,       // B
            null,       // X
            null,       // Y
            null,       // right bumper
            null,       // left bumper
            null,       // right trigger
            null,       // left trigger
            null,       // right stick y
            null,       // left stick y
            null,       // right stick x
            null,       // left stick x
            null,       // dpad right
            null,       // dpad left
            null,       // dpad up
            null        // dpad down
    };

    private float[] methodParams = {
            // gamepad 1 Assigned Methods
            0,       // A
            0,       // B
            0,       // X
            0,       // Y
            0,       // right bumper
            0,       // left bumper
            0,       // right trigger
            0,       // left trigger
            0,       // right stick y
            0,       // left stick y
            0,       // right stick x
            0,       // left stick x
            0,       // dpad right
            0,       // dpad left
            0,       // dpad up
            0,       // dpad down

            // gamepad 2 Assigned Methods
            0,       // A
            0,       // B
            0,       // X
            0,       // Y
            0,       // right bumper
            0,       // left bumper
            0,       // right trigger
            0,       // left trigger
            0,       // right stick y
            0,       // left stick y
            0,       // right stick x
            0,       // left stick x
            0,       // dpad right
            0,       // dpad left
            0,       // dpad up
            0        // dpad down
    };

    //endregion

    public void readFile(String fileName) {

        BufferedReader br;

        String[] validTokens = {
                "rightDrivePower",
                "leftDrivePower",
                "speedToggle",
                "setArmPower",
                "openArm",
                "setArmPowerManual",
                "initArm",
                "setArmPosition",
                "setMineralKickerPosition",
                "setLifterPosition",
                "setLifterPositionManual",
                "setCollector",
                "null",
        };

        String[] deviceNames = { //MUST BE IN ORDER with deviceMethods
                "gamepad1.a",
                "gamepad1.b",
                "gamepad1.x",
                "gamepad1.y",
                "gamepad1.right_bumper",
                "gamepad1.left_bumper",
                "gamepad1.right_trigger",
                "gamepad1.left_trigger",
                "gamepad1.right_stick_y",
                "gamepad1.left_stick_y",
                "gamepad1.right_stick_x",
                "gamepad1.left_stick_x",
                "gamepad1.dpad_right",
                "gamepad1.dpad_left",
                "gamepad1.dpad_up",
                "gamepad1.dpad_down",
                "gamepad2.a",
                "gamepad2.b",
                "gamepad2.x",
                "gamepad2.y",
                "gamepad2.right_bumper",
                "gamepad2.left_bumper",
                "gamepad2.right_trigger",
                "gamepad2.left_trigger",
                "gamepad2.right_stick_y",
                "gamepad2.left_stick_y",
                "gamepad2.right_stick_x",
                "gamepad2.left_stick_x",
                "gamepad2.dpad_right",
                "gamepad2.dpad_left",
                "gamepad2.dpad_up",
                "gamepad2.dpad_down"
        };


        AssetManager am = context.hardwareMap.appContext.getAssets();

        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,fileName);

            br = new BufferedReader(new FileReader(file));


            String x;

            for (x = br.readLine(); x != null; x = br.readLine()) {

                if ((x.charAt(0) == '/' && x.charAt(1) == '/') || x.isEmpty())
                    continue;

                // process each line and get tokens stripping all random characters away
                String[] tokens = x.split(":"); //split it up via : separators

                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replace("\t", ""); //remove all tabs
                    tokens[i] = tokens[i].replace(" ", ""); //remove all spaces
                }


                // Verify valid first token (device name)
                int devicePosition = Arrays.asList(deviceNames).indexOf(tokens[0]);
                if (devicePosition == -1) {
                    console.Log("config syntax error: unrecognized device: ", tokens[0]);
                    console.Update();
                    context.sleep(100000);
                    context.stop();
                }

                // Verify valid second token (operation)
                int operationPosition = Arrays.asList(validTokens).indexOf(tokens[1]);
                if (operationPosition == -1) {
                    console.Log("config syntax error: unrecognized operation: " + tokens[1], "");
                    console.Update();
                    context.sleep(100000);
                    context.stop();
                }


                // If no assigned operation, then assign null and skip rest of processing
                if (tokens[1] == null)
                    deviceMethods[devicePosition] = null;


                else {
                    try {
                        if (tokens.length > 2) {
                            // if real operation, then find data type and assign operation method address
                            if (tokens[2].equals("boolean")) {
                                deviceMethods[devicePosition] = cm.getClass().getMethod(tokens[1], float.class);

                                context.telemetry.addData("float", tokens[3]);
                                context.telemetry.update();
                                methodParams[devicePosition] = Float.parseFloat(tokens[3]);

                            } else if (tokens[2].equals("float")) {
                                deviceMethods[devicePosition] = cm.getClass().getMethod(tokens[1], float.class);
                            } else {
                                console.Log("config syntax error: operation not Boolean or Float", "");
                                console.Update();
                                context.sleep(100000);
                                context.stop();
                            }

                        }

                    } catch (NoSuchMethodException e) { //this will only happen if the method doesn't exist in the code
                        console.Log("Fatal Error: ", e);
                        console.Log("Stack Trace", e.getStackTrace()[0]);
                        console.Update();
                        e.printStackTrace();
                        context.sleep(100000);
                        context.stop();
                    }
                }
            }
        } catch (IOException e) {
            console.Log("Fatal Error: I/O error reading configuration file", e);
            console.Update();
            e.printStackTrace();
            context.sleep(100000);
            context.stop();
        }


        console.Log("Finished Initialization of " + fileName + " config.", "");
    }


    public void updateControls() {
        /* for reference....
        g1A = 0;
        g1B = 1;
        g1X = 2;
        g1Y = 3;
        g1right_bumper = 4;
        g1left_bumper = 5;
        g1right_trigger = 6;
        g1left_trigger = 7;
        g1right_stick_y = 8;
        g1left_stick_y = 9;
        g1right_stick_x = 10;
        g1left_stick_x = 11;
        g1dpad_right = 12;
        g1dpad_left = 13;
        g1dpad_up = 14;;
        g1dpad_down = 15;
        */

        try {
            // controller gamepad1
            if (context.gamepad1.a && deviceMethods[gA] != null && !Float.isNaN(methodParams[gA]))
                deviceMethods[gA].invoke(cm, methodParams[gA]);

            if (context.gamepad1.b && deviceMethods[gB] != null && !Float.isNaN(methodParams[gB]))
                deviceMethods[gB].invoke(cm, methodParams[gB]);

            if (context.gamepad1.x && deviceMethods[gX] != null && !Float.isNaN(methodParams[gX]))
                deviceMethods[gX].invoke(cm, methodParams[gX]);

            if (context.gamepad1.y && deviceMethods[gY] != null && !Float.isNaN(methodParams[gY]))
                deviceMethods[gY].invoke(cm, methodParams[gY]);

            if (context.gamepad1.right_bumper && deviceMethods[gright_bumper] != null && methodParams[gright_bumper] != 0)
                deviceMethods[gright_bumper].invoke(cm, methodParams[gright_bumper]);

            if (context.gamepad1.left_bumper && deviceMethods[gleft_bumper] != null && methodParams[gleft_bumper] != 0)
                deviceMethods[gleft_bumper].invoke(cm, methodParams[gleft_bumper]);

            if (deviceMethods[gright_trigger] != null)
                deviceMethods[gright_trigger].invoke(cm, context.gamepad1.right_trigger);

            if (deviceMethods[gleft_trigger] != null)
                deviceMethods[gleft_trigger].invoke(cm, context.gamepad1.left_trigger);

            if (deviceMethods[gright_stick_y] != null)
                deviceMethods[gright_stick_y].invoke(cm, context.gamepad1.right_stick_y);

            if (deviceMethods[gleft_stick_y] != null)
                deviceMethods[gleft_stick_y].invoke(cm, context.gamepad1.left_stick_y);

            if (deviceMethods[gright_stick_x] != null)
                deviceMethods[gright_stick_x].invoke(cm, context.gamepad1.right_stick_x);

            if (deviceMethods[gleft_stick_x] != null)
                deviceMethods[gleft_stick_x].invoke(cm, context.gamepad1.left_stick_x);

            if (context.gamepad1.dpad_right && deviceMethods[gdpad_right] != null && !Float.isNaN(methodParams[gdpad_right]))
                deviceMethods[gdpad_right].invoke(cm, methodParams[gdpad_right]);

            if (context.gamepad1.dpad_left && deviceMethods[gdpad_left] != null && !Float.isNaN(methodParams[gdpad_left]))
                deviceMethods[gdpad_left].invoke(cm, methodParams[gdpad_left]);

            if (context.gamepad1.dpad_up && deviceMethods[gdpad_up] != null && !Float.isNaN(methodParams[gdpad_up]))
                deviceMethods[gdpad_up].invoke(cm, methodParams[gdpad_up]);

            if (context.gamepad1.dpad_down && deviceMethods[gdpad_down] != null && !Float.isNaN(methodParams[gdpad_down]))
                deviceMethods[gdpad_down].invoke(cm, methodParams[gdpad_down]);


            //controller gamepad2
            if (context.gamepad2.a && deviceMethods[gA + 16] != null && !Float.isNaN(methodParams[gA + 16]))
                deviceMethods[gA + 16].invoke(cm, methodParams[gA + 16]);

            if (context.gamepad2.b && deviceMethods[gB + 16] != null && !Float.isNaN(methodParams[gB + 16]))
                deviceMethods[gB + 16].invoke(cm, methodParams[gB + 16]);

            if (context.gamepad2.x && deviceMethods[gX + 16] != null && !Float.isNaN(methodParams[gX + 16]))
                deviceMethods[gX + 16].invoke(cm, methodParams[gX + 16]);

            if (context.gamepad2.y && deviceMethods[gY + 16] != null && !Float.isNaN(methodParams[gY + 16]))
                deviceMethods[gY + 16].invoke(cm, methodParams[gY + 16]);

            if (context.gamepad2.right_bumper && deviceMethods[gright_bumper + 16] != null && !Float.isNaN(methodParams[gright_bumper + 16]))
                deviceMethods[gright_bumper + 16].invoke(cm, methodParams[gright_bumper + 16]);

            if (context.gamepad2.left_bumper && deviceMethods[gleft_bumper + 16] != null && !Float.isNaN(methodParams[gleft_bumper + 16]))
                deviceMethods[gleft_bumper + 16].invoke(cm, methodParams[gleft_bumper + 16]);

            if (deviceMethods[gright_trigger + 16] != null)
                deviceMethods[gright_trigger + 16].invoke(cm, context.gamepad2.right_trigger);

            if (deviceMethods[gleft_trigger + 16] != null)
                deviceMethods[gleft_trigger + 16].invoke(cm, context.gamepad2.left_trigger);

            if (deviceMethods[gright_stick_y + 16] != null)
                deviceMethods[gright_stick_y + 16].invoke(cm, context.gamepad2.right_stick_y);

            if (deviceMethods[gleft_stick_y + 16] != null)
                deviceMethods[gleft_stick_y + 16].invoke(cm, context.gamepad2.left_stick_y);

            if (deviceMethods[gright_stick_x + 16] != null)
                deviceMethods[gright_stick_x + 16].invoke(cm, context.gamepad2.right_stick_x);

            if (deviceMethods[gleft_stick_x + 16] != null)
                deviceMethods[gleft_stick_x + 16].invoke(cm, context.gamepad2.left_stick_x);

            if (context.gamepad2.dpad_right && deviceMethods[gdpad_right + 16] != null && !Float.isNaN(methodParams[gdpad_right + 16]))
                deviceMethods[gdpad_right + 16].invoke(cm, methodParams[gdpad_right + 16]);

            if (context.gamepad2.dpad_left && deviceMethods[gdpad_left + 16] != null && !Float.isNaN(methodParams[gdpad_left + 16]))
                deviceMethods[gdpad_left + 16].invoke(cm, methodParams[gdpad_left + 16]);

            if (context.gamepad2.dpad_up && deviceMethods[gdpad_up + 16] != null && !Float.isNaN(methodParams[gdpad_up + 16]))
                deviceMethods[gdpad_up + 16].invoke(cm, methodParams[gdpad_up + 16]);

            if (context.gamepad2.dpad_down && deviceMethods[gdpad_down + 16] != null && !Float.isNaN(methodParams[gdpad_down + 16]))
                deviceMethods[gdpad_down + 16].invoke(cm, methodParams[gdpad_down + 16]);

        } catch (IllegalAccessException | InvocationTargetException e) { //absolutely should NEVER happen
            console.Log("Fatal Error", e);
            console.Update();
            e.printStackTrace();
            context.sleep(100000);
            context.stop();
        }
    }
}
