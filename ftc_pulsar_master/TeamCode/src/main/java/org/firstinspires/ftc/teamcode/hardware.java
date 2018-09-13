package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class Hardware { // Here we get the DcMotors from the REV hub and assign their names.

    public DcMotor front_right_motor;
    public DcMotor front_left_motor;
    public DcMotor back_right_motor;
    public DcMotor back_left_motor;

    hardware(OpMode ctx){

        front_left_motor = ctx.hardwareMap.dcMotor.get("front_left_motor");
        front_right_motor = ctx.hardwareMap.dcMotor.get("front_right_motor");
        back_left_motor = ctx.hardwareMap.dcMotor.get("back_left_motor");
        back_right_motor = ctx.hardwareMap.dcMotor.get("back_right_motor");
    }

    public void loop(){

    }


}
