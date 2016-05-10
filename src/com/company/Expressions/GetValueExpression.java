package com.company.Expressions;

import com.company.Components.State;
import com.company.Components.DualNumber;

// Gets a value from the state (i.e. gets the value of a variable) (immutable)
public class GetValueExpression implements ExpressionTree {

    private int index;

    public GetValueExpression(int index) {
        this.index = index;
    }

    public DualNumber run(State<DualNumber> state) {
        return state.get(index);
    }
}
