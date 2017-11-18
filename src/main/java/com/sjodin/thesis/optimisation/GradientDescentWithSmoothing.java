package com.sjodin.thesis.optimisation;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.components.State;
import com.sjodin.thesis.statements.StatementTree;
import com.sjodin.thesis.components.DualNumber;

import java.util.Arrays;
import java.util.function.Function;

// Actual gradient descent (the inner gradient descent of our algorithm).
// Not using GradientDescent interface as relies on smoothing functions
public class GradientDescentWithSmoothing {

    private static final double GAMMA_COOLDOWN = 0.65;
    private static final int MAXIMUM_JUMP_DISTANCE = 5;

    private StatementTree program;
    private int parameters;

    // "gamma" is our step size
    public GradientDescentWithSmoothing(StatementTree program, int parameters) {
        this.program = program;
        this.parameters = parameters;
    }

    // Used for necessary vector arithmetic
    private double amountOfChange(Double[] oldVector, Double[] newVector) {
        double sum = 0;
        for (int i = 0; i < parameters; i++) {
            sum += Math.abs(newVector[i] - oldVector[i]);
        }
        return sum;
    }

    public Double[] findMinimum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint) {
        return findOptimum(precision, gamma, smoother, equalitySmoother, smoothingRange, startPoint, false);
    }

    public Double[] findMaximum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint) {
        return findOptimum(precision, gamma, smoother, equalitySmoother, smoothingRange, startPoint, true);
    }

    // Common logic for maximum and minimum (with boolean to differentiate between the two)
    private Double[] findOptimum(double precision, double gamma, Function<BranchValues, Double> smoother,
                                 Function<BranchValues, Double> equalitySmoother, Double smoothingRange, Double[] startPoint, boolean maximise) {
        Double[] oldVector = new Double[parameters]; // don't fail on first loop

        // pretend there's some changes happening so we don't assume we've already found an optimum
        Arrays.fill(oldVector, startPoint[0] - 5);
        Double[] newVector = startPoint;

        // Need effective gamma for separate parameters.
        // Could have minima/maxima in very different places
        Double[] effectiveGamma = new Double[parameters];
        Arrays.fill(effectiveGamma, gamma);

        Boolean[] direction = new Boolean[parameters];
        Arrays.fill(direction, true);

        while (amountOfChange(oldVector, newVector) > precision) {
            // Use last value for each parameter
            System.arraycopy(newVector, 0, oldVector, 0, parameters);
            // Have to update for each parameter
            for (int i = 0; i < parameters; i++) {
                // State is all the parameters we have at this point, with this one as the one we differentiate
                State<DualNumber> state = initialiseStateToDifferentiateSpecificParameter(oldVector, i);

                // Now perform the actual change for this parameter
                double change = calculateChangeForParameter(effectiveGamma[i], state, smoother, equalitySmoother, smoothingRange);

                if (shouldChangeDirection(direction[i], change)) {
                    direction[i] = !direction[i];
                    // Reduce step size when changing direction, to deal with sharp optima
                    effectiveGamma[i] *= GAMMA_COOLDOWN;
                }

                // Direction based on which kind of optimisation
                newVector[i] = maximise ? oldVector[i] + change : oldVector[i] - change;
            }
        }
        return newVector;
    }

    private State<DualNumber> initialiseStateToDifferentiateSpecificParameter(Double[] oldVector, int i) {
        State<DualNumber> state = new State<DualNumber>();
        for (int j = 0; j < parameters; j++) {
            state.put(j, new DualNumber(oldVector[j], 0.0));
        }
        state.put(i, new DualNumber(oldVector[i], 1.0));
        return state;
    }

    private double calculateChangeForParameter(double effectiveGamma, State<DualNumber> state, Function<BranchValues, Double> smoother,
                                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        double change = (effectiveGamma * program.run(state, smoother, equalitySmoother, smoothingRange).first.get().getEpsilonCoefficient());

        // Interpolation leads to very high gradients over tiny ranges, so limit jumps
        if (change > 0) {
            change = Math.min(change, MAXIMUM_JUMP_DISTANCE * effectiveGamma);
        } else {
            change = Math.max(change, MAXIMUM_JUMP_DISTANCE * -1 * effectiveGamma);
        }

        return change;
    }

    private boolean shouldChangeDirection(boolean direction, double change) {
        return (change > 0 && direction) || (change < 0 && !direction);
    }
}
