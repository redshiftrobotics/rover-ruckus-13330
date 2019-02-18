package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

        doDepo(mineralPosition);
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

        console.Status("okay im fine");


        mecanumChassis = new MecanumChassis(this, DcMotor.ZeroPowerBehavior.BRAKE, imu);

    }

    public void doLifter() {
        hardware.lifter.setPower(1);

        while (!hardware.lifterLimitSwitchUp.isPressed()) {
            idle();
        }
        hardware.lifter.setPower(0);

        //sets the rotation curve
        mecanumChassis.setRotate(0.147);

        //initial rotation
        mecanumChassis.rotate(90, 0.2, 1);
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
        return mineralDetection.getPosition(sample, 2);
    }

    public void doDepo(MineralPosition mineralPosition) {
        switch (mineralPosition) {

            case CENTER:
                //drives into mineral
                mecanumChassis.drive(0, 0.2, 0, 5000);

                //alignes with wall
                mecanumChassis.rotate(135, 0.7, 0.05);

                //strafes into wall
                mecanumChassis.drive(0.2, 0, 0, 1500);

                //TODO DEPOSIT
                sleep(1000);

                //drives forward
                mecanumChassis.drive(0, 0.5, 0, 1450);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //goes into crater
                mecanumChassis.drive(0, 0.5, 0, 1450);

            case RIGHT:
                //rotates toward mineral
                mecanumChassis.rotate(-15, 0.7, 0.05);

                //hits and goes past
                mecanumChassis.drive(0, 0.2, 0, 4000);

                //rotates towards depo
                mecanumChassis.rotate(80, 0.7, 0.05);

                //drives into depo
                mecanumChassis.drive(0, 0.2, 0, 1900);

                //rotates to team marker position
                mecanumChassis.rotate(103, 0.7, 0.05);

                //TODO DEPOSIT
                sleep(1000);

                //rotates back
                mecanumChassis.rotate(-90, 0.7, 0.05);

                //drives to wall
                mecanumChassis.drive(0, 0.2, 0, 1450);

                //aligns with wall
                mecanumChassis.rotate(30, 0.7, 0.05);

                //drives forward
                mecanumChassis.drive(0, 0.5, 0, 1450);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //goes into crater
                mecanumChassis.drive(0, 0.5, 0, 1450);

            case LEFT:
                //rotates toward mineral
                mecanumChassis.rotate(18, 0.7, 0.05);

                //drives int/past it
                mecanumChassis.drive(0, 0.2, 0, 4400);

                //turns around
                mecanumChassis.rotate(120, 0.7, 0.05);

                //backs into depo
                mecanumChassis.drive(0, -0.2, 0, 1500);

                //TODO DEPOSIT
                sleep(1000);

                //drives forward
                mecanumChassis.drive(0, 0.2, 0, 1450);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //goes into crater
                mecanumChassis.drive(0, 0.5, 0, 1450);
        }
    }

    public void noDepo() {

    }
}
