package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name = "Autonomous", group = "13330 Pulsar")
public class auto extends LinearOpMode {

    public double version = 0.05; // version name for organization.

    //this.instances of hardware, robot, and text
    private Hardware hardware;
    private Robot robot;
    private Console console;

    private String position = "BLUE_RIGHT";


    @Override
    public void runOpMode() {

        //makes an instance of hardware with this LinearOpMode as its context
        this.hardware = new Hardware(this);
        //makes in instance or robot with this hardware /\ as context
        this.robot = new Robot(this.hardware, this);
        //makes an instance of Text with Hardware and Robot as its context
        this.console = new Console(this.hardware, this.robot);


        telemetry.addData("Action:", "waiting for IMU initialization");
        telemetry.update();


        //wait for the IMU to be inited
        while (!hardware.imu.isGyroCalibrated()) {
            idle();
        }

        hardware.color_sensor_1.enableLed(true); // makes sure the color sensor is enabled.
        //prints out various statistics to help debugging
        console.displayStatistics(this, version, "Autonomous");



        waitForStart();


        hardware.correction = robot.checkDirection();

        console.displayAngles(this);

        /*

        robot.setPowerLeft(-0.1);
        robot.setPowerRight(-0.1);
        //go forward
        while(opModeIsActive() && (hardware.color_sensor_1.red() + hardware.color_sensor_1.green())/2 - hardware.color_sensor_1.blue() <= 5) {}

        robot.setPowerLeft(0);
        robot.setPowerRight(0);
        */


        robot.rotate(180, 0.3);


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
