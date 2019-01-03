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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * A class for getting input from the user
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

            currentQuestion -= getStickNext(context);
            currentQuestion %= questions.length;

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

    public boolean yesOrNo(String question) {
        boolean answer = false;
        int current = 0;

        context.telemetry.addData(question, "");

        while (!context.gamepad1.start) {

            current -= getStickNext(context);

            if (current % 2 == 0) {
                context.telemetry.addData("-[*]", "Yes");
                context.telemetry.addData("-[ ]", "No");
                answer = true;
            } else {
                context.telemetry.addData("-[ ]", "Yes");
                context.telemetry.addData("-[*]", "No");
                answer = false;
            }

            context.telemetry.update();
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

    public int getStickNext(LinearOpMode context){
        if(context.gamepad1.left_stick_y > 0.5){
            return 1;
        } else if(context.gamepad1.left_stick_y < -0.5){
            return -1;
        } else
            return 0;
    }

}
