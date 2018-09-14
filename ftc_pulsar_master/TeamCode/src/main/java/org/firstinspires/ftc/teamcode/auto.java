package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;



@Autonomous(name = "Auto")
public class auto extends LinearOpMode { // comments written by Mr. Luca Sandoval

    public String versionName = "BETA v0.01"; // version name for organization.

    private Hardware hardware;
    private Robot robot;

    @Override
    public void runOpMode(){ // the method that runs everything.
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware);

        telemetry.addData("Initialized Auto Version: ", versionName);

        waitForStart();


        robot.setPowerLeft(1); // goes forward for one second.
        robot.setPowerRight(1);
        sleep(1000);
        robot.setPowerLeft(1); //goes right for one second.
        robot.setPowerRight(0.5);
        sleep(1000);


        telemetry.addData("Auto Completed.", "");
        telemetry.update();

    }

}