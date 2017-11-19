package com.sjodin.thesis.optimisation;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.statements.StatementTree;

// our actual implementation of the algorithm
public class GradientDescentWithAnnealing implements GradientDescent<Double[]> {

    private static final double EXP_CONSTANT = -Math.log(0.5);

    protected StatementTree program;
    protected int parameters;
    private double gamma;
    protected Double[] startPoint;
    private double range;
    private double coolingSpeed;

    // "gamma" is our step size
    public GradientDescentWithAnnealing(double gamma, double range, double coolingSpeed) {
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

    // the method by which we smooth boundary conditions
    private Double smoother(BranchValues branchValues, double expAdjust, double smoothingRange) {
        if (branchValues.weighting < 0) {
            return branchValues.falseGradient + ((branchValues.trueGradient - (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient + (expAdjust * branchValues.falseResult)) * Math.exp(-expAdjust * (smoothingRange - Math.abs(branchValues.weighting))));
        } else {
            return branchValues.falseGradient + ((branchValues.trueGradient + (expAdjust * branchValues.trueResult)
                    - branchValues.falseGradient - (expAdjust * branchValues.falseResult)) * Math.exp(expAdjust * (branchValues.weighting - smoothingRange)));
        }
    }

    // the method by which we smooth equality conditions
    private Double equalitySmoother(BranchValues branchValues, double smoothingRange) {
        double dist = Math.abs(branchValues.weighting);
        double first = dist * dist * branchValues.falseGradient;
        double second = 2 * dist * branchValues.falseResult;
        double third = smoothingRange * smoothingRange * branchValues.trueGradient;
        double fourth = dist * dist * branchValues.trueGradient;
        double fifth = 2 * dist * branchValues.trueResult;
        return (first + second + third - fourth - fifth)/(smoothingRange * smoothingRange);
    }

    // finds maximum or minimum (parametrised by "maximise")
    protected Double[] findOptimal(double precision, boolean maximise) {
        Double[] parameterVector = startPoint;
        double T = range;
        GradientDescentWithSmoothing innerDescent = new GradientDescentWithSmoothing(program, parameters);
        boolean done = false;
        while (!done) {
            parameterVector = optimiseAtOnePrecisionLevel(T, innerDescent, precision, maximise, parameterVector);

            done = temperatureLowEnoughToFinish(T, precision);

            // next iteration will have smaller ranges of interpolation
            T = T*coolingSpeed;
        }
        return parameterVector;
    }

    protected Double[] optimiseAtOnePrecisionLevel(double T, GradientDescentWithSmoothing innerDescent,
                                                 double precision, boolean maximise, Double[] parameterVector) {
        // the actual difference to normal
        double expAdjust = EXP_CONSTANT/T;
        double effectiveGamma = gamma/expAdjust;
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
