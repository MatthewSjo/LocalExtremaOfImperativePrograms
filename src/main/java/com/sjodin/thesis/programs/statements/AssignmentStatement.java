package com.sjodin.thesis.programs.statements;

import com.sjodin.thesis.components.*;
import com.sjodin.thesis.programs.ExpressionTree;
import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.programs.StatementResult;
import com.sjodin.thesis.programs.StatementTree;

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
    public StatementResult run(ImperativeProgramState<DualNumber> state, Function<BranchValues, Double> smoother,
                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        state.put(index, expression.run(state));
        return nextStatement.run(state, smoother, equalitySmoother, smoothingRange);
    }
}
