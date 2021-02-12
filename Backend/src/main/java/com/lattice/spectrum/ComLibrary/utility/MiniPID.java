/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 6/6/19 2:36 PM
 * Modified : 6/6/19 2:36 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.utility;

/**
 * @deprecated
 */
public class MiniPID {

    private double errSum, lastErr;
    private final double kp;
    private final double ki;
    private final double kd;

    public MiniPID(double Kp, double Ki, double Kd) {
        kp = Kp;
        ki = Ki;
        kd = Kd;
    }

    public double getOutput(double input, double setPoint, double timeChange) {
        /*Compute all the working error variables*/
        double error = setPoint - input;
        errSum += (error * timeChange);
        double dErr = (error - lastErr) / timeChange;
        /*Compute PID Output*/
        /*working variables*/
        double output = kp * error + ki * errSum + kd * dErr;
        /*Remember some variables for next time*/
        lastErr = error;
        return output;
    }
}