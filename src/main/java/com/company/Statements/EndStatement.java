package com.company.Statements;

import com.company.Components.BranchValues;
import com.company.Components.State;
import com.company.Components.Tuple2;
import com.company.Components.DualNumber;

import java.util.Optional;
import java.util.function.Function;

// The end of a branch with no return (immutable)
public class EndStatement implements StatementTree {

    @Override
    public Tuple2<Optional<DualNumber>, State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new Tuple2<Optional<DualNumber>, State<DualNumber>>(Optional.empty(), state);
    }
}
