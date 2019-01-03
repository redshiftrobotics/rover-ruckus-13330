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
 * Created by julian on 12/18/18.
 */

public class MecanumChassis {

    private Imu imu;

    public DcMotor frontRightMotor;
    public DcMotor frontLeftMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;

    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;

    public MecanumChassis(LinearOpMode context, DcMotor.ZeroPowerBehavior zeroPowerBehavior) { // creates the context in this class
        imu = new Imu(context);

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

    public void drive(double y, double x, double rotate) {
        frontLeftPower = y + x + rotate;
        backLeftPower = y - x + rotate;
        frontRightPower = y - x - rotate;
        backRightPower = y + x - rotate;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }

    public void drives(double y, double x, double rotate) {
        double xSqr = Math.pow(x, 2);
        double ySqr = Math.pow(y, 2);

        double magnitude = Math.sqrt(xSqr + ySqr);
        double angle = Math.atan2(y, x);

        frontLeftPower = getPowerBlue(imu.getAngle() + angle) * magnitude + rotate;
        backLeftPower = getPowerRed(imu.getAngle() + angle) * magnitude + rotate;
        frontRightPower = getPowerRed(imu.getAngle() + angle) * magnitude + rotate;
        backRightPower = getPowerBlue(imu.getAngle() + angle) * magnitude + rotate;

    }

    public double getPowerRed(double degrees) {
        return Math.sin((degrees * Math.PI / 180) - Math.PI / 4);
    }

    public double getPowerBlue(double degrees) {
        return Math.sin((degrees * Math.PI / 180) + Math.PI / 4);
    }
}
