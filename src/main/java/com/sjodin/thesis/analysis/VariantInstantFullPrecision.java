package com.sjodin.thesis.analysis;

import com.sjodin.thesis.optimisation.GradientDescentWithAnnealing;
import com.sjodin.thesis.optimisation.GradientDescentWithSmoothing;

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
