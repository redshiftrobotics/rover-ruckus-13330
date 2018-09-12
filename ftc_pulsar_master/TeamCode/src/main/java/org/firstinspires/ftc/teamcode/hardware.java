package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


abstract class hardware extends OpMode{ // Here we get the DcMotors from the REV hub and assign their names.

    //for four motors
    public static DcMotor front_right_motor;
    public static DcMotor front_left_motor;
    public static DcMotor back_right_motor;
    public static DcMotor back_left_motor;

    //for two motors
    public static DcMotor right_motor;
    public static DcMotor left_motor;

    @Override
    public void init() { // here we assign the motors to names in the hardware map.

        //for four motors
        hardware.front_left_motor = hardwareMap.dcMotor.get("name");
        hardware.front_right_motor = hardwareMap.dcMotor.get("name");
        hardware.back_left_motor = hardwareMap.dcMotor.get("name");
        hardware.back_right_motor = hardwareMap.dcMotor.get("name");


        //for two motors

        hardware.right_motor = hardwareMap.dcMotor.get("right_motor");
        hardware.left_motor = hardwareMap.dcMotor.get("left_motor");
    }

}
