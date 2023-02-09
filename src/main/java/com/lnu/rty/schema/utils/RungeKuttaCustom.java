package com.lnu.rty.schema.utils;

import flanagan.integration.DerivnFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class RungeKuttaCustom {

    private static int nStepsMultiplier = 1000;     // multiplied by number of steps to give internally calculated
    private static double safetyFactor = 0.9;       // safety factor for Runge Kutta Fehlberg and CashKarp tolerance checks as
    ;                // final value of x
    // error estimations are uncertain
    private static double incrementFactor = -0.2;   // factor used in calculating a step size increment in Fehlberg and CashKarp procedures
    private static double decrementFactor = -0.25;  // factor used in calculating a step size decrement in Fehlberg and CashKarp procedures
    private int x0 = Integer.MAX_VALUE;                 // initial value of x
    private int xn = Integer.MAX_VALUE;
    private double y0 = Double.NaN;                 // initial value of y; single ODE
    private double[] yy0 = null;                    // initial values of y; multiple ODEs
    private int nODE = 0;                           // number of ODEs
    private double step = Double.NaN;               // step size
    private double relTol = 1.0e-5;                 // tolerance multiplicative factor in adaptive step methods
    //   maximum allowed iterations in adaptive step methods
    private double absTol = 1.0e-3;                 // tolerance additive factor to ensure non-zeto tolerance in adaptive step methods
    private int maxIter = -1;                       // maximum iterations allowed in adaptive step methods
    private int nIter = 0;                          // number of iterations taken

    public RungeKuttaCustom() {
    }

    public static void resetNstepsMultiplier(int multiplier) {
        RungeKuttaCustom.nStepsMultiplier = multiplier;
    }

    public void setInitialValueOfX(int x0) {
        this.x0 = x0;
    }

    public void setFinalValueOfX(int xn) {
        this.xn = xn;
    }

    public void setInitialValuesOfY(double[] yy0) {
        this.yy0 = yy0;
        this.nODE = yy0.length;
        if (this.nODE == 1) this.y0 = yy0[0];
    }

    public void setStepSize(double step) {
        this.step = step;
    }

    public void setToleranceScalingFactor(double relTol) {
        this.relTol = relTol;
    }

    public void setToleranceAdditionFactor(double absTol) {
        this.absTol = absTol;
    }

    public void setMaximumIterations(int maxIter) {
        this.maxIter = maxIter;
    }

    public int getNumberOfIterations() {
        return this.nIter;
    }

    // Fourth order Runge-Kutta for n (nODE) ordinary differential equations (ODE)
    // Non-static method
    public List<FourthOrderValue> fourthOrder(DerivnFunction g) {
        if (Double.isNaN(this.x0)) throw new IllegalArgumentException("No initial x value has been entered");
        if (Double.isNaN(this.xn)) throw new IllegalArgumentException("No final x value has been entered");
        if (this.yy0 == null) throw new IllegalArgumentException("No initial y values have been entered");
        if (Double.isNaN(this.step)) throw new IllegalArgumentException("No step size has been entered");

        double[] k1 = new double[this.nODE];
        double[] k2 = new double[this.nODE];
        double[] k3 = new double[this.nODE];
        double[] k4 = new double[this.nODE];
        double[] y = new double[this.nODE];
        double[] yd = new double[this.nODE];
        double[] dydx = new double[this.nODE];
        int x = 0;

        // Calculate nsteps
        double ns = (this.xn - this.x0) / this.step;
        ns = Math.rint(ns);
        int nsteps = (int) ns;
        this.nIter = nsteps;

        // initialise
        System.arraycopy(this.yy0, 0, y, 0, this.nODE);

        List<FourthOrderValue> result = new ArrayList<>();

        // iteration over allowed steps
        int point = 0;
        for (int j = this.x0; j <= this.xn; j += this.step) {
            x = j;
            dydx = g.derivn(x, y);
            for (int i = 0; i < this.nODE; i++) k1[i] = this.step * dydx[i];

            for (int i = 0; i < this.nODE; i++) yd[i] = y[i] + k1[i] / 2;
            dydx = g.derivn(x + this.step / 2, yd);
            for (int i = 0; i < this.nODE; i++) k2[i] = this.step * dydx[i];

            for (int i = 0; i < this.nODE; i++) yd[i] = y[i] + k2[i] / 2;
            dydx = g.derivn(x + this.step / 2, yd);
            for (int i = 0; i < this.nODE; i++) k3[i] = this.step * dydx[i];

            for (int i = 0; i < this.nODE; i++) yd[i] = y[i] + k3[i];
            dydx = g.derivn(x + this.step, yd);
            for (int i = 0; i < this.nODE; i++) k4[i] = this.step * dydx[i];

            var fourthOrderValue = FourthOrderValue.instance(point, x);
            result.add(fourthOrderValue);
            fourthOrderValue.values = new double[this.nODE];
            for (int i = 0; i < this.nODE; i++) {
                double v = k1[i] / 6 + k2[i] / 3 + k3[i] / 3 + k4[i] / 6;
                y[i] += v;
                fourthOrderValue.values[i] = y[i];
            }
            point++;
        }

        return result;
    }

    @RequiredArgsConstructor(staticName = "instance")
    @Getter
    public static class FourthOrderValue {
        private final int point;
        private final int factor;
        private double[] values;
    }

}
