package org.firstinspires.ftc.teamcode;

import android.content.res.AssetManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by julian on 11/6/18.
 */

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


    public static int gA = 0;
    public static int gB = 1;
    public static int gX = 2;
    public static int gY = 3;
    public static int gright_bumper = 4;
    public static int gleft_bumper = 5;
    public static int gright_trigger = 6;
    public static int gleft_trigger = 7;
    public static int gright_stick_y = 8;
    public static int gleft_stick_y = 9;
    public static int gright_stick_x = 10;
    public static int gleft_stick_x = 11;
    public static int gdpad_right = 12;
    public static int gdpad_left = 13;
    public static int gdpad_up = 14;;
    public static int gdpad_down = 15;


    public Method[] deviceMethods = {
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

    public void readFile(String fileName) {

        BufferedReader br;

        String[] validTokens = {
                "rightDrivePower",
                "leftDrivePower",
                "speedToggle",
                "test",
                "null",
        };

        String[] deviceNames = { //MUST BE IN ORDER with deviceMethods
                "gamepad1.a" ,
                "gamepad1.b" ,
                "gamepad1.x" ,
                "gamepad1.y" ,
                "gamepad1.right_bumper" ,
                "gamepad1.left_bumper" ,
                "gamepad1.right_trigger" ,
                "gamepad1.left_trigger" ,
                "gamepad1.right_stick_y" ,
                "gamepad1.left_stick_y" ,
                "gamepad1.right_stick_x" ,
                "gamepad1.left_stick_x" ,
                "gamepad1.dpad_right" ,
                "gamepad1.dpad_left" ,
                "gamepad1.dpad_up" ,
                "gamepad1.dpad_down" ,
                "gamepad2.a" ,
                "gamepad2.b" ,
                "gamepad2.x" ,
                "gamepad2.y" ,
                "gamepad2.right_bumper" ,
                "gamepad2.left_bumper" ,
                "gamepad2.right_trigger" ,
                "gamepad2.left_trigger" ,
                "gamepad2.right_stick_y" ,
                "gamepad2.left_stick_y" ,
                "gamepad2.right_stick_x" ,
                "gamepad2.left_stick_x" ,
                "gamepad2.dpad_right" ,
                "gamepad2.dpad_left" ,
                "gamepad2.dpad_up" ,
                "gamepad2.dpad_down"
        };


        AssetManager am = context.hardwareMap.appContext.getAssets();

        try {

            br = new BufferedReader(
                    new InputStreamReader(am.open(fileName)));

            console.Log(br.toString(), " ye");
            console.Update();


            String x;

            while ((x = br.readLine()) != null) {

                // process each line and get tokens stripping all random characters away
                String[] tokens = x.split(":"); //split it up via : separators

                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replace("\t", ""); //remove all tabs
                    tokens[i] = tokens[i].replace(" ", ""); //remove all spaces
                }


                // Verify valid first token (device name)
                int devicePosition = Arrays.asList(deviceNames).indexOf(tokens[0]);
                if (devicePosition == -1) {
                    console.Log("config syntax error: unrecognized device: " + tokens[0], "");
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
                        if(tokens.length >= 3) {
                            // if real operation, then find data type and assign operation method address
                            if (tokens[2].equals("boolean")) {
                                deviceMethods[devicePosition] = cm.getClass().getMethod(tokens[1]);
                            }
                            else if (tokens[2].equals( "float")) {
                                deviceMethods[devicePosition] = cm.getClass().getMethod(tokens[1], float.class);
                            } else {
                                console.Log("Test", tokens[0]);
                                console.Log("Test1", tokens[1]);
                                console.Log("Test2", tokens[2]);
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
            if (context.gamepad1.a && deviceMethods[gA]!=null)
                deviceMethods[gA].invoke(cm);

            if (context.gamepad1.b && deviceMethods[gB]!=null)
                deviceMethods[gB].invoke(cm);

            if (context.gamepad1.x && deviceMethods[gX]!=null)
                deviceMethods[gX].invoke(cm);

            if (context.gamepad1.y && deviceMethods[gY]!=null)
                deviceMethods[gY].invoke(cm);

            if (context.gamepad1.right_bumper && deviceMethods[gright_bumper]!=null)
                deviceMethods[gright_bumper].invoke(cm);

            if (context.gamepad1.left_bumper && deviceMethods[gleft_bumper]!=null)
                deviceMethods[gleft_bumper].invoke(cm);

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

            if(context.gamepad1.dpad_right && deviceMethods[gdpad_right]!=null)
                deviceMethods[gdpad_right].invoke(cm);

            if(context.gamepad1.dpad_left && deviceMethods[gdpad_left]!=null)
                deviceMethods[gdpad_left].invoke(cm);

            if(context.gamepad1.dpad_up && deviceMethods[gdpad_up]!=null)
                deviceMethods[gdpad_up].invoke(cm);

            if(context.gamepad1.dpad_down && deviceMethods[gdpad_down]!=null)
                deviceMethods[gdpad_down].invoke(cm);
            
            
            //controller gamepad2
            if (context.gamepad2.a && deviceMethods[gA+16]!=null)
                deviceMethods[gA+16].invoke(cm);

            if (context.gamepad2.b && deviceMethods[gB+16]!=null)
                deviceMethods[gB+16].invoke(cm);

            if (context.gamepad2.x && deviceMethods[gX+16]!=null)
                deviceMethods[gX+16].invoke(cm);

            if (context.gamepad2.y && deviceMethods[gY+16]!=null)
                deviceMethods[gY+16].invoke(cm);

            if (context.gamepad2.right_bumper && deviceMethods[gright_bumper+16]!=null)
                deviceMethods[gright_bumper+16].invoke(cm);

            if (context.gamepad2.left_bumper && deviceMethods[gleft_bumper+16]!=null)
                deviceMethods[gleft_bumper+16].invoke(cm);

            if (deviceMethods[gright_trigger+16] != null)
                deviceMethods[gright_trigger+16].invoke(cm, context.gamepad2.right_trigger);

            if (deviceMethods[gleft_trigger+16] != null)
                deviceMethods[gleft_trigger+16].invoke(cm, context.gamepad2.left_trigger);

            if (deviceMethods[gright_stick_y+16] != null)
                deviceMethods[gright_stick_y+16].invoke(cm, context.gamepad2.right_stick_y);

            if (deviceMethods[gleft_stick_y+16] != null)
                deviceMethods[gleft_stick_y+16].invoke(cm, context.gamepad2.left_stick_y);

            if (deviceMethods[gright_stick_x+16] != null)
                deviceMethods[gright_stick_x+16].invoke(cm, context.gamepad2.right_stick_x);

            if (deviceMethods[gleft_stick_x+16] != null)
                deviceMethods[gleft_stick_x+16].invoke(cm, context.gamepad2.left_stick_x);

            if(context.gamepad2.dpad_right && deviceMethods[gdpad_right+16]!=null)
                deviceMethods[gdpad_right+16].invoke(cm);

            if(context.gamepad2.dpad_left && deviceMethods[gdpad_left+16]!=null)
                deviceMethods[gdpad_left+16].invoke(cm);

            if(context.gamepad2.dpad_up && deviceMethods[gdpad_up+16]!=null)
                deviceMethods[gdpad_up+16].invoke(cm);

            if(context.gamepad2.dpad_down && deviceMethods[gdpad_down+16]!=null)
                deviceMethods[gdpad_down+16].invoke(cm);

        } catch (IllegalAccessException | InvocationTargetException e) { //absolutely should NEVER happen
            console.Log("Fatal Error", e);
            console.Update();
            e.printStackTrace();
            context.sleep(100000);
            context.stop();
        }
    }
}
