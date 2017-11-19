package com.sjodin.thesis.programs.expressions;

import com.sjodin.thesis.programs.ImperativeProgramState;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.ExpressionTree;

// Gets a value from the state (i.e. gets the value of a variable) (immutable)
public class GetValueExpression implements ExpressionTree {

    private int index;

    public GetValueExpression(int index) {
        this.index = index;
    }

    public DualNumber run(ImperativeProgramState<DualNumber> state) {
        return state.get(index);
    }
}
