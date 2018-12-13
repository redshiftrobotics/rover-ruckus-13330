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
    public double scale = 1;
    public boolean pushed = true;

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

            if(gamepad1.dpad_up)
                scale = 100;
            else if (gamepad1.dpad_down)
                scale = 0.1;
            else if (gamepad1.dpad_left)
                scale = 10;
            else if(gamepad1.dpad_right)
                scale = 1;

            timeThreashold += (gamepad1.left_stick_y * 0.0005 * scale);
            powerDegrees += (gamepad1.right_stick_y * 0.0005 * scale);

            if(gamepad1.start)
                powerDegrees = 0.2;

            telemetry.addData("time/threashold", timeThreashold);
            telemetry.addData("power/degrees", powerDegrees);
            telemetry.addData("scale", scale);
            telemetry.update();

            if(gamepad1.a){
                robot.drive(powerDegrees, timeThreashold);
            }

            if(gamepad1.b){
                robot.rotate((int) powerDegrees, 0.7, timeThreashold);
            }

            if(gamepad1.x)
                robot.drive(-powerDegrees, timeThreashold);

            if(gamepad1.y)
                robot.rotate((int) -powerDegrees, 0.7, timeThreashold);

            idle();
        }
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 4) / 4.0;
    }
}
