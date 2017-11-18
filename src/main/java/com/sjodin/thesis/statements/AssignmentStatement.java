package com.sjodin.thesis.statements;

import com.sjodin.thesis.components.BranchValues;
import com.sjodin.thesis.expressions.ExpressionTree;
import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.Tuple2;
import com.sjodin.thesis.components.DualNumber;

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
    public Tuple2<Optional<DualNumber>, State<DualNumber>> run(State<DualNumber> state, Function<BranchValues, Double> smoother,
                                                               Function<BranchValues, Double> equalitySmoother, Double smoothingRange) {
        state.put(index, expression.run(state));
        return nextStatement.run(state, smoother, equalitySmoother, smoothingRange);
    }
}
