package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * A class for recording Auto paths as text files which can be run later.
 */

@TeleOp(name = "TestAuto", group = "13330 Pulsar")
public class TestAuto extends LinearOpMode{

    @Override
    public void runOpMode(){

        // initialize other classes needed
        Hardware hardware = new Hardware(this);
        Console console = new Console(this);
        AutoMaker autoMaker = new AutoMaker(this, hardware, console);

       Object[][] array = autoMaker.readToArray("TestAutoMaker.txt");

       waitForStart();

       autoMaker.runArray(array, 0);

    }
}

