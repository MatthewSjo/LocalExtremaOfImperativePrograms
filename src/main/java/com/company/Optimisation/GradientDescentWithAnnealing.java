package com.company.Optimisation;

import com.company.Components.BranchValues;
import com.company.Statements.StatementTree;

// our actual implementation of the algorithm
public class GradientDescentWithAnnealing implements GradientDescent<Double[]> {

    private StatementTree program;
    private int parameters;
    private double gamma;
    private Double[] startPoint;
    private double range;
    private double coolingSpeed;

    // "gamma" is our step size
    public GradientDescentWithAnnealing(double gamma, double range, double coolingSpeed) {
        this.gamma = gamma;
        this.range = range;
        this.coolingSpeed = coolingSpeed;
    }

    // the method by which we smooth boundary conditions
    private Double smoother(BranchValues branchValues, double expAdjust, double smoothingRange) {
        if (branchValues.point < 0) {
            return branchValues.falseGradient + ((branchValues.trueGradient - (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient + (expAdjust * branchValues.falseResult)) * Math.exp(-expAdjust * (smoothingRange - Math.abs(branchValues.point))));
        } else {
            return branchValues.falseGradient + ((branchValues.trueGradient + (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient - (expAdjust * branchValues.falseResult)) * Math.exp(expAdjust * (branchValues.point - smoothingRange)));
        }
    }

    // the method by which we smooth equality conditions
    private Double equalitySmoother(BranchValues branchValues, double smoothingRange) {
        double dist = Math.abs(branchValues.point);
        double first = dist * dist * branchValues.falseGradient;
        double second = 2 * dist * branchValues.falseResult;
        double third = smoothingRange * smoothingRange * branchValues.trueGradient;
        double fourth = dist * dist * branchValues.trueGradient;
        double fifth = 2 * dist * branchValues.trueResult;
        return (first + second + third - fourth - fifth)/(smoothingRange * smoothingRange);
    }

    // finds maximum or minimum (parametrised by "maximise")
    private Double[] findOptimal(double precision, boolean maximise) {
        Double[] parameterVector = startPoint;
        double expConstant = Math.log(2);
        double T = range;
        GradientDescentWithSmoothing innerDescent = new GradientDescentWithSmoothing(program, parameters);
        boolean done = false;
        while (!done) {
            double expAdjust = expConstant/T;
            // need to have smaller gamma to deal with finer interpolation
            double effectiveGamma = gamma/expAdjust;
            // need this to give generic functions effectively final constants
            final double smoothingRange = T;
            // call actual gradient descent using this range (and precision)
            if (maximise) {
                parameterVector = innerDescent.findMaximum(effectiveGamma*precision, effectiveGamma,
                        x -> smoother(x, expAdjust, smoothingRange),
                        x -> equalitySmoother(x, smoothingRange),
                        T, parameterVector);
            } else {
                parameterVector = innerDescent.findMinimum(effectiveGamma*precision, effectiveGamma,
                        x -> smoother(x, expAdjust, smoothingRange),
                        x -> equalitySmoother(x, smoothingRange),
                        T, parameterVector);
            }
            // once interpolation is over a small enough range, we can finish
            if (T < precision/2) {
                done = true;
            }
            // next iteration will have smaller ranges of interpolation
            T = T*coolingSpeed;
        }
        return parameterVector;
    }

    @Override
    public void giveProgram(StatementTree program, int parameters, Double startPoint[]) {
        this.program = program;
        this.parameters = parameters;
        this.startPoint = startPoint;
    }

    @Override
    public Double[] findMinimum(double precision) {
        return findOptimal(precision, false);
    }

    @Override
    public Double[] findMaximum(double precision) {
        return findOptimal(precision, true);
    }
}
