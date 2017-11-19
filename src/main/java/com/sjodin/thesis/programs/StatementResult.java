package com.sjodin.thesis.programs;

import com.sjodin.thesis.components.DualNumber;

public class StatementResult {

    private DualNumber resultValue;
    private ImperativeProgramState<DualNumber> resultingState;

    public StatementResult(DualNumber resultValue, ImperativeProgramState<DualNumber> resultingState) {
        this.resultValue = resultValue;
        this.resultingState = resultingState;
    }

    public boolean hasResultValue() {
        return resultValue != null;
    }

    public DualNumber getResultValue() {
        return resultValue;
    }

    public ImperativeProgramState<DualNumber> getResultingState() {
        return resultingState;
    }
}
