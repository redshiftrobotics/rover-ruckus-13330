package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Robot { // parent class


    public class Drive { // parent method.

        public class LeftDrive { // this calls reference to the left motors in the hardware class.
            public void setPower(double power) {
                hardware.back_left_motor.setPower(power);
                hardware.front_left_motor.setPower(power);
            }

            public double[] getCurrentPosition() { // gets the current position of the left motors.

                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_left_motor.getCurrentPosition();
                currentPos[1] = hardware.back_left_motor.getCurrentPosition();

                return currentPos;
            }
        }

        public class RightDrive { // this calls reference to the right motors in the hardware class.
            public void setPower(double power) {
                hardware.back_right_motor.setPower(power);
                hardware.front_right_motor.setPower(power);
            }

            public double[] getCurrentPosition() { // gets the current position of the right motors.

                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_right_motor.getCurrentPosition();
                currentPos[1] = hardware.back_right_motor.getCurrentPosition();

                return currentPos;
            }
        }
    }
}
