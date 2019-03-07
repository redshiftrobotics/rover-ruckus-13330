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
        mineralDetection.vuforiaInit(hardwareMap);
        MineralPosition mp = null;

        waitForStart();

        ((FtcRobotControllerActivity) hardwareMap.appContext).opModeName = "webcamTester";


        while (opModeIsActive()) {
            try {
                mp = mineralDetection.getPosition(mineralDetection.getImage());
            } catch (InterruptedException e) {
            }

            telemetry.addData("Mineral Position", mp);

            telemetry.update();

            sleep(100);
            idle();
        }
    }
}

