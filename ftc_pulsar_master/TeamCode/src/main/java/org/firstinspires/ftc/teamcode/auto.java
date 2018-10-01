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
        this.console = new Console(this.hardware, this.robot, this);


        telemetry.addData("Action:", "waiting for IMU initialization");
        telemetry.update();


        //wait for the IMU to be inited
        while (!hardware.imu.isGyroCalibrated()) {
            idle();
        }

        //makes sure the color sensor is enabled
        hardware.color_sensor_1.enableLed(true);

        //prints out various statistics to debug
        console.displayStatistics(version, "Autonomous");

        //waits until we press play
        waitForStart();

        //loops movement
        while(opModeIsActive()) {
            //drives 1 foot forward at full speed
            robot.encoderDrive(1, 12);
            console.Log("Action", "Drove forward one foot");

            //rotates 180 degrees
            robot.rotate(180, 1, 0.2);
            console.Log("Action", "Rotated 180 degrees");

            //drives using encoders with PID
            robot.encoderDrivePID(1, 12, 0.25);

            //rotates back to normal
            robot.rotate(180, 1, 0.2);
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
