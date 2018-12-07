package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Motor Scan", group = "13330 Pulsar")
public class MotorScan extends LinearOpMode {

    //change
    int NUM_MOTORS = 2;
    String hardwarePrefix = "motor";

    //don't change
    int currentMotor = 0;

    DcMotor[] motors = new DcMotor[NUM_MOTORS];

    @Override
    public void runOpMode(){

        for(int i = 0; i< motors.length; i++) {
            motors[i] = hardwareMap.dcMotor.get(hardwarePrefix + i);
        }

        waitForStart();

        while(opModeIsActive()){

            if(gamepad1.dpad_right)
                currentMotor++;
            else if(gamepad1.dpad_left)
                currentMotor--;

            motors[currentMotor].setPower(gamepad1.left_stick_y);

            telemetry.update();

            sleep(50);
            idle();
        }
    }
}
