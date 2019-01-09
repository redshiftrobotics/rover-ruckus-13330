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

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A class that can help with coding an auto. Saves autos to files and can read them. Working on updating recording autos...
 */

@TeleOp(name = "Automaker")
public class AutoMaker {

    private Input input;
    private Console console;
    private LinearOpMode context;

    private MecanumChassis mecanumChassis;
    private MineralPosition mineralPosition;
    private MineralDetection mineralDetection;

    private String craterFileName = "FOO";
    private String depoFileName = "FOO";
    private double scale = 10000;

    private DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    private Hardware hardware;

    public AutoMaker(LinearOpMode context, Hardware hardware) {
        this.context = context;
        this.mecanumChassis = new MecanumChassis(context, zeroPowerBehavior);
        this.hardware = hardware;
    }

    /**
     * This reads a text file and converts it to an array.
     *
     * @param fileName The file name to read
     * @return The built array
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
            BufferedReader br = new BufferedReader(new FileReader(file));

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
     * @param i     The starting line of the array
     */

    private void runArray(Object[][] array, int i) {
        //the current depth of scanning (for case statements)
        int scanDepth = 0;

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
                    DcMotor motor = context.hardwareMap.dcMotor.get(array[i][1].toString());

                    motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //sets the mode to run to position
                    motor.setTargetPosition(Integer.parseInt(array[i][2].toString())); //sets the target position
                    motor.setPower(1); //sets the power

                    //waits for end
                    while (motor.isBusy() && context.opModeIsActive()) {
                        context.sleep(10);
                    }

                    //switches the mode back to using power
                    motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    break;
                //drive
                case "D":
                    //because builders are stupid
                    if (array[i].length > 4)
                        fault("drive needs to have four tokens");

                    //drives with angle, rotation, speed from array
                    mecanumChassis.driveS(Double.parseDouble(array[i][1].toString()), Double.parseDouble(array[i][2].toString()), Double.parseDouble(array[i][3].toString()));

                    //if token 4 is LIM run limit switch wait loop
                    if ((array[i][4].toString()).equals("LIM")) {

                        //integral for limit switch scanning
                        while(!context.hardwareMap.get(TouchSensor.class, array[i][5].toString()).isPressed()){
                            context.sleep(10);
                        }
                    }

                    //if its not using limit switch, then just wait until the time is up
                    else {
                        context.sleep(Long.parseLong(array[i][4].toString()));
                    }
                    break;
                //servo control
                case "L":
                    //because builders are stupid
                    if (array[i].length > 2)
                        fault("servo control needs to have 2 tokens");

                    //finds servo from array name
                    Servo servo = context.hardwareMap.servo.get(array[i][1].toString());

                    //sets the position to the array position
                    servo.setPosition(Double.parseDouble(array[i][2].toString()) / 180);

                    //waits for it to go
                    while (servo.getPosition() != Double.parseDouble(array[i][2].toString()) / 180 && context.opModeIsActive()) {
                        context.sleep(10);
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
                        int mineralDetectionSplits = 3;
                        mineralPosition = mineralDetection.getPosition(mineralDetection.getImage(), mineralDetectionSplits);
                    } catch (Exception e) {
                    }
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
     * Records and saves an array
     *
     * @param array The array that you want to record to
     * @param depo If you are recording crater or depo (for saving)
     */

    public void record(Object[][] array, boolean depo) {

        //the current step to program in auto
        int step = 0;

        //the last angle of g1 left stick
        double lastAngle = 90;

        //while you have not pushed start
        //psst. start actually stops it
        while (context.opModeIsActive() && !context.gamepad1.start) {

            //cycle through steps
            if(context.gamepad1.right_bumper)
                step++;
            if(context.gamepad1.left_bumper)
                step--;

            //horizontal axis sqr
            double xSqr = Math.pow(context.gamepad1.left_stick_x, 2);
            //vertical axis sqr
            double ySqr = Math.pow(context.gamepad1.left_stick_y, 2);

            //magnitude deprived from a^2 + b^2 = c^2
            double magnitude = Math.sqrt(xSqr + ySqr);
            //angle is just the arc tan of y and x
            double angle = Math.atan2(context.gamepad1.left_stick_y, context.gamepad1.left_stick_x);

            //avoid 0 angle bullshit
            if(magnitude > 0.1)
                lastAngle = angle;

            //only for driving
            array[step][0] = "D";
            //angle
            array[step][1] = lastAngle;
            //rotation
            array[step][2] = (context.gamepad1.left_stick_y / (scale * 100)) % 1;
            //speed
            array[step][3] = (context.gamepad2.left_stick_y / (scale * 100)) % 1;
            //time
            array[step][4] = roundTo(100, (context.gamepad2.right_stick_y / (scale / 10)));

            //logging it so people know what is going on
            context.telemetry.addData("Step", step);
            context.telemetry.addData("Angle", array[step][1]);
            context.telemetry.addData("Speed", array[step][3]);
            context.telemetry.addData("Rotation", array[step][2]);
            context.telemetry.addData("Time", array[step][4]);

            //run step
            if (context.gamepad1.a) {
                mecanumChassis.driveS((long) array[step][1], (double) array[step][2], (double) array[step][3]);
                context.sleep((long) array[step][4]);
                mecanumChassis.stop();
            }

            //run step in reverse
            else if (context.gamepad1.b) {
                mecanumChassis.driveS((long) array[step][1] - 180, (double) array[step][2], -(double) array[step][3]);
                context.sleep((long) array[step][4]);
                mecanumChassis.stop();
            }
        }

        //saves it with corresponding file names
        if(depo)
            saveArray(array, depoFileName);
        else
            saveArray(array, craterFileName);
    }

    /**
     * Saves an array as a string in a file
     *
     * @param array    The array to save
     * @param fileName The fileName to save to
     */

    public void saveArray(Object[][] array, String fileName) {

        //creates a new stringBuilder (because I want to use .append).
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
     *
     * @param why The reason for fault
     */

    public void fault(String why) {
        console.Status("ERROR :" + why);
        context.stop();
    }

    /**
     * Rounds to a specific integer. For example if one wanted to round to the nearest hundredth 867 -> 900
     * @param to What to round to
     * @param x The value to round
     * @return
     */

    public int roundTo(int to, double x) {
        return (int) ((x + to - 1) / to) * to;
    }

}
