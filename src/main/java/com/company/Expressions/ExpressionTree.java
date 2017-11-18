package com.company.Expressions;

import com.company.Components.State;
import com.company.Components.DualNumber;

// Interface for expressions
// Expressions return values (DualNumbers)
public interface ExpressionTree {
    // Evaluates an expression on a state, returning a dual number
    DualNumber run(State<DualNumber> state);
}
