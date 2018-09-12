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

        hardware.front_left_motor = hardwareMap.dcMotor.get("name");
        hardware.front_right_motor = hardwareMap.dcMotor.get("name");
        hardware.back_left_motor = hardwareMap.dcMotor.get("name");
        hardware.back_right_motor = hardwareMap.dcMotor.get("name");
    }

}
