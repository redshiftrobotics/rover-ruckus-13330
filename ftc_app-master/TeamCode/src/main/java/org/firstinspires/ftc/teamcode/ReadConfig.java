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

    public Method g1A;
    public Object[] g1AParams = {};
    public Method g1B;
    public Object[] g1BParams = {};
    public Method g1X;
    public Object[] g1XParams = {};
    public Method g1Y;
    public Object[] g1YParams = {};

    public Method g1dpad_down;
    public Object[] g1dpad_downParams = {};
    public Method g1dpad_up;
    public Object[] g1dpad_upParams = {};
    public Method g1dpad_right;
    public Object[] g1dpad_rightParams = {};
    public Method g1dpad_left;
    public Object[] g1dpad_leftParams = {};

    public Method g1left_bumper;
    public Object[] g1left_bumperParams = {};
    public Method g1right_bumper;
    public Object[] g1right_bumperParams = {};

    public Method g1left_trigger;
    public Object[] g1left_triggerParams = {};
    public Method g1right_trigger;
    public Object[] g1right_triggerParams = {};

    public Method g1left_stick_y;
    public Object[] g1left_stick_yParams = {};
    public Method g1left_stick_x;
    public Object[] g1left_stick_xParams = {};
    public Method g1right_stick_y;
    public Object[] g1right_stick_yParams = {};
    public Method g1right_stick_x;
    public Object[] g1right_stick_xParams = {};

    public Method g2A;
    public Object[] g2AParams = {};
    public Method g2B;
    public Object[] g2BParams = {};
    public Method g2X;
    public Object[] g2XParams = {};
    public Method g2Y;
    public Object[] g2YParams = {};

    public Method g2dpad_down;
    public Object[] g2dpad_downParams = {};
    public Method g2dpad_up;
    public Object[] g2dpad_upParams = {};
    public Method g2dpad_right;
    public Object[] g2dpad_rightParams = {};
    public Method g2dpad_left;
    public Object[] g2dpad_leftParams = {};

    public Method g2left_bumper;
    public Object[] g2left_bumperParams = {};
    public Method g2right_bumper;
    public Object[] g2right_bumperParams = {};

    public Method g2left_trigger;
    public Object[] g2left_triggerParams = {};
    public Method g2right_trigger;
    public Object[] g2right_triggerParams = {};

    public Method g2left_stick_y;
    public Object[] g2left_stick_yParams = {};
    public Method g2left_stick_x;
    public Object[] g2left_stick_xParams = {};
    public Method g2right_stick_y;
    public Object[] g2right_stick_yParams = {};
    public Method g2right_stick_x;
    public Object[] g2right_stick_xParams = {};


    public void readFile(String fileName) {

        BufferedReader br;
        Gamepad gamepad = context.gamepad1;

        Class[] doubleArgs = new Class[1];
        doubleArgs[0] = double.class;


        String[] validTokens = {
                "rightDrivePower",
                "leftDrivePower",
                "speedToggle",
                "test",
                "null",
        };


        AssetManager am = context.hardwareMap.appContext.getAssets();

        try {

            br = new BufferedReader(
                    new InputStreamReader(am.open(fileName)));

            // do reading, usually loop until end of file reading


            console.Log(br.toString(), " ye");
            // each line just look like “a : b : c …”
            // where a=“gamepad1” or “gamepad2”
            // where b=controller name such as “leftJoy”
            // where c=the exact name of the method to call to do the operation
            // where d, e, f, etc. could be parameters to be sent to the method.(note I didn’t code that below yet)
            String x;
            while ((x = br.readLine()) != null) {
                // process each line;
                String[] tokens = x.split(":"); //split it up via : separators

                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replace("\t", ""); //remove all tabs
                    tokens[i] = tokens[i].replace(" ", ""); //remove all spaces

                }

                if (Arrays.asList(validTokens).contains(tokens[1])) {
                    try {
                        switch (tokens[0]) {
                            case "gamepad1.a":
                                if(tokens[1] == "null")
                                    g1A = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1A = cm.getClass().getDeclaredMethod(tokens[1]);
                                console.Log("Inited ", tokens[0] + ":" + tokens[1]);
                                //console.Update();

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1AParams[0] = tokens[2];
                                    g1AParams[1] = tokens[3];
                                }

                            case "gamepad1.b":
                                if(tokens[1] == "null")
                                    g1B = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1B = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1BParams[0] = tokens[3];
                                    g1BParams[1] = tokens[4];
                                }

                            case "gamepad1.x":
                                if(tokens[1] == "null")
                                    g1X = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1X = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1XParams[0] = tokens[3];
                                    g1XParams[1] = tokens[4];
                                }

                            case "gamepad1.y":
                                if(tokens[1] == "null")
                                    g1Y = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1Y = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1YParams[0] = tokens[3];
                                    g1YParams[1] = tokens[4];
                                }


                            case "gamepad1.right_bumper":
                                if(tokens[1] == "null")
                                    g1right_bumper = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1right_bumper = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1right_bumperParams[0] = tokens[3];
                                    g1right_bumperParams[1] = tokens[4];
                                }

                            case "gamepad1.left_bumper":
                                if(tokens[1] == "null")
                                    g1left_bumper = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1left_bumper = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1left_bumperParams[0] = tokens[3];
                                    g1left_bumperParams[1] = tokens[4];
                                }

                            case "gamepad1.right_trigger":
                                if(tokens[1] == "null")
                                    g1right_trigger = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1right_trigger = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1right_triggerParams[0] = tokens[3];
                                    g1right_triggerParams[1] = tokens[4];
                                }

                            case "gamepad1.left_trigger":
                                if(tokens[1] == "null")
                                    g1left_trigger = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1left_trigger = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1left_triggerParams[0] = tokens[3];
                                    g1left_triggerParams[1] = tokens[4];
                                }

                            case "gamepad1.right_stick_y":
                                if(tokens[1] == "null")
                                    g1right_stick_y = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1right_stick_y = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1right_stick_yParams[0] = tokens[3];
                                    g1right_stick_yParams[1] = tokens[4];
                                }

                            case "gamepad1.right_stick_x":
                                if(tokens[1] == "null")
                                    g1right_stick_x = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1right_stick_x = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1right_stick_xParams[0] = tokens[3];
                                    g1right_stick_xParams[1] = tokens[4];
                                }

                            case "gamepad1.left_stick_y":
                                if(tokens[1] == "null")
                                    g1left_stick_y = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1left_stick_y = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1left_stick_yParams[0] = tokens[3];
                                    g1left_stick_yParams[1] = tokens[4];
                                }

                            case "gamepad1.left_stick_x":
                                if(tokens[1] == "null")
                                    g1left_stick_x = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1left_stick_x = cm.getClass().getDeclaredMethod(tokens[1], doubleArgs);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1left_stick_xParams[0] = tokens[3];
                                    g1left_stick_xParams[1] = tokens[4];
                                }

                            case "gamepad1.dpad_right":
                                if(tokens[1] == "null")
                                    g1dpad_right = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1dpad_right = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1dpad_rightParams[0] = tokens[3];
                                    g1dpad_rightParams[1] = tokens[4];
                                }

                            case "gamepad1.dpad_left":
                                if(tokens[1] == "null")
                                    g1dpad_left = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1dpad_left = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1dpad_leftParams[0] = tokens[3];
                                    g1dpad_leftParams[1] = tokens[4];
                                }

                            case "gamepad1.dpad_up":
                                if(tokens[1] == "null")
                                    g1dpad_up = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1dpad_up = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1dpad_upParams[0] = tokens[3];
                                    g1dpad_upParams[1] = tokens[4];
                                }

                            case "gamepad1.dpad_down":
                                if(tokens[1] == "null")
                                    g1dpad_down = cm.getClass().getDeclaredMethod("notAssigned");
                                else
                                g1dpad_down = cm.getClass().getDeclaredMethod(tokens[1]);

                                if (tokens.length < 3) {
                                    break;
                                } else {
                                    g1dpad_downParams[0] = tokens[3];   
                                    g1dpad_downParams[1] = tokens[4];
                                }

                            default:
                                console.Log("config error: switch or button undefined " + tokens[0], "");
                                console.Update();
                                context.stop();
                        }
                    } catch (NoSuchMethodException e) {
                        console.Log("Fatal Error", e);
                        console.Log("Stack Trace", e.getStackTrace()[0]);
                        console.Update();
                        e.printStackTrace();
                        context.sleep(100000);
                        context.stop();
                    }

                } else {
                    console.Log("config error: syntax " + tokens[2], "");
                    console.Update();
                    context.sleep(100000);
                    context.stop();
                }
            }


        } catch (IOException e) {
            console.Log("Fatal Error", e);
            console.Update();
            e.printStackTrace();
            context.sleep(100000);
            context.stop();
        }


        console.Log("Finished Initilization of " + fileName + " config.", "");
        //console.Update();
    }

    public void updateControls() {
        try {
            if (context.gamepad1.a)
                g1A.invoke(cm, g1AParams);

            if (context.gamepad1.b)
                g1B.invoke(cm, g1BParams);

            if (context.gamepad1.x)
                g1X.invoke(cm, g1XParams);

            if (context.gamepad1.y)
                g1Y.invoke(cm, g1YParams);

            if (context.gamepad1.right_bumper)
                g1right_bumper.invoke(cm, g1right_bumperParams);

            if (context.gamepad1.left_bumper)
                g1left_bumper.invoke(cm, g1left_bumperParams);

            if(context.gamepad1.dpad_right)
                g1dpad_right.invoke(cm, g1dpad_rightParams);

            if(context.gamepad1.dpad_left)
                g1dpad_left.invoke(cm, g1dpad_leftParams);

            if(context.gamepad1.dpad_down)
                g1dpad_down.invoke(cm, g1dpad_downParams);


            if(context.gamepad1.dpad_up)
                g1dpad_up.invoke(cm, g1dpad_upParams);

            g1right_trigger.invoke(cm, context.gamepad1.right_trigger);
            g1left_trigger.invoke(cm, context.gamepad1.left_trigger);

            g1right_stick_y.invoke(cm, context.gamepad1.right_stick_y);
            g1right_stick_x.invoke(cm, context.gamepad1.right_stick_x);
            g1left_stick_y.invoke(cm, context.gamepad1.left_stick_y);
            g1left_stick_x.invoke(cm, context.gamepad1.left_stick_x);


        } catch (IllegalAccessException | InvocationTargetException e) {
            console.Log("Fatal Error", e);
            console.Update();
            e.printStackTrace();
            context.sleep(100000);
            context.stop();
        }
    }
}
