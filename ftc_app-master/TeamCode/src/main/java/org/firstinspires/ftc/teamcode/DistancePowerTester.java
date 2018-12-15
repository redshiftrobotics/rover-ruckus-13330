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
    private Input input;


    @Override
    public void runOpMode() throws InterruptedException {
        long last_time = System.nanoTime();

        this.hardware = new Hardware(this);
        this.robot = new Robot(this.hardware, this);
        this.input = new Input(this);

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

            double[] answers = input.form(new String[]{"Time/Threshold", "Power/Degrees"});

            telemetry.addData("running command.", "");
            timeThreashold += answers[0];
            powerDegrees += answers[1];

            if(gamepad1.right_bumper)
                powerDegrees = 0.2;
            if(gamepad1.left_bumper)
                powerDegrees = -0.2;

            if(input.question(new String[]{"Transform", "Rotate"}) == "Transform"){
                robot.drive(powerDegrees, timeThreashold);
                if(input.question(new String[]{"Undo", "No"}) == "Undo")
                    robot.drive(-powerDegrees, timeThreashold);
            } else {
                robot.rotate((int) powerDegrees, 0.7, timeThreashold);
                if(input.question(new String[]{"Undo", "No"}) == "Undo")
                    robot.rotate((int) -powerDegrees, 0.7, timeThreashold);
            }


            if(gamepad1.x)

            if(gamepad1.y)

            sleep(100);
            idle();
        }
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 4) / 4.0;
    }
}
