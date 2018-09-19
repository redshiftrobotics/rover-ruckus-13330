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
    private Text text;

    @Override
    public void runOpMode() {

        //makes an instance of hardware with this LinearOpMode as its context
        this.hardware = new Hardware(this);
        //makes in instance or robot with this hardware /\ as context
        this.robot = new Robot(this.hardware);
        //makes an instance of Text with Hardware and Robot as its context
        this.text = new Text(this.hardware, this.robot);


        telemetry.addData("Action:", "waiting for IMU initialization");
        telemetry.update();


        //wait for the IMU to be inited
        while (!hardware.imu.isGyroCalibrated()) {
            idle();
        }

        //prints out various statistics to help debugging
        text.displayStatistics(this);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.setPowerLeft(gamepad1.left_stick_y);
            robot.setPowerRight(gamepad1.right_stick_y);
        }
    }
}
