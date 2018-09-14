package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "Auto")
public class auto extends OpMode { // comments written by Mr. Luca Sandoval

    public String versionName = "BETA v0.01";

    private Hardware hardware;
    private Robot robot;

    @Override
    public void init(){
        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware);

        telemetry.addData("Initialized Auto Version: ", versionName);
    }


    @Override
    public void loop() {
        robot.setPowerLeft(1);

        telemetry.addData("Auto Completed.", "");
        telemetry.update();

    }

}