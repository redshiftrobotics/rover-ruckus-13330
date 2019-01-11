package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "AutoRecorder", group = "13330 Pulsar")
public class AutoRecorder extends LinearOpMode{

    private AutoMaker autoMaker;
    private Console console;
    private Hardware hardware;

    private int fileNumber = 0;

    @Override
    public void runOpMode(){

        // initialize other classes needed
        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.autoMaker = new AutoMaker(this, hardware, console);

        Object[][] depoArray = new Object[100][];
        Object[][] craterArray = new Object[100][];
        boolean isDepo;


        console.Log("Recorder initialized. Press left/right bumper to set Depo side.", "");
        console.Update();

        while (true){

            if(gamepad1.left_bumper){
                isDepo = true;
                console.Log("Depo Side is true.", "");
                console.Update();
                break;
            }
            if(gamepad1.right_bumper){
                isDepo = false;
                console.Log("Depo side is false.", "");
                console.Update();
                break;
            }

            idle();
        }

        console.Log("Side selected. Input number for file using D-pad. Press Start to save.", "");
        console.Update();

        while(!gamepad1.start){

            if (gamepad1.dpad_up){
                fileNumber++;
                console.Log("File Number: ", fileNumber);
                console.Update();
            }

            if (gamepad1.dpad_down){
                fileNumber--;
                console.Log("File Number: ", fileNumber);
                console.Update();
            }

            idle();
        }

        console.Log("File number selected: " + fileNumber, "");
        console.Log("Once you have finished, press start to save auto to a file.", "");
        console.Update();

        waitForStart();

        if(isDepo){
            autoMaker.record(depoArray, isDepo);
        } else {
            autoMaker.record(craterArray, isDepo);
        }

        if(gamepad1.start){
            if(isDepo){
                autoMaker.saveArray(depoArray, "depoAuto" + fileNumber);
            } else {
                autoMaker.saveArray(craterArray, "craterAuto" + fileNumber);
            }
        }

    }
}
