package com.sjodin.thesis.expressions;

import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.DualNumber;

// Represents a constant number used in the program (not a variable) (immutable)
public class ConstantExpression implements ExpressionTree {

    private DualNumber value;

    public ConstantExpression(DualNumber value) {
        this.value = value;
    }

    public DualNumber run(State<DualNumber> state) {
        return value;
    }
}
