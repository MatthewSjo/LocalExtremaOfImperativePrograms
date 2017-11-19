package com.sjodin.thesis.algorithm.variants;

import com.sjodin.thesis.algorithm.GradientDescentWithAnnealing;
import com.sjodin.thesis.algorithm.GradientDescentWithSmoothing;

// A variant without an outer loop; simply goes instantly to full precision
public class VariantInstantFullPrecision extends GradientDescentWithAnnealing {

    // "gamma" is our step size
    public VariantInstantFullPrecision(double gamma) {
        super(gamma, 0, 0);
    }

    @Override
    protected Double[] findOptimal(double precision, boolean maximise) {
        Double[] parameterVector = startPoint;
        double T = precision/2;
        GradientDescentWithSmoothing innerDescent = new GradientDescentWithSmoothing(program, parameters);
        return optimiseAtOnePrecisionLevel(T, innerDescent, precision, maximise, parameterVector);
    }
}
