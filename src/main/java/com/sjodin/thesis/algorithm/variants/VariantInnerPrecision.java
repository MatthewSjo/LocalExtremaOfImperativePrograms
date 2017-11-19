package com.sjodin.thesis.algorithm.variants;

import com.sjodin.thesis.algorithm.GradientDescentWithAnnealing;
import com.sjodin.thesis.algorithm.GradientDescentWithSmoothing;

// a variant that calculates inner precision differently (in findOptimal)
public class VariantInnerPrecision extends GradientDescentWithAnnealing {

    public VariantInnerPrecision(double gamma, double range, double coolingSpeed) {
        super(gamma, range, coolingSpeed);
    }

    @Override
    protected Double[] optimiseAtOnePrecisionLevel(double T, GradientDescentWithSmoothing innerDescent,
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
}
