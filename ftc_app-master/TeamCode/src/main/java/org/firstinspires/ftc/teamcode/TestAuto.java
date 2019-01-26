package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * A class for recording Auto paths as text files which can be run later.
 */

@TeleOp(name = "TestAuto", group = "13330 Pulsar")
public class TestAuto extends LinearOpMode {
    MecanumChassis mecanumChassis;
    AutoMaker autoMaker;
    Imu imu;
    Console console;
    Hardware hardware;

    double power = 0.6;
    @Override
    public void runOpMode() {

        // initialize other classes needed
        hardware = new Hardware(this);
        console = new Console(this);
        imu = new Imu(this);
        autoMaker = new AutoMaker(this, hardware, console, imu);

        while (!imu.imu.isGyroCalibrated()) {
            console.Status("Calibrating Gyro");
            idle();
        }

        mecanumChassis = new MecanumChassis(this, DcMotor.ZeroPowerBehavior.BRAKE, imu);

        waitForStart();

        imu.resetAngle();

        mecanumChassis.rotate(45, 0.1, 1);

        drive(-0.45 * power, 0.43 * power, 0, 1900);
        drive(0.45 * power, -0.45 * power, 0, 1000);
        drive(-0.45 * power, -0.15 * power, 0, 2800);
        drive(-0.45 * power, 0 * power, 0, 1000);
        drive(0.45 * power, 0 * power, 0, 800);
        drive(0 * power, -1 * power, 0, 1000);
        drive(0 * power, 0.3 * power, 0, 3000);
        drive(-0.45 * power, 0 * power, 0, 1000);
        drive(0 * power, 0.3 * power, 0, 1000);


    }


    void drive(double x, double y, double rotate, long time){
        mecanumChassis.driveS(x, y, rotate);
        sleep(time);
        mecanumChassis.stop();
    }
}

