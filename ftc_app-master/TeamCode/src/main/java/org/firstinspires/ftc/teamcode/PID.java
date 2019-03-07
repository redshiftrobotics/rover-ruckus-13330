package org.firstinspires.ftc.teamcode;

public class PID {

    public double getError(double currentValue, double goalValue, double steepness) {

        double percentage = currentValue / goalValue;

        return (1- Math.pow(percentage, 1 / steepness));
    }


    double integral;
    double lastProportional = 0;

    public double getSeekValue(double seekValue, double currentValue, double pCoeff, double iCoeff, double dCoeff) {

        double proportional = seekValue - currentValue;

        double derivative = (proportional - lastProportional);
        integral += proportional;
        lastProportional = proportional;

        //This is the actual PID formula. This gives us the value that is returned

        return pCoeff * proportional + iCoeff * integral + dCoeff * derivative;
    }
}
