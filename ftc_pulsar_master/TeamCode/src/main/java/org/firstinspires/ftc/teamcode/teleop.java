package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp", group = "13330 Pulsar")
public class teleop extends LinearOpMode {

    // version name for organization.
    public String versionName = "v0.06";

    //this.instances of hardware and robot
    private Hardware hardware;
    private Robot robot;

    @Override
    public void runOpMode() {

        //makes an instance of hardware with this LinearOpMode as its context
        this.hardware = new Hardware(this);
        //makes in instance or robot with this hardware /\ as context
        this.robot = new Robot(this.hardware);

        telemetry.addData("Action:", "waiting for IMU initialization");
        telemetry.update();


        //wait for the IMU to be inited
        while (!hardware.imu.isGyroCalibrated()) {
            idle();
        }

        //prints out various statistics to help debugging
        telemetry.addData("Action:", "waiting for start");
        telemetry.addData("IMU Status: ", hardware.imu.getCalibrationStatus().toString());
        telemetry.addData("TeleOp Version: ", versionName);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.setPowerLeft(gamepad1.left_stick_y);
            robot.setPowerRight(gamepad1.right_stick_y);
        }
    }
}
