package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "AutoRecorder", group = "13330 Pulsar")
public class AutoRecorder extends LinearOpMode{

    private AutoMaker autoMaker;
    private Hardware hardware;
    private Console console;
    private boolean isDepo = false;

    private int fileNumber = 0;

    @Override
    public void runOpMode(){

        Object[][] depoArray = new Object[100][];
        Object[][] craterArray = new Object[100][];


        console.Log("Recorder Initied. Press left/right bumper to set Depo side.", "");

        console.Update();

        while (!isDepo){

            if(gamepad1.left_bumper){
                isDepo = true;
                console.Log("Depo Side is true.", "");
                console.Update();
            }
            if(gamepad1.right_bumper){
                isDepo = false;
                console.Log("Depo side is false.", "");
                console.Update();
            }

            idle();
        }

        console.Log("Side selected. Input number for file using D-pad. Press Start to save.", "");
        console.Update();

        while(!gamepad1.start){

            if (gamepad1.dpad_up){
                fileNumber = fileNumber + 1;

                console.Log("File Number: ", fileNumber);
                console.Update();
            }

            if (gamepad1.dpad_down){
                fileNumber = fileNumber - 1;

                console.Log("File Number: ", fileNumber);
                console.Update();
            }

            idle();
        }

        console.Log("File number selected.", "");
        console.Log("Once you have finished, press start to save auto to a file.", "");
        console.Update();

        waitForStart();

        while (opModeIsActive()){

            if(isDepo == true){
                autoMaker.record(depoArray, isDepo);
            } else {
                autoMaker.record(craterArray, isDepo);
            }

            if(gamepad1.start){
                if(isDepo == true){
                    autoMaker.saveArray(depoArray, "depoAuto" + fileNumber);
                } else {
                    autoMaker.saveArray(craterArray, "craterAuto" + fileNumber);
                }

            }

            idle();

        }

    }
}
