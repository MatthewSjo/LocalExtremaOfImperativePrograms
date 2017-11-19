package com.sjodin.thesis.components;

public class ConditionResult {
    private boolean conditionPassed;
    private double howCloseToPassing;

    public ConditionResult(boolean conditionPassed, double howCloseToPassing) {
        this.conditionPassed = conditionPassed;
        this.howCloseToPassing = howCloseToPassing;
    }

    public boolean isConditionPassed() {
        return conditionPassed;
    }

    public double getHowCloseToPassing() {
        return howCloseToPassing;
    }
}
