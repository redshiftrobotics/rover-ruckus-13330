package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * A class for recording Auto paths as text files which can be run later.
 */

@TeleOp(name = "AutoRecorder", group = "13330 Pulsar")
public class AutoRecorder extends LinearOpMode{

    private AutoMaker autoMaker;
    private Console console;
    private Hardware hardware;
    private MecanumChassis mecanumChassis;
    private Imu imu;

    private double speed = 0.3;


    @Override
    public void runOpMode(){

        // initialize other classes needed
        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.autoMaker = new AutoMaker(this, hardware, console);
        this.mecanumChassis = new MecanumChassis(this, hardware.zeroPowerBehavior, this.imu);

        //Arrays with which to write auto sequences to.
        Object[][] depoArray = new Object[100][];
        Object[][] craterArray = new Object[100][];
        boolean isDepo = false;

        while(!gamepad1.start){

            //Asks the driver if this auto is for the Depo or Crater side.
            if(gamepad1.left_bumper)
                isDepo = true;

            else if(gamepad1.right_bumper)
                isDepo = false;

            // Logs your decision.
            if(isDepo)
                console.Log("Side", "[DEPO]");
            else
                console.Log("Side", "[CRATER]");

            console.Update();
            idle();
        }

        waitForStart();

        //Checks which side you are writing for, and then records to the corresponding array.
        if(isDepo){
            autoMaker.record(depoArray, true);
        } else {
            autoMaker.record(craterArray, false);
        }

        //Allows the user to drive the chassis while recording.
        if(gamepad1.right_bumper){
            mecanumChassis.driveGlobal(-gamepad1.left_stick_x * speed, -gamepad1.left_stick_y * speed, -gamepad1.right_stick_x * speed);
        } else {
            mecanumChassis.driveS(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x);
        }

        //Allows the user to save the current Array to a file on the phone by pressing 'Start'.
        if(gamepad1.start){
            if(isDepo){
                autoMaker.saveArray(depoArray, "depoAuto");
            } else {
                autoMaker.saveArray(craterArray, "craterAuto");
            }
        }

    }
}
