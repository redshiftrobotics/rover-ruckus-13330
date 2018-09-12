package org.firstinspires.ftc.teamcode;

public class Robot {
    public static class Drive {

        public static void drive(double lPower, double rPower, long time) {

            LeftDrive.setPower(lPower);
            RightDrive.setPower(rPower);

            try{
                Thread.sleep(time);
            } catch(InterruptedException e){
                System.out.println(e);
            }

            LeftDrive.setPower(0);
            RightDrive.setPower(0);
        }

        public static class LeftDrive {
            public static void setPower(double power) {
                hardware.back_left_motor.setPower(power);
                hardware.front_left_motor.setPower(power);
            }
            public static double[] getCurrentPosition(){
                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_left_motor.getCurrentPosition();
                currentPos[1] = hardware.back_left_motor.getCurrentPosition();

                return currentPos;
            }
        }

        public static class RightDrive {
            public static void setPower(double input) {
                hardware.back_right_motor.setPower(input);
                hardware.front_right_motor.setPower(input);
            }
            public static double[] getCurrentPosition(){
                double[] currentPos = new double[2];
                currentPos[0] = hardware.front_right_motor.getCurrentPosition();
                currentPos[1] = hardware.back_right_motor.getCurrentPosition();

                return currentPos;
            }
        }
    }
}
