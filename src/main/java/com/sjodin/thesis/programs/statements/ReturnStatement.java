package com.sjodin.thesis.programs.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.programs.ExpressionTree;
import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.programs.StatementResult;
import com.sjodin.thesis.programs.StatementTree;

import java.util.function.Function;

// returns the output of an expression (immutable)
public class ReturnStatement implements StatementTree {

    private ExpressionTree expression;

    public ReturnStatement(ExpressionTree expression) {
        this.expression = expression;
    }

    @Override
    public StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        return new StatementResult(expression.run(state), state);
    }
}
