package com.sjodin.thesis.expressions;

import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.DualNumber;

// Represents the output of two other expressions being multiplied together (immutable)
public class MultiplicationExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public MultiplicationExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(State<DualNumber> state) {
        return left.run(state).multiply(right.run(state));
    }
}
