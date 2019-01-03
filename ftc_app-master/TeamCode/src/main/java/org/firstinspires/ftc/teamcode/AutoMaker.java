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
import android.os.Environment;
import android.widget.Toast;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A class that can help with coding an auto. Saves autos to files and can read them. Working on updating recording autos...
 */

@TeleOp(name = "Automaker")
public class AutoMaker extends LinearOpMode {

    private Input input;
    private Console console;

    private String craterFileName = "FOO";
    private String depoFileName = "FOO";

    private BufferedReader br;

    private MecanumChassis mecanumChassis;

    private MineralDetection mineralDetection;
    private MineralPosition mineralPosition;
    private int mineralDetectionSplits = 3;

    @Override
    public void runOpMode() throws InterruptedException {

        mecanumChassis = new MecanumChassis(this, DcMotor.ZeroPowerBehavior.BRAKE);

        Object[][] craterCommands = new Object[100][];
        Object[][] depoCommands = new Object[100][];

        //crater auto
        if (input.question(new String[]{"Crater", "Depo"}).equals("Crater")) {

            //if open file
            if (!input.yesOrNo("Create New?")) {
                console.Display("Opening File", 100);
                craterCommands = readToArray("craterAuto");

                //depo auto
            } else {
                if (!input.yesOrNo("Create New?")) {
                    console.Display("Opening File", 100);
                    depoCommands = readToArray("depoAuto");
                }
            }
        }
    }

    private int scanDepth = 0;

    /**
     * This reads a text file and converts it to an array.
     *
     * @param fileName The file name to read
     * @return
     */

    private Object[][] readToArray(String fileName) {
        //argh... i didn't want to use a list
        Object[][] array = new Object[100][];

        try {
            //gets the external directory
            File sdcard = Environment.getExternalStorageDirectory();
            //creates a empty file
            File file = new File(sdcard, fileName);

            //creates a buffered reader
            br = new BufferedReader(new FileReader(file));

            //for each line
            String x;

            //array line
            int y = 0;

            //for each line, if it is not null
            for (x = br.readLine(); x != null; x = br.readLine()) {

                //this is the syntax for commenting.
                //it will skip the line if the first three characters are: <--
                if ((x.charAt(0) == '<' && x.charAt(1) == '-' && x.charAt(2) == '-') || x.isEmpty())
                    continue;

                // process each line and get tokens stripping all random characters away
                String[] tokens = x.split(":"); //split it up via : separators

                //for each token
                for (int i = 0; i < tokens.length; i++) {
                    //remove all tabs
                    tokens[i] = tokens[i].replace("\t", "");
                    //remove all spaces
                    tokens[i] = tokens[i].replace(" ", "");

                    //add to the array the current token
                    array[y][i] = tokens[i];
                }

                //add to the array line count
                y++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    /**
     * Runs an array as an auto script
     *
     * @param array The Array to read
     * @param i The starting line of the array
     */

    private void runArray(Object[][] array, int i) {
        //for each line
        for (i = i; i < array.length; i++) {
            //case commands
            switch (array[i][0].toString()) {
                //motor control
                case "M":
                    //because builders are stupid
                    if (array[i].length > 2)
                        fault("motor control needs to have two tokens");

                    //finds the desired dc motor
                    DcMotor motor = hardwareMap.dcMotor.get(array[i][1].toString());

                    motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //sets the mode to run to position
                    motor.setTargetPosition(Integer.parseInt(array[i][2].toString())); //sets the target position
                    motor.setPower(1); //sets the power

                    //waits for end
                    while (motor.isBusy() && opModeIsActive()) {
                        sleep(10);
                    }

                    //switches the mode back to using power
                    motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    break;
                //drive
                case "D":
                    //because builders are stupid
                    if (array[i].length > 4)
                        fault("drive needs to have four tokens");

                    //drives with y,x,r from array
                    mecanumChassis.drive(Double.parseDouble(array[i][1].toString()), Double.parseDouble(array[i][2].toString()), Double.parseDouble(array[i][3].toString()));

                    //if token 4 is LIM run limit switch wait loop
                    if ((array[i][4].toString()).equals("LIM")) {
                        //TODO add limit switch while loop
                    }

                    //if its not using limit switch, then just wait until the time is up
                    else {
                        sleep(Long.parseLong(array[i][4].toString()));
                    }
                    break;
                //servo control
                case "L":
                    //because builders are stupid
                    if (array[i].length > 2)
                        fault("servo control needs to have 2 tokens");

                    //finds servo from array name
                    Servo servo = hardwareMap.servo.get(array[i][1].toString());

                    //sets the position to the array position
                    servo.setPosition(Double.parseDouble(array[i][2].toString()) / 180);

                    //waits for it to go
                    while (servo.getPosition() != Double.parseDouble(array[i][2].toString()) / 180 && opModeIsActive()) {
                        sleep(10);
                    }
                    break;
                case "SCAN":
                    //if no endScan was found
                    if (scanDepth == 1) {
                        fault("did not find endScan");
                        return;
                    }
                    scanDepth = 1;
                    try {
                        //scans for the mineral and takes the photo
                        mineralPosition = mineralDetection.getPosition(mineralDetection.getImage(), mineralDetectionSplits);
                    } catch(Exception e){ }
                    break;
                case "CASE LEFT":
                    if (scanDepth != 1) {
                        fault("did not find endScan");
                        return;
                    }

                    //if the mineral is left
                    if (mineralPosition == MineralPosition.LEFT) {
                        //process: this is not right, but general idea...call recursive
                        runArray(array, i);
                    }
                    //zoom ahead ignoring all entries until after endcase
                    while (array[i][0] != "ENDCASE") i = i + 1;
                    break;
                case "CASE CENTER":
                    if (scanDepth != 1) {
                        fault("did not find endScan");
                        return;
                    }

                    //if the mineral is center
                    if (mineralPosition == MineralPosition.CENTER) {
                        //process: this is not right, but general idea...call recursive
                        runArray(array, i);
                    }
                    //zoom ahead ignoring all entries until after endcase
                    while (array[i][0] != "ENDCASE") i = i + 1;
                    break;
                case "CASE RIGHT":
                    if (scanDepth != 1) {
                        fault("did not find endScan");
                        return;
                    }

                    //if the mineral is right
                    if (mineralPosition == MineralPosition.RIGHT) {
                        //process: this is not right, but general idea...call recursive
                        runArray(array, i);
                    }
                    //zoom ahead ignoring all entries until after endcase
                    while (array[i][0] != "ENDCASE") i = i + 1;
                    break;
                case "ENDSCAN":
                    scanDepth = 0;
                    break;

                case "END":
                    return;
            }
        }
    }

    /**
     * Saves an array as a string in a file
     *
     * @param array The array to save
     * @param fileName The fileName to save to
     */

    public void saveArray(Object[][] array, String fileName) {

        //creates a new stringBuilder (because i want to use .append)
        StringBuilder txtData = new StringBuilder();

        //for each line
        for (int i = 0; i < array.length; i++) {
            //for each token
            for (int j = 0; j < array[i].length; j++) {

                //add to the string the current token
                txtData.append(array[i][j]);


                //if the token is not the last
                if (j != array[i].length - 1)
                    //add a colon
                    txtData.append(": ");

            }

            //add a carriage return
            txtData.append("\n");
        }

        try {
            //gets the external dir
            File sdcard = Environment.getExternalStorageDirectory();
            //creates a empty file with 'fileName'
            File file = new File(sdcard, fileName);

            //creates the file
            file.createNewFile();
            //creates an output stream for the file
            FileOutputStream fOut = new FileOutputStream(file);
            //creates an output stream writer for the file output
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            //appends the stringBuilder as a string
            myOutWriter.append(txtData.toString());
            //closes the writer
            myOutWriter.close();
            //closes the stream
            fOut.close();

        } catch (Exception e) {
            fault(e.getMessage());
        }
    }

    /**
     * Throws a fault message
     * @param why
     */

    public void fault(String why) {
        console.Status("SYNTAX ERROR :" + why);
        stop();
    }
}
