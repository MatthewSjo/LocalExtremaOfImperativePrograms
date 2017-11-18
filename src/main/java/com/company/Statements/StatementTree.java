package com.company.Statements;

import com.company.Components.BranchValues;
import com.company.Components.State;
import com.company.Components.Tuple2;
import com.company.Components.DualNumber;

import java.util.Optional;
import java.util.function.Function;

// interface for statements (programs or sub-programs)
// they require smoothing functions and current state
// outputs results and new state
public interface StatementTree {
    Tuple2<Optional<DualNumber>,State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                       Function<BranchValues, Double> equalitySmoother, Double smoothingRange);
}
