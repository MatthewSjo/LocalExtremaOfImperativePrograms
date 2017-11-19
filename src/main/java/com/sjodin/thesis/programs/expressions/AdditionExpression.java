package com.sjodin.thesis.programs.expressions;

import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.ExpressionTree;

// Represents the output of two other expressions being added together (immutable)
public class AdditionExpression implements ExpressionTree {

    private ExpressionTree left;
    private ExpressionTree right;

    public AdditionExpression(ExpressionTree left, ExpressionTree right) {
        this.left = left;
        this.right = right;
    }

    public DualNumber run(ImperativeProgramState<DualNumber> state) {
        return left.run(state).add(right.run(state));
    }
}
