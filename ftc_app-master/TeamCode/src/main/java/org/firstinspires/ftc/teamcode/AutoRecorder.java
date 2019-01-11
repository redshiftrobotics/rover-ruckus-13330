package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "AutoRecorder", group = "13330 Pulsar")
public class AutoRecorder extends LinearOpMode {

    private AutoMaker autoMaker;
    private Hardware hardware;
    private Console console;
    private boolean isDepo = false;


    @Override
    public void runOpMode() {

        hardware = new Hardware(this);
        autoMaker = new AutoMaker(this, hardware);
        console = new Console(this);

        Object[][] depoArray = new Object[100][];
        Object[][] craterArray = new Object[100][];


        while (!gamepad1.start) {

            if (gamepad1.left_bumper)
                isDepo = true;

            if (gamepad1.right_bumper)
                isDepo = false;


            if (isDepo)
                console.Log("Side", "[DEPO]");
            else
                console.Log("Side", "[CRATER]");

            console.Update();

            idle();
        }


        waitForStart();

        while (opModeIsActive()) {

            if (isDepo) {
                autoMaker.record(depoArray, isDepo);
            } else {
                autoMaker.record(craterArray, isDepo);
            }

            idle();

        }

    }
}
