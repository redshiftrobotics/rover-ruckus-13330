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
        robot.setPowerLeft(gamepad1.left_stick_y);

        robot.setPowerRight(gamepad1.right_stick_y);
    }
}
