package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Custom Autonomous", group = "Custom")
public class CustomAuto extends LinearOpMode {
    private MineralDetection mineralDetection;
    private MineralPosition mineralPosition;
    private MecanumChassis mecanumChassis;
    private Hardware hardware;
    private Console console;
    private Imu imu;

    private CraterAuto craterAuto;
    private DepoAuto depoAuto;

    private Bitmap sample = null;
    private boolean analyzePhotoComplete = false;

    @Override
    public void runOpMode() throws InterruptedException {

        craterAuto = new CraterAuto();


        int current = 0;
        int delay = 0;
        boolean doHang = false;
        boolean doDepo = false;
        boolean doMove = false;

        boolean isCrater = false;

        while (!gamepad1.start) {


            if (gamepad1.right_bumper) {
                current++;
                if (current == 4) current = 0;
                sleep(200);
            } else if (gamepad1.left_bumper) {
                current--;
                if (current == -1) current = 4;
                sleep(200);
            }

            if (current == 0) { //if set on delay
                delay -= gamepad1.right_stick_y * 0.001;
            } else if (current == 1) { //if hang
                if (gamepad1.right_stick_y < 0) {
                    doHang = true;
                } else if (gamepad1.right_stick_y > 0) {
                    doHang = false;
                }
            } else if (current == 2) { //if doDepo
                if (gamepad1.right_stick_y < 0) {
                    doDepo = true;
                    doMove = true;
                } else if (gamepad1.right_stick_y > 0) {
                    doDepo = false;
                }
            } else if (current == 3) { //if doMove
                if (gamepad1.right_stick_y < 0) {
                    doMove = true;
                } else if (gamepad1.right_stick_y > 0) {
                    doMove = false;
                    doDepo = false;
                }
            } else if (current == 4) { //if isCrater
                if (gamepad1.right_stick_y < 0) {
                    isCrater = true;
                } else if (gamepad1.right_stick_y > 0) {
                    isCrater = false;
                }
            }

            telemetry.addData("Delay Time", "[%f s]", delay);
            telemetry.addData("Do Hang", "[%b]", doHang);
            telemetry.addData("Do Depo", "[%b]", doDepo);
            telemetry.addData("Do Move", "[%b]", doMove);

            telemetry.addLine();

            if (isCrater)
                telemetry.addData("Position", "Crater");
            else
                telemetry.addData("Position", "Depo");


            telemetry.update();
        }

        waitForStart();


        if (isCrater) {
            craterAuto.Init();

            if (doHang) {
                sample = craterAuto.takePhoto();

                Thread analyzePhoto = new Thread() {
                    public void run() {
                        mineralPosition = craterAuto.analyzePhoto(sample);
                        analyzePhotoComplete = true;
                    }
                };

                analyzePhoto.start();

                craterAuto.doLifter();


                while (!analyzePhotoComplete)
                    idle();

                analyzePhoto.interrupt();


                if(doMove){
                    if(doDepo){
                        sleep(delay);
                        depoAuto.doDepo(mineralPosition);
                    }
                }
            } else
                craterAuto.doLifter();

            if (doMove) {
                if (doDepo) {
                    craterAuto.doCrater(mineralPosition);
                }
            }

        } else {
            depoAuto.Init();
            if (doDepo) {
                sample = depoAuto.takePhoto();
                Thread analyzePhoto = new Thread() {
                    public void run() {
                        mineralPosition = depoAuto.analyzePhoto(sample);
                        analyzePhotoComplete = true;
                    }
                };

                analyzePhoto.start();

                depoAuto.doLifter();

                while (!analyzePhotoComplete)
                    idle();

                analyzePhoto.interrupt();
            } else
                depoAuto.doLifter();
        }
        if (doMove) {
            if (doDepo) {
                depoAuto.doDepo(mineralPosition);
            }
        }
    }
}

