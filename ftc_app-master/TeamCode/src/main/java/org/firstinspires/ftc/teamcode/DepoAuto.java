package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Depo Auto", group = "13330 Pulsar")
public class DepoAuto extends LinearOpMode {
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
        hardware.lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.lifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hardware.lifter.setTargetPosition(11000);

        hardware.lifter.setPower(1);

        while(hardware.lifter.isBusy()){
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

    public void doDepo(MineralPosition mineralPosition) {
        //sets the rotation curve
        mecanumChassis.setRotate(0.1);

        //initial rotation
        mecanumChassis.rotate(90, 0.2, 0.05);

        switch (mineralPosition) {

            case CENTER:
                //drives into mineral
                mecanumChassis.drive(0, 0.2, 0, 2000);


                mecanumChassis.drive(0, -0.2, 0, 1000);

                //turns around
                mecanumChassis.rotate(180, 0.2, 0.05);

                //drives into depo
                mecanumChassis.drive(0, -0.2, 0, 5000);

                //TODO DEPOSIT
                sleep(1000);

                //aligns with wall
                mecanumChassis.rotate(-45, 0.2, 0.05);

                //strafes into wall
                mecanumChassis.drive(0.2, 0, 0, 1500);

                //drives forward
                mecanumChassis.drive(0, 0.5, 0, 1450);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //goes into crater
                mecanumChassis.drive(0, 0.5, 0, 1450);

                sleep(1000);

                break;

            case RIGHT:
                //rotates toward mineral
                mecanumChassis.rotate(-25, 0.2, 0.05);

                //hits and goes past
                mecanumChassis.drive(0, 0.2, 0, 2400);


                mecanumChassis.rotate(25, 0.2, 0.05);

                mecanumChassis.drive(0,-0.2, 0, 700);

                mecanumChassis.drive(-0.5, 0, 0, 2500);

                mecanumChassis.rotate(135, 0.2, 0.05);

                //strafes into wall
                mecanumChassis.drive(0.2, 0, 0, 1500);

                mecanumChassis.drive(0,-0.5, 0, 1500);


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

                break;

            case LEFT:
                //rotates toward mineral
                mecanumChassis.rotate(28, 0.2, 0.05);

                //drives into it
                mecanumChassis.drive(0, 0.2, 0, 3300);

                //turns around
                mecanumChassis.rotate(90, 0.2, 0.05);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,1500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //backs into depo
                mecanumChassis.drive(0, -0.2, 0, 1700);

                //TODO DEPOSIT
                sleep(1000);

                //drives forward
                mecanumChassis.drive(0, 0.2, 0, 1450);

                //re-aligns with will
                mecanumChassis.drive(0.2, 0, 0,500);

                //re-aligns with will
                mecanumChassis.drive(-0.1, 0, 0,500);

                //goes into crater
                mecanumChassis.drive(0, 0.5, 0, 1600);

                break;
        }
    }

    public void noDepo() {

    }
}
