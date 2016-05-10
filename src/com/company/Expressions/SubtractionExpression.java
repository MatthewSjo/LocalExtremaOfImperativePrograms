package com.company.Expressions;

import com.company.Components.State;
import com.company.Components.DualNumber;

// Represents the output of one expression being subtracted from the output of another (immutable)
public class SubtractionExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public SubtractionExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(State<DualNumber> state) {
        return left.run(state).subtract(right.run(state));
    }
}
