package org.firstinspires.ftc.teamcode;


public class Robot { // parent class

    private Hardware hardware;

    public Robot(Hardware hardware) {
        this.hardware = hardware;
    }

    public void setPowerRight(double power) {
        hardware.back_right_motor.setPower(power * hardware.motorInverse[0]);
        hardware.front_right_motor.setPower(power * hardware.motorInverse[1]);
    }

    public double[] getCurrentPositionRight() { // gets the current position of the right motors.

        double[] currentPos = new double[2];
        currentPos[0] = hardware.front_right_motor.getCurrentPosition();
        currentPos[1] = hardware.back_right_motor.getCurrentPosition();

        return currentPos;
    }


    public void setPowerLeft(double power) {
        hardware.back_left_motor.setPower(power * hardware.motorInverse[2]);
        hardware.front_left_motor.setPower(power * hardware.motorInverse[3]);
    }

    public double[] getCurrentPositionLeft() { // gets the current position of the left motors.

        double[] currentPos = new double[2];
        currentPos[0] = hardware.front_left_motor.getCurrentPosition();
        currentPos[1] = hardware.back_left_motor.getCurrentPosition();

        return currentPos;
    }


}


