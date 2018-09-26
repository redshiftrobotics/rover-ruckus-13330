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

        context.telemetry.addData("Color Sensor Staus: ", "");
        context.telemetry.addData(" > RED", hardware.color_sensor_1.red());
        context.telemetry.addData(" > GREEN", hardware.color_sensor_1.green());
        context.telemetry.addData(" > BLUE", hardware.color_sensor_1.blue());

        context.telemetry.addLine();
        context.telemetry.addData("Color Difference:", (hardware.color_sensor_1.red() + hardware.color_sensor_1.green())/2 - hardware.color_sensor_1.blue());
        context.telemetry.addData("Color Average:", (hardware.color_sensor_1.red() + hardware.color_sensor_1.green() + hardware.color_sensor_1.blue()/3));

        context.telemetry.update();

    }

    public void displayStatistics(LinearOpMode context, double versionName, String type){ // used to display the statistics.

        context.telemetry.addData("Action:", "waiting for start");
        context.telemetry.addData("IMU Status: ", hardware.imu.getCalibrationStatus().toString());
        context.telemetry.addData(type + " Version: ", "v" + versionName);

        context.telemetry.addData("Color Sensor Staus: ", "");
        context.telemetry.addData("     RGB:", hardware.color_sensor_1.argb());
        context.telemetry.update();

    }

}
