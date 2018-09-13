package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

abstract public class Robot extends OpMode { // parent class


    public static class Drive { // parent method.


        public static void drive(double lPower, double rPower, long time) { // the sub method that handles driving.
            LeftDrive.setPower(lPower);
            RightDrive.setPower(rPower);


            try {
                Thread.sleep(time);
            } catch (InterruptedException e) { // "sleep" method that allows the robot to move for only a specified time.
                System.out.println(e);
            }


            LeftDrive.setPower(0);
            RightDrive.setPower(0);
        }


        static class LeftDrive { // this calls reference to the left motors in the hardware class.
            public static void setPower(double power) {
                hardware.back_left_motor.setPower(power);
                hardware.front_left_motor.setPower(power);
            }

            double[] getCurrentPosition() { // gets the current position of the motors.
                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_left_motor.getCurrentPosition();
                currentPos[1] = hardware.back_left_motor.getCurrentPosition();

                return currentPos;
            }
        }

        static class RightDrive { // this calls reference to the right motors in the hardware class.
            public static void setPower(double power) {
                hardware.back_right_motor.setPower(power);
                hardware.front_right_motor.setPower(power);
            }

            double[] getCurrentPosition() {
                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_right_motor.getCurrentPosition();
                currentPos[1] = hardware.back_right_motor.getCurrentPosition();

                return currentPos;
            }
        }
    }
}
