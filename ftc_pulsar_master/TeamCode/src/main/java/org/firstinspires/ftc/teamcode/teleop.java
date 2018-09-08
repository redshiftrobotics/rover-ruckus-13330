package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "teleop")
abstract class teleop extends OpMode {


    public void loop() {
        drive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_y);
    }

    public void drive(float front_left_power, float back_left_power, float front_right_power, float back_right_power) {
        hardware.front_left_motor.setPower(front_left_power);
        hardware.back_left_motor.setPower(back_left_power);
        hardware.front_right_motor.setPower(front_right_power);
        hardware.back_right_motor.setPower(back_right_power);
    }
}
