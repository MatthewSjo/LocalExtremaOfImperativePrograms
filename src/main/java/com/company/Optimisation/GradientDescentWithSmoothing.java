package com.company.Optimisation;

import com.company.Components.BranchValues;
import com.company.Components.State;
import com.company.Statements.StatementTree;
import com.company.Components.DualNumber;

import java.util.Arrays;
import java.util.function.Function;

// actual gradient descent (the inner gradient descent of our algorithm)
// not using GradientDescent interface as relies on smoothing functions
public class GradientDescentWithSmoothing {

    private static final double gammaCooldown = 0.65;
    private StatementTree program;
    private int parameters;

    // "gamma" is our step size
    public GradientDescentWithSmoothing(StatementTree program, int parameters) {
        this.program = program;
        this.parameters = parameters;
    }

    // used for necessary vector arithmetic
    private double amountOfChange(Double[] oldVector, Double[] newVector) {
        double sum = 0;
        for (int i = 0; i < parameters; i++) {
            sum += Math.abs(newVector[i] - oldVector[i]);
        }
        return sum;
    }

    // common logic for maximum and minimum (with boolean to differentiate between the two)
    private Double[] findOptimum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                 Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint, boolean maximise) {
        Double[] oldVector = new Double[parameters]; // don't fail on first loop
        Arrays.fill(oldVector, startPoint[0] - 5);
        // not strictly necessary but more explicitly shows use
        Double[] newVector = startPoint;
        // Need effective gamma for separate parameters.
        // Could have minima/maxima in very different places
        Double[] effectiveGamma = new Double[parameters];
        Arrays.fill(effectiveGamma, gamma);
        Boolean[] direction = new Boolean[parameters];
        Arrays.fill(direction, true);

        while (amountOfChange(oldVector, newVector) > precision) {
            // use last value for each parameter
            for (int i = 0; i < parameters; i++) {
                oldVector[i] = newVector[i];
            }
            // have to update for each parameter
            for (int i = 0; i < parameters; i++) {
                // state is all the parameters we have at this point, with this one as the one we differentiate
                State<DualNumber> state = new State<DualNumber>();
                for (int j = 0; j < parameters; j++) {
                    state.put(j, new DualNumber(oldVector[j], 0.0));
                }
                state.put(i, new DualNumber(oldVector[i], 1.0));
                // now perform the actual change for this parameter
                double change = (effectiveGamma[i] * program.run(state, smoother, equalitySmoother, smoothingRange).first.get().getEpsilonCoefficient());
                //interpolation leads to very high gradients over tiny ranges, so limit jumps
                if (change > 0) {
                    change = Math.min(change, 5*effectiveGamma[i]);
                } else {
                    change = Math.max(change, -5*effectiveGamma[i]);
                }
                // reduce step size when changing direction, to deal with sharp optima
                if (change > 0 && direction[i]) {
                    direction[i] = false;
                    effectiveGamma[i] *= gammaCooldown;
                } else if (change < 0 && !direction[i]) {
                    direction[i] = true;
                    effectiveGamma[i] *= gammaCooldown;
                }
                // direction based on which kind of optimisation
                if (maximise) {
                    newVector[i] = oldVector[i] + change;
                } else {
                    newVector[i] = oldVector[i] - change;
                }
            }
        }
        return newVector;
    }

    public Double[] findMinimum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint) {
        return findOptimum(precision, gamma, smoother, equalitySmoother, smoothingRange, startPoint, false);
    }

    public Double[] findMaximum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint) {
        return findOptimum(precision, gamma, smoother, equalitySmoother, smoothingRange, startPoint, true);
    }
}
