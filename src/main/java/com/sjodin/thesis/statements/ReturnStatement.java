package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.expressions.ExpressionTree;

import java.util.Optional;
import java.util.function.Function;

// returns the output of an expression (immutable)
public class ReturnStatement implements StatementTree {

    private ExpressionTree expression;

    public ReturnStatement(ExpressionTree expression) {
        this.expression = expression;
    }

    @Override
    public StatementResult run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new StatementResult(expression.run(state), state);
    }
}
