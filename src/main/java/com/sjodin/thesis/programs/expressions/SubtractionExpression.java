package com.sjodin.thesis.programs.expressions;

import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.ExpressionTree;

// Represents the output of one expression being subtracted from the output of another (immutable)
public class SubtractionExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public SubtractionExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(ImperativeProgramState<DualNumber> state) {
        return left.run(state).subtract(right.run(state));
    }
}
