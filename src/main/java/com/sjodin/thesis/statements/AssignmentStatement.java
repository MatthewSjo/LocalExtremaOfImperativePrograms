package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.expressions.ExpressionTree;

import java.util.Optional;
import java.util.function.Function;

// Assigns the output of an expression to a variable (immutable)
public class AssignmentStatement implements StatementTree {

    private int index;
    private ExpressionTree expression;
    private StatementTree nextStatement;

    public AssignmentStatement(int index, ExpressionTree expression, StatementTree nextStatement) {
        this.index = index;
        this.expression = expression;
        this.nextStatement = nextStatement;
    }

    @Override
    public StatementResult run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        state.put(index, expression.run(state));
        return nextStatement.run(state, smoother, equalitySmoother, smoothingRange);
    }
}
