package com.sjodin.thesis.programs;

import com.sjodin.thesis.components.*;

import java.util.function.Function;

// Interface for statements (programs or sub-programs).
// They require smoothing functions and current state
public interface StatementTree {
    StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                        Function<BranchValues, Double> equalitySmoother, Double smoothingRange);
}
