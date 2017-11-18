package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.expressions.ExpressionTree;
import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.Tuple2;
import com.sjodin.thesis.components.DualNumber;

import java.util.Optional;
import java.util.function.Function;

// returns the output of an expression (immutable)
public class ReturnStatement implements StatementTree {

    private ExpressionTree expression;

    public ReturnStatement(ExpressionTree expression) {
        this.expression = expression;
    }

    @Override
    public Tuple2<Optional<DualNumber>, State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new Tuple2<Optional<DualNumber>, State<DualNumber>>(Optional.of(expression.run(state)), state);
    }
}
