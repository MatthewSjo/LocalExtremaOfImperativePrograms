package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.Tuple2;
import com.sjodin.thesis.components.DualNumber;

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
