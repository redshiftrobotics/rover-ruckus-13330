package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;


import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Hardware { // Here we get the DcMotors from the REV hub and assign their names.

    public DcMotor front_right_motor; // motor initialization
    public DcMotor front_left_motor;
    public DcMotor back_right_motor;
    public DcMotor back_left_motor;

    public ColorSensor color_sensor_1; // sensor initialization.

    public BNO055IMU imu; // imu initialization.
    public Orientation oldAngle = new Orientation();
    public Orientation angles = new Orientation();


    public double globalAngle;
    public double correction;

    //encoders!

    public int ENCODER_TICKS = 1400;
    public double GEAR_RATIO = 1;
    public double WHEEL_RADIUS = 10;

    public Hardware(OpMode context) { // this class gets all the motors, sensors, and imu and hooks it up to the hardware map.

        imu = context.hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();

        imuParameters.mode                = BNO055IMU.SensorMode.IMU;
        imuParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled      = false;

        imu.initialize(imuParameters);

        front_left_motor = context.hardwareMap.dcMotor.get("front_left_motor");
        back_left_motor = context.hardwareMap.dcMotor.get("back_left_motor");

        front_right_motor = context.hardwareMap.dcMotor.get("front_right_motor");
        back_right_motor = context.hardwareMap.dcMotor.get("back_right_motor");

        color_sensor_1 = context.hardwareMap.colorSensor.get("color_sensor_1");

        front_right_motor.setDirection(DcMotor.Direction.REVERSE);
        back_right_motor.setDirection(DcMotor.Direction.REVERSE);

        front_left_motor.setDirection(DcMotor.Direction.FORWARD);
        back_left_motor.setDirection(DcMotor.Direction.FORWARD);
    }
}
