package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "webcamTester", group = "13330 Pulsar")
public class webcamTester extends LinearOpMode {

    private MineralDetection mineralDetection;
    private Bitmap sample;
    private MineralPosition oldPosition;

    @Override
    public void runOpMode() {
        mineralDetection = new MineralDetection(this);
        waitForStart();

        mineralDetection.vuforiaInit(hardwareMap);

        while (opModeIsActive()) {
            try {
                sample = mineralDetection.getImage();
            } catch (InterruptedException e) {
            }

            ((FtcRobotControllerActivity) hardwareMap.appContext).setWebcamPreview(sample);

//            if(mineralDetection.getPosition(sample, 3) != oldPosition) {
//                telemetry.addData("Position", mineralDetection.getPosition(sample, 3));
//                oldPosition = mineralDetection.getPosition(sample, 3);
//                telemetry.update();
//            }
        }
    }
}

