package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Robot { //parent class

    private Hardware hardware;

    public Robot(Hardware hardware) {
        this.hardware = hardware;
    }

//DRIVE FUNCTIONS

    //sets the power of the right drivew ith a double, power
    public void setPowerRight(double power) {
        hardware.back_right_motor.setPower(power);
        hardware.front_right_motor.setPower(power);
    }

    //sets power of left drive with a double, power
    public void setPowerLeft(double power) {
        hardware.back_left_motor.setPower(power);
        hardware.front_left_motor.setPower(power);
    }

    //to drive forward
    public void drive(double power, long time){
        setPowerLeft(-power + hardware.correction);
        setPowerRight(-power);
        waitFor(time);
        setPowerLeft(0);
        setPowerRight(0);
    }

//SUPER FUNCTIONS

    //sets the angles to zero
    public void resetAngle() {
        hardware.oldAngle = hardware.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        hardware.globalAngle = 0;
    }

    //gets the angle
    public double getAngle() {

        //we determined that imu angles works in euler angles so
        hardware.angles = hardware.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double differenceAngle = hardware.angles.firstAngle - hardware.oldAngle.firstAngle;

        if (differenceAngle < -180)
            differenceAngle += 360;
        else if (differenceAngle > 180)
            differenceAngle -= 360;

        hardware.globalAngle += differenceAngle;

        hardware.oldAngle = hardware.angles;

        return hardware.globalAngle;
    }

    //returns a correction value to move in a straight line.
    public double checkDirection() {
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             //no adjustment.
        else
            correction = -angle;        //reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }

    //rotates the robot x degrees
    public void rotate(int degrees, double power) {
        double leftPower, rightPower;

        //restart imu movement tracking.
        resetAngle();

        //getAngle() returns + when rotating counter clockwise (left) and - when rotating
        //clockwise (right).

        if (degrees < 0) {   //turn right.
            leftPower = -power;
            rightPower = power;
        } else if (degrees > 0) {   //turn left.
            leftPower = power;
            rightPower = -power;
        } else return;

        //set power to rotate.
        setPowerLeft(leftPower);
        setPowerRight(rightPower);

        //rotate until turn is completed.
        if (degrees < 0) {
            //On right turn we have to get off zero first.
            while (getAngle() == 0) {
            }

            while (getAngle() > degrees) {
            }
        } else    //left turn.
            while (getAngle() < degrees) {
            }

        //turn the motors off.
        setPowerLeft(0);
        setPowerRight(0);

        //wait for finish
        waitFor(1000);

        //reset angle
        resetAngle();
    }

    //makes the robot nap
    public void waitFor(long milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
        }
    }
}


