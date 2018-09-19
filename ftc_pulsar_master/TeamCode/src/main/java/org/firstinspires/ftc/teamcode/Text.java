package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Text { // parent class


    private Hardware hardware; // get the ctx of other classes to use
    private Robot robot;

    public Text(Hardware hardware, Robot robot) { // creates the ctx in this class
        this.hardware = hardware;
        this.robot = robot;
    }

    public void displayAngles(LinearOpMode ctx){ // used to display the current angles.

        ctx.telemetry.addData("Current Angle:", robot.getAngle());
        ctx.telemetry.addData("Forward Angle:", hardware.oldAngle.firstAngle);
        ctx.telemetry.addData("Correction Angle:", hardware.correction);
        ctx.telemetry.addData("Current Global Angle:", hardware.globalAngle);

    }

    public void displayStatistics(LinearOpMode ctx){ // used to display the statistics.

        ctx.telemetry.addData("Action:", "waiting for start");
        ctx.telemetry.addData("IMU Status: ", hardware.imu.getCalibrationStatus().toString());
        ctx.telemetry.addData("Autonomous Version: ", auto.versionName);

    }

}
