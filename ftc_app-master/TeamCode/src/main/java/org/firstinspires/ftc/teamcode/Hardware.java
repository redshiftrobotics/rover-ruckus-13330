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

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;


/**
 * The non-chassis specific hardware initializations.
 */

public class Hardware { // Here we get the DcMotors from the REV hub and assign their names.

    public DcMotor frontRightMotor;
    public DcMotor frontLeftMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;

    public DcMotor collectorHinge;
    public DcMotor extenderWheel;

    public Servo mineralKicker1;
    public Servo mineralKicker2;
    public DcMotor lifter;
    public DcMotor flipper;

    public Servo topFlipL;
    public Servo topFlipR;

    public Servo depositServo;

    public CRServo vexL;
    public CRServo vexR;




    public CameraName webcam;

    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;


    public Hardware(OpMode context) { // this class gets all the motors, sensors, and imu and hooks it up to the hardware map.


        collectorHinge = context.hardwareMap.dcMotor.get("collectorHinge");
        collectorHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        collectorHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        collectorHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lifter = context.hardwareMap.dcMotor.get("lifter");
        lifter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flipper = context.hardwareMap.dcMotor.get("flipper");
        flipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        vexL = context.hardwareMap.get(CRServo.class, "vexL");
        vexR = context.hardwareMap.get(CRServo.class, "vexR");

        topFlipL = context.hardwareMap.get(Servo.class, "topFlipL");
        topFlipR = context.hardwareMap.get(Servo.class, "topFlipR");

        extenderWheel = context.hardwareMap.dcMotor.get("extenderWheel");
        extenderWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        depositServo = context.hardwareMap.servo.get("depositServo");


        //endregion

        //region other
        webcam = context.hardwareMap.get(CameraName.class, "Webcam 1");

        //limitSwitch = context.hardwareMap.get(TouchSensor.class, "limitSwitch");
        //endregion


    }
}