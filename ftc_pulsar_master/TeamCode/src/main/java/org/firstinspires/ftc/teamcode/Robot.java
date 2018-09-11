package org.firstinspires.ftc.teamcode;


public class Robot {


    public void drive(float time, float power){
        hardware.front_left_motor.setPower(power);
        hardware.back_left_motor.setPower(power);
        hardware.front_right_motor.setPower(power);
        hardware.back_right_motor.setPower(power);

    }
}
