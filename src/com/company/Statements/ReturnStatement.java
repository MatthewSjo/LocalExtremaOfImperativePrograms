package com.company.Statements;

import com.company.Components.BranchValues;
import com.company.Expressions.ExpressionTree;
import com.company.Components.State;
import com.company.Components.Tuple2;
import com.company.Components.DualNumber;

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
