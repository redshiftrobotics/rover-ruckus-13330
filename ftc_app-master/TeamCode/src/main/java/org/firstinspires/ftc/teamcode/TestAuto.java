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

    int lifterTime = 3850;

    @Override
    public void runOpMode() {

        // initialize other classes needed
        hardware = new Hardware(this);
        console = new Console(this);
        imu = new Imu(this);
        mineralDetection = new MineralDetection(this);

        mineralDetection.vuforiaInit(this.hardwareMap);

        while (!imu.imu.isGyroCalibrated()) {
            console.Status("kindly put me down. ill let you know when im ready...");
            idle();
        }

        console.Status("okay im fine");


        mecanumChassis = new MecanumChassis(this, DcMotor.ZeroPowerBehavior.BRAKE, imu);

        MineralPosition mineralPosition = MineralPosition.CENTER;

        waitForStart();

        imu.resetAngle();

        try {
            mineralPosition = mineralDetection.getPosition(mineralDetection.getImage());
        } catch (InterruptedException e) {
            console.Status("YOU BITCH. LET A MAN TAKE A PHOTO");
            sleep(100);
        }



        //drop down
        hardware.lifter.setPower(1);
        sleep(lifterTime);
        hardware.lifter.setPower(0);

        //sets the rotation curve
        mecanumChassis.setRotate(0.147);

        //initial rotation
        mecanumChassis.rotate(90, 0.2, 1);

        Thread t = new Thread() {
            public void run() {
                try {
                    hardware.lifter.setPower(-1);
                    sleep(lifterTime);
                    hardware.lifter.setPower(0);
                } catch (InterruptedException e) {

                }
            }
        };

        t.start();


        t.interrupt();

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
        mecanumChassis.driveS(x, y, rotate);
        sleep(time);
        mecanumChassis.stop();
        sleep(200);
    }

    void driveGlobal(double x, double y, double rotate, long time) {
        mecanumChassis.resetAngle();
        mecanumChassis.driveGlobal(x, y, rotate);
        sleep(time);
        mecanumChassis.stop();
    }
}

