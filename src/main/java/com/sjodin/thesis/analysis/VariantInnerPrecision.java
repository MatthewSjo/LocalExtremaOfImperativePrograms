package com.sjodin.thesis.analysis;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.optimisation.GradientDescent;
import com.sjodin.thesis.optimisation.GradientDescentWithSmoothing;
import com.sjodin.thesis.statements.StatementTree;

// a variant that calculates inner precision differently (in findOptimal)
public class VariantInnerPrecision implements GradientDescent<Double[]> {

    private static final double EXP_CONSTANT = -Math.log(0.5);

    private StatementTree program;
    private int parameters;
    private double gamma;
    private Double[] startPoint;
    private double range;
    private double coolingSpeed;

    // "gamma" is our step size
    public VariantInnerPrecision(double gamma, double range, double coolingSpeed) {
        this.gamma = gamma;
        this.range = range;
        this.coolingSpeed = coolingSpeed;
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

    private Double smoother(BranchValues branchValues, double expAdjust, double smoothingRange) {
        if (branchValues.weighting < 0) {
            return branchValues.falseGradient + ((branchValues.trueGradient - (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient + (expAdjust * branchValues.falseResult)) * Math.exp(-expAdjust * (smoothingRange - Math.abs(branchValues.weighting))));
        } else {
            return branchValues.falseGradient + ((branchValues.trueGradient + (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient - (expAdjust * branchValues.falseResult)) * Math.exp(expAdjust * (branchValues.weighting - smoothingRange)));
        }
    }

    private Double equalitySmoother(BranchValues branchValues, double smoothingRange) {
        double dist = Math.abs(branchValues.weighting);
        double first = dist * dist * branchValues.falseGradient;
        double second = 2 * dist * branchValues.falseResult;
        double third = smoothingRange * smoothingRange * branchValues.trueGradient;
        double fourth = dist * dist * branchValues.trueGradient;
        double fifth = 2 * dist * branchValues.trueResult;
        return (first + second + third - fourth - fifth)/(smoothingRange * smoothingRange);
    }

    private Double[] findOptimal(double precision, boolean maximise) {
        Double[] parameterVector = startPoint;
        double T = range;
        GradientDescentWithSmoothing innerDescent = new GradientDescentWithSmoothing(program, parameters);
        boolean done = false;
        while (!done) {
            optimiseAtOnePrecisionLevel(T, innerDescent, precision, maximise, parameterVector);

            done = temperatureLowEnoughToFinish(T, precision);

            T = T*coolingSpeed;
        }
        return parameterVector;
    }

    private Double[] optimiseAtOnePrecisionLevel(double T, GradientDescentWithSmoothing innerDescent,
                                                 double precision, boolean maximise, Double[] parameterVector) {
        // the actual difference to normal
        double expAdjust = EXP_CONSTANT/T;
        double effectiveGamma = gamma*T;
        final double smoothingRange = T;
        if (maximise) {
            return innerDescent.findMaximum(effectiveGamma*precision, effectiveGamma,
                    x -> smoother(x, expAdjust, smoothingRange),
                    x -> equalitySmoother(x, smoothingRange),
                    T, parameterVector);
        } else {
            return innerDescent.findMinimum(effectiveGamma*precision, effectiveGamma,
                    x -> smoother(x, expAdjust, smoothingRange),
                    x -> equalitySmoother(x, smoothingRange),
                    T, parameterVector);
        }
    }

    private boolean temperatureLowEnoughToFinish(double T, double precision) {
        return T < precision/2;
    }
}
