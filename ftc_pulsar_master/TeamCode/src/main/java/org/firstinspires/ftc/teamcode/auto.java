package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "Auto")
public class auto extends OpMode { // comments written by Mr. Luca Sandoval


    public String versionName = "BETA v0.01";

    public void init() {

        telemetry.addData("Initialized Auto Version: ", versionName);
    }


    @Override
    public void loop() {

        telemetry.addData("Auto Completed.", "");
        telemetry.update();

    }

}