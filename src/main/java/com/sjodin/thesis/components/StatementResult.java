package com.sjodin.thesis.components;

public class StatementResult {

    private DualNumber resultValue;
    private State<DualNumber> resultingState;

    public StatementResult(DualNumber resultValue, State<DualNumber> resultingState) {
        this.resultValue = resultValue;
        this.resultingState = resultingState;
    }

    public boolean hasResultValue() {
        return resultValue != null;
    }

    public DualNumber getResultValue() {
        return resultValue;
    }

    public State<DualNumber> getResultingState() {
        return resultingState;
    }
}
