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

/**
 * Initialization and functions for the mechanum type chassis
 */

public class MecanumChassis {

    private Imu imu;

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    private double frontLeftPower;
    private double backLeftPower;
    private double frontRightPower;
    private double backRightPower;

    /**
     * Initialization constructor
     *
     * @param context The LinearOpMode context
     * @param zeroPowerBehavior What to do if the controller or power is 0: FLOAT OR BRAKE
     */

    public MecanumChassis(LinearOpMode context, DcMotor.ZeroPowerBehavior zeroPowerBehavior, Imu imu) { // creates the context in this class

        this.imu = imu;

        //region frontLeftMotor
        frontLeftMotor = context.hardwareMap.dcMotor.get("frontLeftMotor");
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        //endregion

        //region backLeftMotor
        backLeftMotor = context.hardwareMap.dcMotor.get("backLeftMotor");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        //endregion

        //region frontRightMotor
        frontRightMotor = context.hardwareMap.dcMotor.get("frontRightMotor");
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setZeroPowerBehavior(zeroPowerBehavior);
        //endregion

        //region backRightMotor
        backRightMotor = context.hardwareMap.dcMotor.get("backRightMotor");
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setZeroPowerBehavior(zeroPowerBehavior);
        //endregion
    }

    /**
     * Outdated drive function. Keeping it here for a trip down memory lane
     *
     * @param y The Y amount of power
     * @param x The X amount of power
     * @param rotate The rotate amount of power
     */

    public void drive(double x, double y, double rotate) {
        //I don't even know how to explain this
        frontLeftPower = y + x + rotate;
        backLeftPower = y - x + rotate;
        frontRightPower = y - x - rotate;
        backRightPower = y + x - rotate;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }

    public void setMotorPower(double power){
        frontLeftPower = power;
        backLeftPower = power;
        frontRightPower = power;
        backRightPower = power;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }


    /**
     * Drive function for teleop
     *
     * @param y Motor power Y
     * @param x Motor power X
     * @param rotate Motor power rotate
     */

    public void driveS(double x, double y, double rotate) {
        //magnitude of both vectors
        double magnitude = Math.hypot(x, y);

        //angle of controller
        double angle = getControllerAngle(x, y);

        //mecanum drive train code
        //the tires diagonal to each other will move the same direction
        //I named the diagonals red and blue based on an article I read online

        //sets the power for red and blue
        frontLeftPower = getPowerBlue(angle) * magnitude + rotate;
        backLeftPower = getPowerRed(angle) * magnitude + rotate;
        frontRightPower = getPowerRed(angle) * magnitude - rotate;
        backRightPower = getPowerBlue(angle) * magnitude - rotate;

        //sets the hardware motor power
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

    }



    public void driveGlobal(double x, double y, double rotate) {
        //magnitude of both vectors
        double magnitude = Math.hypot(x, y);

        //angle of controller
        double angle = getControllerAngle(x, y);

        //mecanum drive train code
        //the tires diagonal to each other will move the same direction
        //I named the diagonals red and blue based on an article I read online

        //sets the power for red and blue
        frontLeftPower = getPowerRed(Math.toRadians(imu.getAngle()) + angle) * magnitude + rotate;
        backLeftPower = getPowerBlue(Math.toRadians(imu.getAngle()) + angle) * magnitude + rotate;
        frontRightPower = getPowerBlue(Math.toRadians(imu.getAngle()) + angle) * magnitude - rotate;
        backRightPower = getPowerRed(Math.toRadians(imu.getAngle()) + angle) * magnitude - rotate;

        //sets the hardware motor power
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

    }

    public void driveGlobal(int angle, double rotate, double speed) {;

        //mecanum drive train code
        //the tires diagonal to each other will move the same direction
        //I named the diagonals red and blue based on an article I read online

        //sets the power for red and blue
        frontLeftPower = getPowerRed(Math.toRadians(imu.getAngle()) + angle) * speed + rotate;
        backLeftPower = getPowerBlue(Math.toRadians(imu.getAngle()) + angle) * speed + rotate;
        frontRightPower = getPowerBlue(Math.toRadians(imu.getAngle()) + angle) * speed - rotate;
        backRightPower = getPowerRed(Math.toRadians(imu.getAngle()) + angle) * speed - rotate;

        //sets the hardware motor power
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

    }


    /**
     * Drive function for autonomous
     *
     * @param angle The angle to drive at
     * @param rotate The rotation to drive at
     * @param speed The speed to drive at
     */

    public void driveS(int angle, double rotate, double speed) {

        //sets the power for red and blue
        frontLeftPower = getPowerBlue((double) angle) * speed + rotate;
        backLeftPower = getPowerRed((double) angle) * speed + rotate;
        frontRightPower = getPowerRed((double) angle) * speed + rotate;
        backRightPower = getPowerBlue((double) angle) * speed + rotate;

        //sets the hardware motor power
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

    }

    /**
     * Stops all motors
     */

    public void stop(){
        //sets the power of all the motors zero, stopping the robot.
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public double getControllerAngle(double x, double y){
        //angle of the joystick
        return Math.atan2(y, x);
    }

    /**
     * Returns the red motor power for mecanum wheels
     *
     * @param radians The desired angle of movement
     * @return The outputted motor power
     */

    public double getPowerRed(double radians) {
        return Math.sin((radians) - (Math.PI / 4));
    }

    /**
     * Returns the blue motor power for mecanum wheels
     *
     * @param radians The desired angle of movement
     * @return The outputted motor power
     */

    public double getPowerBlue(double radians) {
        return Math.sin((radians) + (Math.PI / 4));
    }

    /**
     * Returns the sensitivity of the joystick.
     *
     */

    public double getStickSensitivity(double x, double sensitivity){
        double y = Math.pow(x, sensitivity);
        return y;
    }
}
