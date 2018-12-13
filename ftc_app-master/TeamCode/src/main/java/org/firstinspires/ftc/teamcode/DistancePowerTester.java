package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by julian on 12/12/18.
 */

@TeleOp(name="DistancePowerTester", group = "13330 Pulsar")
public class DistancePowerTester extends LinearOpMode{

    public double timeThreashold = 0;
    public double powerDegrees = 0;

    private Robot robot;
    private Hardware hardware;


    @Override
    public void runOpMode() throws InterruptedException {
        long last_time = System.nanoTime();

        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);

        waitForStart();
        
        while(opModeIsActive()) {

            long timer = System.nanoTime();
            int delta_time = (int) ((timer - last_time) / 1000000);
            last_time = timer;

            timeThreashold += gamepad1.left_stick_y * delta_time;
            powerDegrees += gamepad1.right_stick_y * delta_time;

            telemetry.addData("time/threashold", timeThreashold);
            telemetry.addData("power/degrees", powerDegrees);
            telemetry.update();

            if(gamepad1.a){
                robot.drive(powerDegrees, timeThreashold);
            }

            if(gamepad1.b){
                robot.rotate((int) powerDegrees, 1, timeThreashold);
            }

            idle();
        }
    }
}
