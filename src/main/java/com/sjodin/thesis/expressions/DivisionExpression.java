package com.sjodin.thesis.expressions;

import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.DualNumber;

// Represents division of one expression by another (immutable)
public class DivisionExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public DivisionExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(State<DualNumber> state) {
        return left.run(state).divide(right.run(state));
    }
}
