package com.sjodin.thesis.programs.expressions;

import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.ExpressionTree;

// Represents a constant number used in the program (not a variable) (immutable)
public class ConstantExpression implements ExpressionTree {

    private DualNumber value;

    public ConstantExpression(DualNumber value) {
        this.value = value;
    }

    public DualNumber run(ImperativeProgramState<DualNumber> state) {
        return value;
    }
}
