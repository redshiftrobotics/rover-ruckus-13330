package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "teleop")
public class teleop extends OpMode {

    public  DcMotor front_right_motor;
    public  DcMotor front_left_motor;
    public  DcMotor back_right_motor;
    public  DcMotor back_left_motor;

    public void init(){
        front_left_motor = hardwareMap.dcMotor.get("front_left_motor");
        front_right_motor = hardwareMap.dcMotor.get("front_right_motor");
        back_left_motor = hardwareMap.dcMotor.get("back_left_motor");
        back_right_motor = hardwareMap.dcMotor.get("back_right_motor");
    }

    public void loop() {
        back_right_motor.setPower(gamepad1.right_stick_y);
        front_right_motor.setPower(gamepad1.right_stick_y);

        back_left_motor.setPower(gamepad1.left_stick_y);
        front_left_motor.setPower(gamepad1.left_stick_y);


    }
}
