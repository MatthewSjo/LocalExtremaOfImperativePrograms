package com.company.Optimisation;

import com.company.Components.State;
import com.company.Statements.StatementTree;
import com.company.Components.DualNumber;

import java.util.Arrays;

// A basic version of gradient descent.
// Unused, but useful for reference.
// Useless for programs containing if statements.
public class GradientDescentBasic implements GradientDescent<Double[]> {

    private StatementTree program;
    private int parameters;
    private double gamma;
    private double startPoint;

    // "gamma" is our step size
    public GradientDescentBasic(StatementTree program, int parameters, double gamma, double startPoint) {
        this.program = program;
        this.parameters = parameters;
        this.gamma = gamma;
        this.startPoint = startPoint;
    }

    // used for necessary vector arithmetic
    private double amountOfChange(Double[] oldVector, Double[] newVector) {
        double sum = 0;
        for (int i = 0; i < parameters; i++) {
            sum += Math.abs(newVector[i] - oldVector[i]);
        }
        return sum;
    }

    @Override
    public void giveProgram(StatementTree program, int parameters, Double[] startPoint) {
        this.program = program;
        this.parameters = parameters;
        this.startPoint = startPoint[0];
    }

    @Override
    public Double[] findMinimum(double precision) {
        Double[] oldVector = new Double[parameters]; // don't fail on first loop
        Arrays.fill(oldVector, startPoint - 5);
        Double[] newVector = new Double[parameters];
        Arrays.fill(newVector, startPoint);

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
                newVector[i] = oldVector[i] - (gamma * program.run(state, (x -> 1.0), (x -> 1.0), 0.0).first.get().getEpsilonCoefficient());
            }
        }
        return newVector;
    }

    @Override
    public Double[] findMaximum(double precision) {
        Double[] oldVector = new Double[parameters]; // don't fail on first loop
        Arrays.fill(oldVector, startPoint - 5);
        Double[] newVector = new Double[parameters];
        Arrays.fill(newVector, startPoint);

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
                newVector[i] = oldVector[i] + (gamma * program.run(state, (x -> 1.0), (x -> 1.0), 0.0).first.get().getEpsilonCoefficient());
            }
        }
        return newVector;
    }
}
