package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

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
    MineralDetection mineralDetection;

    double power = 0.6;
    int sampleCount = 2;

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
        mineralDetection = new MineralDetection(this);

        MineralPosition mineralPosition = MineralPosition.CENTER;




        waitForStart();

        imu.resetAngle();

        try {
            mineralPosition = mineralDetection.getPosition(mineralDetection.getImage(), 2);
        } catch (InterruptedException e) {
            console.Status("Interrupted while taking photo");
            sleep(100);
        }

        //drop down
        hardware.lifter.setPower(1);
        sleep(3600);
        hardware.lifter.setPower(0);

        //sets the rotation curve
        mecanumChassis.setRotate(0.147);

        //initial rotation
        mecanumChassis.rotate(88, 0.2, 1);

        switch (mineralPosition) {

            case CENTER:
                //hits center mineral
                drive(0 , 0.45 , 0, 1700);

                //backs up
                drive(0 , -0.45 , 0, 600);

                //strafes left while rotating
                driveGlobal(0.3, 0, 1, 1000);

            case RIGHT:
                //hits right mineral
                drive(0.45 , 0.45 , 0, 1700);

                //backs up
                drive(-0.45 , -0.45 , 0, 600);

                //moves to position
                drive(-0.45 , 0 , 0, 3400);

            case LEFT:
                //hits right mineral
                drive(-0.45 , 0.45 , 0, 1700);

                //backs up
                drive(0.45 , -0.45 , 0, 600);

                //moves to position
                drive(-0.45 , 0 , 0, 3400);
        }

//        //region Crater
//        switch(mineralPosition) {
//
//            case CENTER:
//                //hits center mineral
//                drive(0 , 0.45 , 0, 1700);
//
//                //backs up
//                drive(0 , -0.45 , 0, 600);
//
//                //strafes left
//                drive(-0.45 , 0 , 0, 3400);
//
//            case RIGHT:
//                //hits right mineral
//                drive(0.45 , 0.45 , 0, 1700);
//
//                //backs up
//                drive(-0.45 , -0.45 , 0, 600);
//
//                //moves to position
//                drive(-0.45 , 0 , 0, 3400);
//
//            case LEFT:
//                //hits right mineral
//                drive(-0.45 , 0.45 , 0, 1700);
//
//                //backs up
//                drive(0.45 , -0.45 , 0, 600);
//
//                //moves to position
//                drive(-0.45 , 0 , 0, 3400);
//        }
//
//
//        //aligns with wall
//        mecanumChassis.rotate(-42, 0.2, 1);
//
//
//        //double check alignment
//        drive(-0.45 , 0 , 0, 1000);
//        drive(0.45 , 0 , 0, 300);
//
//        //move to depo
//        drive(0 , -1 , 0, 1000);
//        drive(0 , 0.5 , 0, 3000);
//        drive(-0.45 , 0 , 0, 1000);
//        drive(0 , 0.5 , 0, 2000);
//
//        //endregion
    }


    void drive(double x, double y, double rotate, long time) {
        mecanumChassis.driveS(x , y , rotate);
        sleep(time);
        mecanumChassis.stop();
    }

    void driveGlobal(double x, double y, double rotate, long time) {
        mecanumChassis.resetAngle();
        mecanumChassis.driveGlobal(x , y , rotate);
        sleep(time);
        mecanumChassis.stop();
    }
}

