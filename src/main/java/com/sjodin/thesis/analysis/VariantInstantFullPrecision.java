package com.sjodin.thesis.analysis;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.optimisation.GradientDescent;
import com.sjodin.thesis.optimisation.GradientDescentWithSmoothing;
import com.sjodin.thesis.statements.StatementTree;

// A variant without an outer loop; simply goes instantly to full precision
public class VariantInstantFullPrecision implements GradientDescent<Double[]> {

    private StatementTree program;
    private int parameters;
    private double gamma;
    private Double[] startPoint;

    // "gamma" is our step size
    public VariantInstantFullPrecision(double gamma) {
        this.gamma = gamma;
    }

    private Double smoother(BranchValues branchValues, double expAdjust, double smoothingRange) {
        if (branchValues.point < 0) {
            return branchValues.falseGradient + ((branchValues.trueGradient - (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient + (expAdjust * branchValues.falseResult)) * Math.exp(-expAdjust * (smoothingRange - Math.abs(branchValues.point))));
        } else {
            return branchValues.falseGradient + ((branchValues.trueGradient + (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient - (expAdjust * branchValues.falseResult)) * Math.exp(expAdjust * (branchValues.point - smoothingRange)));
        }
    }

    private Double equalitySmoother(BranchValues branchValues, double smoothingRange) {
        double dist = Math.abs(branchValues.point);
        double first = dist * dist * branchValues.falseGradient;
        double second = 2 * dist * branchValues.falseResult;
        double third = smoothingRange * smoothingRange * branchValues.trueGradient;
        double fourth = dist * dist * branchValues.trueGradient;
        double fifth = 2 * dist * branchValues.trueResult;
        return (first + second + third - fourth - fifth)/(smoothingRange * smoothingRange);
    }

    // no outer loop, immediately full precision
    private Double[] findOptimal(double precision, boolean maximise) {
        Double[] parameterVector = startPoint;
        double expConstant = -Math.log(0.5);
        double T = precision/2;
        GradientDescentWithSmoothing innerDescent = new GradientDescentWithSmoothing(program, parameters);
        double expAdjust = expConstant/T;
        double effectiveGamma = gamma/expAdjust;
        final double smoothingRange = T;
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