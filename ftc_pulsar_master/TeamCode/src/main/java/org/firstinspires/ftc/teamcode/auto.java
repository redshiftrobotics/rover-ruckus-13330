package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto")
public class auto {



    public void turn(int direction, float time, float power){

        if (direction == 1){ // left

         hardware.front_left_motor.setPower(power);
         hardware.back_left_motor.setPower(power);

        } else if (direction == 2){ // right

         hardware.front_right_motor.setPower(power);
         hardware.back_right_motor.setPower(power);

        }
    }






}
