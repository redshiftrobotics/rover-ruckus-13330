# Teamcode

This is where we keep all our code for the robot. Currently we have five classes:

### Robot
* We keep the main functions of the robot
	* Drive functions
	* PID Controllers
	* IMU Parameters
	* VuForia Parameters
### Hardware
* This is where the hardware for the robot is stored
	* Example: 
	
	```	
	front_left_motor = ctx.hardwareMap.dcMotor.get("front_left_motor");
	back_left_motor = ctx.hardwareMap.dcMotor.get("back_left_motor");

	front_right_motor = ctx.hardwareMap.dcMotor.get("front_right_motor");
	back_right_motor = ctx.hardwareMap.dcMotor.get("back_right_motor");


	front_right_motor.setDirection(DcMotor.Direction.REVERSE);
	back_right_motor.setDirection(DcMotor.Direction.REVERSE);

	front_left_motor.setDirection(DcMotor.Direction.FORWARD);
	back_left_motor.setDirection(DcMotor.Direction.FORWARD);
	```

### Console
* In this class we keep the console (Telemetry) commands to make it easy to
	* Update
	* Display information quickly
	* Keep our classes orginized

### TeleOp
* Controls motors and servos
	* Input by the drivers

### Autonomous
* Controls motors and servos
	* Input by AI / sensors
