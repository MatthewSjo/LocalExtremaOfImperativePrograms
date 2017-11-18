package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.Tuple2;
import com.sjodin.thesis.components.DualNumber;

import java.util.Optional;
import java.util.function.Function;

// interface for statements (programs or sub-programs)
// they require smoothing functions and current state
// outputs results and new state
public interface StatementTree {
    Tuple2<Optional<DualNumber>,State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                       Function<BranchValues, Double> equalitySmoother, Double smoothingRange);
}
