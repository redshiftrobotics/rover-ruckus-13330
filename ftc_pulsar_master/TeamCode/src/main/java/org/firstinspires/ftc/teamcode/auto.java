package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name = "Autonomous", group = "13330 Pulsar")
public class auto extends LinearOpMode {

    public static String versionName = "v0.04"; // version name for organization.

    //this.instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Text text;

    private String position = "BLUE_RIGHT";


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

            hardware.correction = robot.checkDirection();

            text.displayAngles(this);
            telemetry.update();


            //rotate 90 deg right
            robot.rotate(-90, 1);

            //rotate 30 deg left
            robot.rotate(390, 1);

            //go forward
            robot.drive(0.3, 1000);

            //turn all the way around
            robot.rotate(180, 1);

            //go back to starting position
            robot.drive(0.3, 1000);

            //turn all the way around
            robot.rotate(180-30, 1);

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
