package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "teleop")
public class teleop extends OpMode {
    public void init() {

    }

    public void loop() {
        //Control the left drive with gamepad 1 left stick y

        Robot.Drive.LeftDrive.setPower(gamepad1.left_stick_y);

        //Control the left drive with gamepad 1 right stick y

        Robot.Drive.RightDrive.setPower(gamepad1.right_stick_y);
    }
}