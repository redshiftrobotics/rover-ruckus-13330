package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "Autonomous", group = "13330 Pulsar")
public class auto extends LinearOpMode {

    public String versionName = "v0.04"; // version name for organization.

    private Hardware hardware;
    private Robot robot;

    private String position = "BLUE_RIGHT";


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
            sleep(10);
            idle();
        }

        //prints out various statistics to help debugging
        telemetry.addData("Action:", "waiting for start");
        telemetry.addData("IMU Status: ", hardware.imu.getCalibrationStatus().toString());
        telemetry.addData("Autonomous Version: ", versionName);
        telemetry.update();


        waitForStart();

        while (opModeIsActive()) {

            hardware.correction = robot.checkDirection();

            telemetry.addData("Current Angle:", robot.getAngle());
            telemetry.addData("Forward Angle:", hardware.oldAngle.firstAngle);
            telemetry.addData("Correction Angle:", hardware.correction);
            telemetry.addData("Current Global Angle:", hardware.globalAngle);
            telemetry.update();


            //rotate -90 degrees
            robot.rotate(-90, 1);

            //rotate 180 degrees
            robot.rotate(180, 1);

            //go forward
            robot.drive(0.3, 1000);

            //turn all the way around
            robot.rotate(180, 1);

            //go back to starting position
            robot.drive(0.3, 1000);

        }


        telemetry.addData("Auto Completed.", "");
        telemetry.update();

    }
}















/*
switch (position) {
    case "BLUE_RIGHT":

        break;
    case "BLUE_LEFT":

        break;
    case "RED_RIGHT":

        break;
    case "RED_LEFT":

        break;
    default:

        break;
}
*/
