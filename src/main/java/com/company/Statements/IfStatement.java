package com.company.Statements;

import com.company.Components.BranchValues;
import com.company.Components.ConditionWithDistance;
import com.company.Components.State;
import com.company.Components.Tuple2;
import com.company.Components.DualNumber;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

// represents an if statement with a condition, two branches and what comes after the branches (rest of the program)
public class IfStatement implements StatementTree {

    private ConditionWithDistance condition;
    private StatementTree trueBranch;
    private StatementTree falseBranch;
    private StatementTree nextStatement;

    // Constructor (immutable)
    public IfStatement(ConditionWithDistance condition, StatementTree trueBranch, StatementTree falseBranch, StatementTree nextStatement) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.nextStatement = nextStatement;
    }

    // first one corresponds to "true" branch; smooths values between states
    private State<DualNumber> spliceStatesWithSmoothing(State<DualNumber> x, State<DualNumber> y, double proportion) {
        State<DualNumber> state = new State<DualNumber>();
        for (Map.Entry<Integer, DualNumber> entry : x.getAll()) {
            if (y.contains(entry.getKey())) {
                state.put(entry.getKey(), entry.getValue().spliceCoefficients(y.get(entry.getKey()), proportion));
            } else {
                state.put(entry.getKey(), entry.getValue());
            }
        }
        return state;
    }

    @Override
    public Tuple2<Optional<DualNumber>, State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        // first find which branch was taken, and find results of branch taken or not
        Tuple2<Boolean, Double> conditionResults = condition.run(state);
        boolean takeTrueBranch = conditionResults.first;
        double point = conditionResults.second;
        State<DualNumber> takenState = state.copy();
        State<DualNumber> notTakenState = state.copy();
        Tuple2<Optional<DualNumber>, State<DualNumber>> takenBranch =
                takeTrueBranch ? trueBranch.run(takenState, smoother, equalitySmoother, smoothingRange)
                        : falseBranch.run(takenState, smoother, equalitySmoother, smoothingRange);
        Tuple2<Optional<DualNumber>, State<DualNumber>> notTakenBranch =
                takeTrueBranch ? falseBranch.run(notTakenState, smoother, equalitySmoother, smoothingRange)
                        : trueBranch.run(notTakenState, smoother, equalitySmoother, smoothingRange);

        // interpolate if within the smoothing range of this iteration
        if (Math.abs(point) <= smoothingRange) {
            // if any of the branches don't themselves return, run the rest of the program with them
            if (!takenBranch.first.isPresent()) {
                takenBranch = nextStatement.run(takenBranch.second, smoother, equalitySmoother, smoothingRange);
            }
            if (!notTakenBranch.first.isPresent()) {
                notTakenBranch = nextStatement.run(notTakenBranch.second, smoother, equalitySmoother, smoothingRange);
            }
            // Separate case for equality test, as need to smooth very differently
            if (condition.isEqualityTest()) {
                // if at the point of equality, just return the case where it is equal
                if (takeTrueBranch) {
                    return takenBranch;
                } else {
                    double proportion = Math.pow(1.0 - (Math.abs(point) / ((double) smoothingRange)), 2);
                    double splicedGradient = equalitySmoother.apply(new BranchValues(point, takenBranch.first.get().getValue(), takenBranch.first.get().getEpsilonCoefficient(),
                            notTakenBranch.first.get().getValue(), notTakenBranch.first.get().getEpsilonCoefficient()));
                    return new Tuple2<Optional<DualNumber>, State<DualNumber>>(
                            Optional.of(new DualNumber(takenBranch.first.get().getValue(), splicedGradient)),
                            spliceStatesWithSmoothing(takenBranch.second, notTakenBranch.second, proportion));
                }
            // boundary case, not equality case
            } else {
                // splice values each return
                double splicedGradient = smoother.apply(new BranchValues(point, takenBranch.first.get().getValue(), takenBranch.first.get().getEpsilonCoefficient(),
                        notTakenBranch.first.get().getValue(), notTakenBranch.first.get().getEpsilonCoefficient()));
                return new Tuple2<Optional<DualNumber>, State<DualNumber>>(
                        Optional.of(new DualNumber(takenBranch.first.get().getValue(), splicedGradient)),
                        spliceStatesWithSmoothing(takenBranch.second, notTakenBranch.second, 1.0));
            }
        // if not in smoothing range - no interpolation
        } else {
            if (takenBranch.first.isPresent()) {
                // if this branch returns, return
                return takenBranch;
            } else {
                // if not returning from this branch, update state and move on
                return nextStatement.run(takenBranch.second, smoother, equalitySmoother, smoothingRange);
            }
        }

    }
}
