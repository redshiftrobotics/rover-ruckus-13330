package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "AutoRecorder", group = "13330 Pulsar")
public class AutoRecorder extends LinearOpMode{

    private AutoMaker autoMaker;
    private Console console;
    private Hardware hardware;
    @Override
    public void runOpMode(){

        // initialize other classes needed
        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.autoMaker = new AutoMaker(this, hardware, console);

        Object[][] depoArray = new Object[100][];
        Object[][] craterArray = new Object[100][];
        boolean isDepo = false;

        while(!gamepad1.start){

            if(gamepad1.left_bumper)
                isDepo = true;

            else if(gamepad1.right_bumper)
                isDepo = false;

            if(isDepo)
                console.Log("Side", "[DEPO]");
            else
                console.Log("Side", "[CRATER]");

            console.Update();
            idle();
        }

        waitForStart();

        if(isDepo){
            autoMaker.record(depoArray, true);
        } else {
            autoMaker.record(craterArray, false);
        }

        if(gamepad1.start){
            if(isDepo){
                autoMaker.saveArray(depoArray, "depoAuto");
            } else {
                autoMaker.saveArray(craterArray, "craterAuto");
            }
        }

    }
}
