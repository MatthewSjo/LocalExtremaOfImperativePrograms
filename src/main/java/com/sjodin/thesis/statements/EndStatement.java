package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.*;

import java.util.Optional;
import java.util.function.Function;

// The end of a branch with no return (immutable)
public class EndStatement implements StatementTree {

    @Override
    public StatementResult run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new StatementResult(null, state);
    }
}
