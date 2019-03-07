package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Crater Auto", group = "13330 Pulsar")
public class CraterAuto extends LinearOpMode {
    private MineralDetection mineralDetection;
    private MineralPosition mineralPosition;
    private MecanumChassis mecanumChassis;
    private Hardware hardware;
    private Console console;
    private Imu imu;

    private Bitmap sample = null;
    private boolean analyzePhotoComplete = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Init();

        waitForStart();

        sample = takePhoto();

        Thread analyzePhoto = new Thread() {
            public void run() {
                mineralPosition = analyzePhoto(sample);
                analyzePhotoComplete = true;
            }
        };

        analyzePhoto.start();

        doLifter();

        while (!analyzePhotoComplete)
            idle();

        analyzePhoto.interrupt();

        Thread lifterDown = new Thread() {
            public void run() {
                try {

                    sleep(1000);
                    hardware.lifter.setTargetPosition(0);

                    hardware.lifter.setPower(1);

                    while (hardware.lifter.isBusy()) {
                        idle();
                    }

                    hardware.lifter.setPower(0);
                } catch(InterruptedException e){

                }
            }
        };

        lifterDown.start();

        doCrater(mineralPosition);
    }

    public void Init() {
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

        mecanumChassis = new MecanumChassis(this, DcMotor.ZeroPowerBehavior.BRAKE, imu);

        console.Status("okay im fine");
    }

    public void doLifter() {
        hardware.lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.lifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hardware.lifter.setTargetPosition(11000);

        hardware.lifter.setPower(1);

        while (hardware.lifter.isBusy()) {
            idle();
        }

        hardware.lifter.setPower(0);
    }

    public Bitmap takePhoto() {
        try {
            return mineralDetection.getImage();
        } catch (InterruptedException e) {
            console.Status("common!! lemme take a selfie");
            sleep(100);
            return null;
        }
    }

    public MineralPosition analyzePhoto(Bitmap sample) {
        return mineralDetection.getPosition(sample);
    }

    public void doCrater(MineralPosition mineralPosition) {

        //sets the rotation curve
        mecanumChassis.setRotate(0.1);

        //initial rotation
        mecanumChassis.rotate(90, 0.2, 0.05);

        switch (mineralPosition) {

            case CENTER:

                mecanumChassis.drive(0,0.2,0,1000);

                mecanumChassis.drive(0,-0.2,0,800);

                mecanumChassis.rotate(-90, 0.2, 0.05);

                mecanumChassis.drive(0,-0.2,0,3000);


                break;

            case RIGHT:
                mecanumChassis.rotate(-25, 0.2, 0.05);

                mecanumChassis.drive(0,0.2,0,1000);


                break;

            case LEFT:

                mecanumChassis.rotate(15, 0.2, 0.05);

                mecanumChassis.drive(0,0.2,0,1500);
                mecanumChassis.drive(0,0.2,0.1,3000);

                break;
        }

        //aligns with wall
        mecanumChassis.rotate(-42, 0.7, 0.05);

        //double check alignment
        mecanumChassis.drive(-0.45, 0, 0, 1000);
        mecanumChassis.drive(0.45, 0, 0, 300);

        //move to depo
        mecanumChassis.drive(0, -1, 0, 1000);
        mecanumChassis.drive(0, 0.5, 0, 3000);
        mecanumChassis.drive(-0.45, 0, 0, 1000);
        mecanumChassis.drive(0, 0.5, 0, 2000);
    }


    public void deposit(){
        hardware.depositServo.setPosition(1);
    }


    public void noDepo() {

    }
}
