package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Robot { //parent class

    private Hardware hardware;
    private LinearOpMode context;

    public Robot(Hardware hardware, LinearOpMode context) {
        this.hardware = hardware;
        this.context = context;
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
        power /= 2;
        double leftPower, rightPower;

        //makes the degrees between -359 and 359, zero is 360
        degrees = degrees % 360;

        //finds most efficient way to turn
        if (degrees < -180)
            degrees += 360;
        else if (degrees > 180)
            degrees -= 360;

        //restart imu movement tracking
        resetAngle();

        //turn right
        if (degrees < 0) {
            leftPower = -power;
            rightPower = power;

        } //turn left
        else if (degrees > 0) {
            leftPower = power;
            rightPower = -power;

        } else return;

        //set power to rotate.
        setPowerLeft(leftPower);
        setPowerRight(rightPower);

        //rotate until turn is completed.
        if(degrees > 0)
            while (getAngle() <= degrees){}
        else
            while (getAngle() >= degrees){}

        //turn the motors off.
        setPowerRight(0);
        setPowerLeft(0);

        context.sleep(1000);

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

    //clamps a value
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }


//PID

    /*
    pCoeff = .8f;
    iCoeff = .0002f;
    dCoeff = .2f;
    minimum = -1;
    maximum = 1;
    */

    public double PIDSeek(double seekValue, double currentValue, double pCoeff, double iCoeff, double dCoeff, double minimum, double maximum)
    {
        double integral = 0;
        double lastProportional = 0;


        double proportional = seekValue - currentValue;

        double derivative = (proportional - lastProportional);
        integral += proportional;
        lastProportional = proportional;

        //This is the actual PID formula. This gives us the value that is returned
        double value = pCoeff * proportional + iCoeff * integral + dCoeff * derivative;
        value = clamp(value, minimum, maximum);

        return value;
    }

}


