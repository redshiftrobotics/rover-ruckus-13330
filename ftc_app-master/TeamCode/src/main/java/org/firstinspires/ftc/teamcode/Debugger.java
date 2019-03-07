package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Debugger")
public class Debugger extends LinearOpMode {

public Hardware hardware;
    @Override
    public void runOpMode() throws InterruptedException {

        hardware = new Hardware(this);
waitForStart();
        while(opModeIsActive()){
            hardware.extenderWheel.setPower(gamepad2.left_stick_y);
            hardware.collectorHinge.setPower(gamepad2.right_stick_y);
            telemetry.addData("Running:", "Running") ;
            telemetry.update();
        }

    }
}
