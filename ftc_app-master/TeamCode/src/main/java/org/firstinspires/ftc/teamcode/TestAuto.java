package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * A class for recording Auto paths as text files which can be run later.
 */

@TeleOp(name = "TestAuto", group = "13330 Pulsar")
public class TestAuto extends LinearOpMode{

    private AutoMaker autoMaker;
    private Console console;
    private Hardware hardware;
    @Override
    public void runOpMode(){

        // initialize other classes needed
        this.hardware = new Hardware(this);
        this.console = new Console(this);
        this.autoMaker = new AutoMaker(this, hardware, console);

       Object[][] array = autoMaker.readToArray("TestAutoMaker.txt");

       waitForStart();

       autoMaker.runArray(array, 0);

    }
}

