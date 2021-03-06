package com.sjodin.thesis.programs.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.programs.StatementResult;
import com.sjodin.thesis.programs.StatementTree;

import java.util.Map;
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

    @Override
    public StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        // first find which branch was taken, and find results of branch taken or not
        ConditionResult conditionResults = condition.run(state);
        boolean takeTrueBranch = conditionResults.isConditionPassed();
        double displacementFromBranchPoint = conditionResults.getHowCloseToPassing();
        ImperativeProgramState<DualNumber> takenState = state.copy();
        ImperativeProgramState<DualNumber> notTakenState = state.copy();
        StatementResult takenBranch =
                takeTrueBranch ? trueBranch.run(takenState, smoother, equalitySmoother, smoothingRange)
                        : falseBranch.run(takenState, smoother, equalitySmoother, smoothingRange);
        StatementResult notTakenBranch =
                takeTrueBranch ? falseBranch.run(notTakenState, smoother, equalitySmoother, smoothingRange)
                        : trueBranch.run(notTakenState, smoother, equalitySmoother, smoothingRange);

        // interpolate if within the smoothing range of this iteration
        if (Math.abs(displacementFromBranchPoint) <= smoothingRange) {
            // if any of the branches don't themselves return, run the rest of the program with them
            if (!takenBranch.hasResultValue()) {
                takenBranch = nextStatement.run(takenBranch.getResultingState(), smoother, equalitySmoother, smoothingRange);
            }
            if (!notTakenBranch.hasResultValue()) {
                notTakenBranch = nextStatement.run(notTakenBranch.getResultingState(), smoother, equalitySmoother, smoothingRange);
            }

            // Separate case for equality test, as need to smooth very differently
            if (condition.isEqualityTest()) {
                return spliceBranchesOfEqualityConditionIfStatement(takeTrueBranch, takenBranch, notTakenBranch, equalitySmoother, smoothingRange, displacementFromBranchPoint);
            } else {
                return spliceBranchesOfBoundaryConditionIfStatement(smoother, displacementFromBranchPoint, takenBranch, notTakenBranch);
            }
        } else {
            return outOfSmoothingRangeSoNoInterpolation(smoother, equalitySmoother, smoothingRange, takenBranch);
        }
    }

    private StatementResult spliceBranchesOfEqualityConditionIfStatement(boolean takeTrueBranch, StatementResult takenBranch, StatementResult notTakenBranch,
                                                                         Function<BranchValues, Double> equalitySmoother, Double smoothingRange,
                                                                         double displacementFromBranchPoint) {
        // if at the point of equality, just return the case where it is equal
        if (takeTrueBranch) {
            return takenBranch;
        } else {
            double proportion = Math.pow(1.0 - (Math.abs(displacementFromBranchPoint) / ((double) smoothingRange)), 2);
            double splicedGradient = equalitySmoother.apply(new BranchValues(displacementFromBranchPoint, takenBranch.getResultValue().getValue(), takenBranch.getResultValue().getEpsilonCoefficient(),
                    notTakenBranch.getResultValue().getValue(), notTakenBranch.getResultValue().getEpsilonCoefficient()));
            return new StatementResult(
                    new DualNumber(takenBranch.getResultValue().getValue(), splicedGradient),
                    spliceStatesWithSmoothing(takenBranch.getResultingState(), notTakenBranch.getResultingState(), proportion));
        }
    }

    private StatementResult spliceBranchesOfBoundaryConditionIfStatement(Function<BranchValues, Double> smoother, double displacementFromBranchPoint, StatementResult takenBranch, StatementResult notTakenBranch) {
        double splicedGradient = smoother.apply(new BranchValues(displacementFromBranchPoint, takenBranch.getResultValue().getValue(), takenBranch.getResultValue().getEpsilonCoefficient(),
                notTakenBranch.getResultValue().getValue(), notTakenBranch.getResultValue().getEpsilonCoefficient()));
        return new StatementResult(
                new DualNumber(takenBranch.getResultValue().getValue(), splicedGradient),
                spliceStatesWithSmoothing(takenBranch.getResultingState(), notTakenBranch.getResultingState(), 1.0));
    }

    private StatementResult outOfSmoothingRangeSoNoInterpolation(Function<BranchValues, Double> smoother, Function<BranchValues, Double> equalitySmoother, Double smoothingRange, StatementResult takenBranch) {
        if (takenBranch.hasResultValue()) {
            // if this branch returns, return
            return takenBranch;
        } else {
            // if not returning from this branch, update state and move on
            return nextStatement.run(takenBranch.getResultingState(), smoother, equalitySmoother, smoothingRange);
        }
    }

    // first one corresponds to "true" branch; smooths values between states
    private ImperativeProgramState<DualNumber> spliceStatesWithSmoothing(ImperativeProgramState<DualNumber> x, ImperativeProgramState<DualNumber> y, double proportion) {
        ImperativeProgramState<DualNumber> state = new ImperativeProgramState<DualNumber>();
        for (Map.Entry<Integer, DualNumber> entry : x.getAll()) {
            if (y.contains(entry.getKey())) {
                state.put(entry.getKey(), entry.getValue().spliceCoefficients(y.get(entry.getKey()), proportion));
            } else {
                state.put(entry.getKey(), entry.getValue());
            }
        }
        return state;
    }
}
