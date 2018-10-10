package org.firstinspires.ftc.teamcode;



public class ASAMController {
    /**
     * Compute the motor power for a given timestamp along an ASAM curve
     * @param totalRunTime total time allocated to the movement
     * @param elapsedTime time elapsed since the movement began
     * @param startSpeed starting speed of the movement
     * @param endSpeed ending speed of the movement
     * @param accelTime amount of time to accelerate/decelerate
     * @return
     */
    public static double computeMotorPower(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {
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
     * @see ASAMController#computeMotorPower
     * @param totalRunTime total move time
     * @param elapsedTime total elapsed time
     * @param startSpeed starting speed
     * @param endSpeed ending speed
     * @param accelTime amount of time to accelerate
     * @return current motor power
     */
    private static double computeAcceleration(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {

        return (
                ((startSpeed - endSpeed) / 2)
                        * Math.cos((elapsedTime * Math.PI) / accelTime)
                        + ((startSpeed + endSpeed) / 2)
        );
    }

    /**
     * Compute the deceleration section of the curve
     *
     * @see ASAMController#computeMotorPower
     * @param totalRunTime total move time
     * @param elapsedTime total elapsed time
     * @param startSpeed starting speed
     * @param endSpeed ending speed
     * @param accelTime amount of time to deceleration
     * @return current motor power
     */
    private static double computeDeceleration(long totalRunTime, long elapsedTime, float startSpeed, float endSpeed, long accelTime) {

        return (
                ((endSpeed - startSpeed) / 2)
                        * Math.cos(
                        (Math.PI * (totalRunTime - accelTime - elapsedTime)) / accelTime
                )
                        + ((startSpeed + endSpeed) / 2)
        );
    }
}
