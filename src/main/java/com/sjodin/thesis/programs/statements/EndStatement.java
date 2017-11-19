package com.sjodin.thesis.programs.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.programs.StatementResult;
import com.sjodin.thesis.programs.StatementTree;

import java.util.function.Function;

// The end of a branch with no return (immutable)
public class EndStatement implements StatementTree {

    @Override
    public StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new StatementResult(null, state);
    }
}
