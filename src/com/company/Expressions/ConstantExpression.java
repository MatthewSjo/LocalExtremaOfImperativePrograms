package com.company.Expressions;

import com.company.Components.State;
import com.company.Components.DualNumber;

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
