package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Scan", group = "13330 Pulsar")
public class ServoScan extends LinearOpMode {

    //change
    int NUM_SERVOS = 2;
    String hardwarePrefix = "servo";

    //don't change
    int currentServo = 0;

    Servo[] servos = new Servo[NUM_SERVOS];

    double[] degrees = new double[NUM_SERVOS];
    @Override
    public void runOpMode(){

        for(int i = 0; i< servos.length; i++) {
            servos[i] = hardwareMap.servo.get(hardwarePrefix + i);
        }

        waitForStart();

        while(opModeIsActive()){

            if(gamepad1.dpad_right)
                currentServo++;
            else if(gamepad1.dpad_left)
                currentServo--;

            degrees[currentServo] -= gamepad1.left_stick_y/20;

            for(int i = 0; i< servos.length; i++) {
                servos[i].setPosition(degrees[i]);
            }


            telemetry.update();

            sleep(50);
            idle();
        }
    }
}