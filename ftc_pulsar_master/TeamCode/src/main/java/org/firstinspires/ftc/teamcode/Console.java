package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Console { // parent class


    private Hardware hardware; // get the context of other classes to use
    private Robot robot;

    public Console(Hardware hardware, Robot robot) { // creates the context in this class
        this.hardware = hardware;
        this.robot = robot;
    }

    public void displayAngles(LinearOpMode context){ // used to display the current angles.

        context.telemetry.addData("Current Angle:", robot.getAngle());
        context.telemetry.addData("Forward Angle:", hardware.oldAngle.firstAngle);
        context.telemetry.addData("Correction Angle:", hardware.correction);
        context.telemetry.addData("Current Global Angle:", hardware.globalAngle);

        context.telemetry.update();

    }

    public void displayStatistics(LinearOpMode context, double versionName, String type){ // used to display the statistics.

        context.telemetry.addData("Action:", "waiting for start");
        context.telemetry.addData("IMU Status: ", hardware.imu.getCalibrationStatus().toString());
        context.telemetry.addData(type + " Version: ", "v" + versionName);

        context.telemetry.addData("Color Sensor Staus: ", hardware.color_sensor_1.toString()); //TODO: IDK what this will retun so test it!

        context.telemetry.update();

    }

}
