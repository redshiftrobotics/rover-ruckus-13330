package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by julian on 12/14/18.
 */

public class Input {
    private Hardware hardware;
    private Robot robot;
    private LinearOpMode context;

    public Input(Hardware hardware, Robot robot, LinearOpMode context) { // creates the context in this class
        this.hardware = hardware;
        this.robot = robot;
        this.context = context;
    }

    public Input(LinearOpMode context){
        this.context = context;
    }

    public double[] form(String[] questions){
        double[] answers = new double[questions.length];
        int currentQuestion = 0;
        double scale = 0;

        while(!context.gamepad1.start) {

            scale = getScale(context);

            currentQuestion += getNext(context);

            for (int i = 0; i < questions.length; i++) {
                if(i == currentQuestion){
                    context.telemetry.addData("-[*] " + questions[i], "[" + answers[i] + "]");
                    answers[i] -= (scale * Math.round(context.gamepad1.left_stick_y));
                } else {
                    context.telemetry.addData("-[ ] " + questions[i], "[" + answers[i] + "]");
                }

                context.telemetry.update();
            }

            context.sleep(50);
            context.idle();
        }

        return answers;
    }

    public String question(String[] questions){
        String answer = null;
        int currentQuestion = 0;

        while(!context.gamepad1.start) {

            currentQuestion += getNext(context);

            for (int i = 0; i < questions.length; i++) {
                if(i == currentQuestion){
                    context.telemetry.addData("-[*] " + questions[i], "");
                    if(context.gamepad1.a) {
                        answer = questions[i];
                    }

                } else {
                    context.telemetry.addData("-[ ] " + questions[i], "");
                }

                context.telemetry.update();
            }

            context.sleep(50);
            context.idle();
        }

        return answer;
    }

    public double getScale(LinearOpMode context){
        if(context.gamepad1.dpad_up)
            return 100;
        else if (context.gamepad1.dpad_down)
            return 0.1;
        else if (context.gamepad1.dpad_left)
            return 10;
        else if(context.gamepad1.dpad_right)
            return 1;
        else
            return 0;
    }

    public int getNext(LinearOpMode context){
        if(context.gamepad1.right_bumper){
            return 1;
        } else if(context.gamepad1.left_bumper){
            return -1;
        } else
            return 0;
    }
}
