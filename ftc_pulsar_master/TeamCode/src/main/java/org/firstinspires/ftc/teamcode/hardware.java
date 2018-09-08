package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


abstract class hardware extends OpMode{
    public static DcMotor front_right_motor;
    public static DcMotor front_left_motor;
    public static DcMotor back_right_motor;
    public static DcMotor back_left_motor;

    @Override
    public void init() {
        hardware.front_left_motor = hardwareMap.dcMotor.get("name");
        hardware.front_right_motor = hardwareMap.dcMotor.get("name");
        hardware.back_left_motor = hardwareMap.dcMotor.get("name");
        hardware.back_right_motor = hardwareMap.dcMotor.get("name");
    }

}
