package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "teleop")
public class teleop extends OpMode {

    private Hardware hardware;
    private Robot robot;

    @Override
    public void init(){
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware);
    }

    @Override
    public void loop() {
        hardware.front_right_motor.setPower(gamepad1.right_stick_y * hardware.motorInverse[0]);
        hardware.back_right_motor.setPower(gamepad1.right_stick_y * hardware.motorInverse[1]);

        hardware.back_left_motor.setPower(gamepad1.left_stick_y * hardware.motorInverse[2]);
        hardware.front_left_motor.setPower(gamepad1.left_stick_y * hardware.motorInverse[3]);
    }
}
