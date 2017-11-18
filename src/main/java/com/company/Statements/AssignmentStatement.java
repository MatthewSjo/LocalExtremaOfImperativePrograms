package com.company.Statements;

import com.company.Components.BranchValues;
import com.company.Expressions.ExpressionTree;
import com.company.Components.State;
import com.company.Components.Tuple2;
import com.company.Components.DualNumber;

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
