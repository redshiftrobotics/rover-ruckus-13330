package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by julian on 11/6/18.
 */

public class ControllerMethods {
    private Hardware hardware;
    private LinearOpMode context;
    private Robot robot;
    private Console console;


    //constructor that allows the Robot class to use opModes and hardware
    public ControllerMethods(Hardware hardware, LinearOpMode context, Robot robot) {
        this.hardware = hardware;
        this.context = context;
        this.robot = robot;
        this.console = new Console(hardware, this.robot, context);
    }

    public void speedToggle(){
        robot.speedToggle = !robot.speedToggle;
        console.Log("Toggled Speed!", null);
    }

    public void rightDrivePower(double power){
        console.Log("set motor power to", power);
    }

    public void leftDrivePower(double power){
        console.Log("set motor power to", power);
    }

    public void notAssigned(){
        return;
    }
}
