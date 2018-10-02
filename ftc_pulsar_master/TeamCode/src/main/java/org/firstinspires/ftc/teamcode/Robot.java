/* Copyright (c) 2017 FIRST. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification,
* are permitted (subject to the limitations in the disclaimer below) provided that
* the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list
* of conditions and the following disclaimer.
*
* Redistributions in binary form must reproduce the above copyright notice, this
* list of conditions and the following disclaimer in the documentation and/or
* other materials provided with the distribution.
*
* Neither the name of FIRST nor the names of its contributors may be used to endorse or
* promote products derived from this software without specific prior written permission.
*
* NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
* LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Robot { //parent class

    private Hardware hardware;
    private LinearOpMode context;

    //constructor that allows to use opModes and hardware
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

    //to drive forward with time
    public void drive(double power, long time) {
        setPowerLeft(-power + hardware.correction);
        setPowerRight(-power);
        context.sleep(time);
        setPowerLeft(0);
        setPowerRight(0);
    }

    //to drive forward with distance in inches
    public void encoderDrive(double power, int distance) {

        double circumference = Math.PI * hardware.WHEEL_DIAMETER;
        double encoderDistance = ((360 / circumference) * distance) * hardware.GEAR_RATIO;

        resetEncoders();
        setTargetPosition((int) encoderDistance);

        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPowerLeft(power);
        setPowerRight(power);

        //wait for finish
        while (context.opModeIsActive() && isBusyLeft() && isBusyRight()) {
        }

        //stop moving
        setPowerLeft(0);
        setPowerRight(0);

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDrivePID(double power, int distance, double stopThreashold) {

        double powerLeft = 1, powerRight = 1;

        double circumference = Math.PI * hardware.WHEEL_DIAMETER;
        double encoderDistance = ((360 / circumference) * distance) * hardware.GEAR_RATIO;

        resetEncoders();

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //wait for finish
        while (context.opModeIsActive() && powerLeft >= stopThreashold && powerRight >= stopThreashold) {
            hardware.correction = checkDirection();
            powerLeft = PIDSeek(hardware.back_left_motor.getCurrentPosition(), (int) encoderDistance, 0.0007, 0.000000001, 0.00009);
            powerRight = PIDSeek(hardware.back_right_motor.getCurrentPosition(), (int) encoderDistance, 0.0007, 0.000000001, 0.00009);

            setPowerLeft(powerLeft);
            setPowerRight(powerRight + hardware.correction);
        }

        //stop moving
        setPowerLeft(0);
        setPowerRight(0);
    }

//ENCODERS

    //returns if the right drive is moving
    public boolean isBusyRight() {
    if (hardware.back_right_motor.isBusy() && hardware.front_right_motor.isBusy())
        return true;
    else
        return false;

}

    //returns if the left drive is moving
    public boolean isBusyLeft() {
        if (hardware.back_left_motor.isBusy() && hardware.front_left_motor.isBusy())
            return true;
        else
            return false;

    }

    //resets encoders
    public void resetEncoders() {
        hardware.back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hardware.back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    //sets the target position of each motor
    public void setTargetPosition(int position) {
        hardware.back_left_motor.setTargetPosition(position);
        hardware.front_left_motor.setTargetPosition(position);

        hardware.back_right_motor.setTargetPosition(position);
        hardware.front_right_motor.setTargetPosition(position);
    }

    //sets a main runmode for each motor
    public void setRunMode(DcMotor.RunMode runMode) {
        hardware.back_left_motor.setMode(runMode);
        hardware.front_left_motor.setMode(runMode);

        hardware.back_right_motor.setMode(runMode);
        hardware.front_right_motor.setMode(runMode);
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
    public void rotate(int degrees, double power, double stopThreashold) {
        double leftPower = 1, rightPower = 1;

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
        while (context.opModeIsActive() && leftPower >= stopThreashold && rightPower >= stopThreashold) {
            if (degrees > 0) {
                leftPower = PIDSeek(degrees, getAngle(), 0.0007, 0.000000001, 0.00009);
                rightPower = -PIDSeek(degrees, getAngle(), 0.0007, 0.000000001, 0.00009);
            } else {
                leftPower = -PIDSeek(degrees, getAngle(), 0.0007, 0.000000001, 0.00009);
                rightPower = PIDSeek(degrees, getAngle(), 0.0007, 0.000000001, 0.00009);
            }
            setPowerLeft(leftPower);
            setPowerRight(rightPower);
            context.telemetry.addData("Angle", getAngle());
            context.telemetry.addData("Left Power", hardware.back_left_motor.getPower());
            context.telemetry.addData("Right Power", hardware.back_right_motor.getPower());
            context.telemetry.update();
        }
        setPowerLeft(0);
        setPowerRight(0);

        //reset angle
        resetAngle();
    }

    //makes the robot take a fat nap
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

    double integral;
    double lastProportional = 0;

    //given a current value and a seek value it can return incremental values in between given a p, i, d coefficient
    public double PIDSeek(double seekValue, double currentValue, double pCoeff, double iCoeff, double dCoeff) {

        double proportional = seekValue - currentValue;

        double derivative = (proportional - lastProportional);
        integral += proportional;
        lastProportional = proportional;

        //This is the actual PID formula. This gives us the value that is returned
        double value = pCoeff * proportional + iCoeff * integral + dCoeff * derivative;

        context.telemetry.addData("PID Value", value);
        context.telemetry.addData("porportional", proportional);
        context.telemetry.addData("intergral", integral);

        return value;

    }

}


