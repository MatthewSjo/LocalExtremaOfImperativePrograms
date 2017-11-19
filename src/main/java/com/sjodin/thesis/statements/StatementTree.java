package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.*;

import java.util.Optional;
import java.util.function.Function;

// Interface for statements (programs or sub-programs).
// They require smoothing functions and current state
public interface StatementTree {
    StatementResult run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                        Function<BranchValues, Double> equalitySmoother, Double smoothingRange);
}
