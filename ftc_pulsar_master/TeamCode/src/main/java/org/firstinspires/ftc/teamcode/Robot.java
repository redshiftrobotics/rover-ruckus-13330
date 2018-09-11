package org.firstinspires.ftc.teamcode;


public class Robot {
    class LeftDrive {
        void setPower(double input){
            hardware.back_left_motor.setPower(input);
            hardware.front_left_motor.setPower(input);
        }
    }

    class RightDrive {
        void setPower(double input) {
            hardware.back_right_motor.setPower(input);
            hardware.front_right_motor.setPower(input);
        }
    }
}
