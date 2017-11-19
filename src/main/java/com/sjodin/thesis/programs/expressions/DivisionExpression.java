package com.sjodin.thesis.programs.expressions;

import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.ExpressionTree;

// Represents division of one expression by another (immutable)
public class DivisionExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public DivisionExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(ImperativeProgramState<DualNumber> state) {
        return left.run(state).divide(right.run(state));
    }
}
