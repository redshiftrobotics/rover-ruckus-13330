/*
 * Copyright (c) 2018. RED SHIFT ROBOTICS. All rights reserved.
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
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Robot { //parent class

    private Hardware hardware;
    private LinearOpMode context;
    private Console console;
    private Imu imu;


    //constructor that allows the Robot class to use opModes and hardware
    public Robot(Hardware hardware, LinearOpMode context) {
        this.hardware = hardware;
        this.context = context;
        this.imu = new Imu(context);
        this.console = new Console(hardware, this, context);
    }

    public boolean fastMode = false;
    public double[] speeds = {0.3, 0.8};
    public int[] lifterPositions = {0, -28525, -28495, -22780, -5375};
    public int currentLifterPosition = 0;
    public int[] armPositions = {0, -28525, -29201, -22780, -5375};

    public double speed = speeds[0];

    public double drivePower = 0.2;
    public double timeMultiplier = 1.0;
    public double turnThreashold = 0.9;
    public double sleepTime = 50;

    //region Drive Methods

    public void setPowerLeft(double power) {
        hardware.backLeftMotor.setPower(-power * speed);
        hardware.frontLeftMotor.setPower(-power * speed);
    }

    public void setPowerRight(double power) {
        hardware.backRightMotor.setPower(-power * speed);
        hardware.frontRightMotor.setPower(-power * speed);
    }

    public void setZeroPowerBehavior() {
        hardware.frontLeftMotor.setZeroPowerBehavior(hardware.zeroPowerBehavior);
        hardware.backLeftMotor.setZeroPowerBehavior(hardware.zeroPowerBehavior);

        hardware.frontRightMotor.setZeroPowerBehavior(hardware.zeroPowerBehavior);
        hardware.backRightMotor.setZeroPowerBehavior(hardware.zeroPowerBehavior);
    }

    public void setMineralKickerPosition(double position) {
        hardware.mineralKicker1.setPosition(position);
        hardware.mineralKicker2.setPosition(1 - position);
    }

    public void collect(double power) {
        hardware.collector.setPower(power);
    }

    public void setLifterPositions(int position, double power) {
        hardware.lifter.setTargetPosition(position);
        hardware.lifter.setPower(power);
    }

    public void waitForMotor(DcMotor motor) {
        while (motor.isBusy() && context.opModeIsActive()) {
            context.idle();
        }
    }

    public void depositMineral() {
        hardware.depositor.setPosition(1);
        context.sleep(500);
        hardware.depositor.setPosition(0);
    }

    public void setLifterMode(DcMotor.RunMode runMode) {
        hardware.lifter.setMode(runMode);
    }

    public void updateSpeed() {
        if (fastMode) {
            speed = speeds[1];
        } else {
            speed = speeds[0];
        }

    }

    //endregion

    //region Encoders

    public void resetEncoders() {
        hardware.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hardware.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setTargetPosition(int position) {
        hardware.backLeftMotor.setTargetPosition(position);
        hardware.frontLeftMotor.setTargetPosition(position);

        hardware.backRightMotor.setTargetPosition(position);
        hardware.frontRightMotor.setTargetPosition(position);
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        hardware.backLeftMotor.setMode(runMode);
        hardware.frontLeftMotor.setMode(runMode);

        hardware.backRightMotor.setMode(runMode);
        hardware.frontRightMotor.setMode(runMode);
    }
    //endregion

    //region Super Functions


    public void rotatePID(int degrees, double power, double stopThreashold) {
        double turnPower = 1;

        //makes the degrees between -359 and 359, zero is 360
        degrees = degrees % 360;

        //finds most efficient way to turn
        if (degrees < -180)
            degrees += 360;
        else if (degrees > 180)
            degrees -= 360;

        //restart imu movement tracking
        imu.resetAngle();

        //turn right
        while (context.opModeIsActive() && Math.abs(turnPower) >= stopThreashold) {

            turnPower = PIDSeek(degrees, imu.getAngle(), 0.00002, 0.00000001, 0.000009);

            setPowerLeft(turnPower * 100 * power);
            setPowerRight(-turnPower * 100 * power);
        }
        setPowerLeft(0);
        setPowerRight(0);

        //reset angle
        imu.resetAngle();
    }

    public void rotate(int degrees, double power, double stopThreashold) {
        double turnPower = power;
        double turnPercentage = 0;

        //makes the degrees between -359 and 359, zero is 360
        degrees = degrees % 360;

        // finds most efficient way to turn
        if (degrees < -180)
            degrees += 360;
        else if (degrees > 180)
            degrees -= 360;

        imu.resetAngle(); // restart imu movement tracking
        console.Log("BeforeLoop", turnPercentage);


        while (context.opModeIsActive() && turnPercentage < stopThreashold) { // while the percentage of turn is less than threshold

            console.Status(" " + turnPower * (1 - turnPercentage * 2));

            if (degrees < 0) { // turn right
                setPowerLeft(turnPower);//* (1 - turnPercentage * 2));
                setPowerRight(-turnPower);// * (1 - turnPercentage * 2));
            } else { // turn left
                setPowerLeft(-turnPower);// * (1 - turnPercentage * 2));
                setPowerRight(turnPower);// * (1 - turnPercentage * 2));
            }


            turnPercentage = imu.getAngle() / degrees; // sets turnPercentage

            context.idle();
        }

        setPowerLeft(0);
        setPowerRight(0);

        //reset angle
        imu.resetAngle();
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public double decay(double turnPercentage, double toTurnDegrees) {
        double x = toTurnDegrees * turnPercentage;

        return Math.pow(Math.cos((Math.PI / 2) * (x / toTurnDegrees)), 0.6);
    }

    public void openArm() {
        hardware.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.arm1.setTargetPosition(53);
        hardware.arm1.setPower(1);
        hardware.arm2.setPosition(0.2);
        hardware.arm1.setTargetPosition(100);
    }

    public void initArm() {
        hardware.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.arm1.setTargetPosition(100);
        hardware.arm1.setPower(1);
    }

    public void setArmPosition(double position) {
        hardware.arm2.setPosition(Range.clip(position, 0, 1));
    }

    public void setArmPower(int position) {
        hardware.arm1.setTargetPosition(armPositions[position]);
        hardware.arm1.setPower(0.3);
    }

    //endregion

    //region PID

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
        context.telemetry.addData("Angle", imu.getAngle());
        context.telemetry.addData("Left Power", hardware.backLeftMotor.getPower());
        context.telemetry.addData("Right Power", hardware.backRightMotor.getPower());
        context.telemetry.update();

        return value;

    }

    //endregion

    //region ASAM

    public long startTime;
    public long totalTime;
    public long elapsedTime;

    //test drive with ASAM implemented.
    public void asamDrive(long time, long accelTime) {
        startTime = System.currentTimeMillis();
        totalTime = time;
        elapsedTime = 0;
        while (elapsedTime < totalTime) {
            elapsedTime = System.currentTimeMillis() - startTime;

            setPowerLeft(-computeMotorPower(totalTime, (long) elapsedTime, 0, 1, accelTime));
            setPowerRight(-computeMotorPower(totalTime, (long) elapsedTime, 0, 1, accelTime));
        }
        setPowerRight(0);
        setPowerLeft(0);
    }

    /**
     * Compute the motor power for a given timestamp along an ASAM curve
     *
     * @param totalRunTime total time allocated to the movement
     * @param elapsedTime  time elapsed since the movement began
     * @param startSpeed   starting speed of the movement
     * @param endSpeed     ending speed of the movement
     * @param accelTime    amount of time to accelerate/decelerate
     * @return
     */
    public double computeMotorPower(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {
        if (elapsedTime <= accelTime) {
            return computeAcceleration(totalRunTime, elapsedTime, startSpeed, endSpeed, accelTime);
        } else if (elapsedTime < (totalRunTime - accelTime)) {
            return endSpeed;
        } else {
            return computeDeceleration(totalRunTime, elapsedTime, startSpeed, endSpeed, accelTime);
        }
    }

    /**
     * Compute the acceleration section of the curve
     *
     * @param totalRunTime total move time
     * @param elapsedTime  total elapsed time
     * @param startSpeed   starting speed
     * @param endSpeed     ending speed
     * @param accelTime    amount of time to accelerate
     * @return current motor power
     */
    private double computeAcceleration(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {

        return (
                ((startSpeed - endSpeed) / 2)
                        * Math.cos((elapsedTime * Math.PI) / accelTime)
                        + ((startSpeed + endSpeed) / 2)
        );
    }

    /**
     * Compute the deceleration section of the curve
     *
     * @param totalRunTime total move time
     * @param elapsedTime  total elapsed time
     * @param startSpeed   starting speed
     * @param endSpeed     ending speed
     * @param accelTime    amount of time to deceleration
     * @return current motor power
     */
    private double computeDeceleration(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {

        return (
                ((endSpeed - startSpeed) / 2)
                        * Math.cos(
                        (Math.PI * (totalRunTime - accelTime - elapsedTime)) / accelTime
                )
                        + ((startSpeed + endSpeed) / 2)
        );
    }

    //endregion


}


