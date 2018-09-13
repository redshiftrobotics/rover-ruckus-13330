package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "teleop")
public class teleop extends OpMode {

    private Hardware hardware;

    @Override
    public void init(){
        this.hardware = new Hardware(this);
    }

    @Override
    public void loop() {
        hardware.front_right_motor.setPower(gamepad1.right_stick_y);
        hardware.back_right_motor.setPower(gamepad1.right_stick_y);

        hardware.back_left_motor.setPower(gamepad1.left_stick_y);
        hardware.front_left_motor.setPower(gamepad1.left_stick_y);
    }
}
