package org.firstinspires.ftc.teamcode;

public class Robot {
    public static class Drive {
        public static class LeftDrive {
            public static void setPower(double power) {
                hardware.back_left_motor.setPower(power);
                hardware.front_left_motor.setPower(power);
            }
        }

        public static class RightDrive {
            public static void setPower(double input) {
                hardware.back_right_motor.setPower(input);
                hardware.front_right_motor.setPower(input);
            }
        }
    }
}
