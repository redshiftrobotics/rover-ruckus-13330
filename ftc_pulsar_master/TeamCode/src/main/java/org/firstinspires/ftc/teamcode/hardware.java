package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


abstract class hardware extends OpMode{ // Here we get the DcMotors from the REV hub and assign their names.

    public static DcMotor front_right_motor;
    public static DcMotor front_left_motor;
    public static DcMotor back_right_motor;
    public static DcMotor back_left_motor;

    @Override
    public void init() { // here we assign the motors to names in the hardware map.


        front_left_motor = hardwareMap.dcMotor.get("front_left_motor");
        front_right_motor = hardwareMap.dcMotor.get("front_right_motor");
        back_left_motor = hardwareMap.dcMotor.get("back_left_motor");
        back_right_motor = hardwareMap.dcMotor.get("back_right_motor");
    }

}
