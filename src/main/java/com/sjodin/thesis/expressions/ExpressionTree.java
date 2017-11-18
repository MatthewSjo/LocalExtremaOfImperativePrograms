package com.sjodin.thesis.expressions;

import com.sjodin.thesis.components.State;
import com.sjodin.thesis.components.DualNumber;

// Interface for expressions
// expressions return values (DualNumbers)
public interface ExpressionTree {
    // Evaluates an expression on a state, returning a dual number
    DualNumber run(State<DualNumber> state);
}
