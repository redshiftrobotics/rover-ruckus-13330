package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.robotcore.external.navigation.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Hardware { // Here we get the DcMotors from the REV hub and assign their names.

    public DcMotor front_right_motor;
    public DcMotor front_left_motor;
    public DcMotor back_right_motor;
    public DcMotor back_left_motor;
    public BNO055IMU imu;
    public Orientation oldAngle = new Orientation();
    public Orientation angles = new Orientation();


    public double globalAngle;
    public double correction;

    public Hardware(OpMode ctx) {

        imu = ctx.hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();

        imuParameters.mode                = BNO055IMU.SensorMode.IMU;
        imuParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled      = false;

        imu.initialize(imuParameters);

        front_left_motor = ctx.hardwareMap.dcMotor.get("front_left_motor");
        back_left_motor = ctx.hardwareMap.dcMotor.get("back_left_motor");

        front_right_motor = ctx.hardwareMap.dcMotor.get("front_right_motor");
        back_right_motor = ctx.hardwareMap.dcMotor.get("back_right_motor");


        front_right_motor.setDirection(DcMotor.Direction.REVERSE);
        back_right_motor.setDirection(DcMotor.Direction.REVERSE);

        front_left_motor.setDirection(DcMotor.Direction.FORWARD);
        back_left_motor.setDirection(DcMotor.Direction.FORWARD);
    }
}
