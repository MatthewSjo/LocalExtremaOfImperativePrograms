package com.company.Components;

// for storing what comes out of 2 branches for the purposes of splicing (immutable POJO)
public class BranchValues {
    public final double point;
    public final double trueResult;
    public final double trueGradient;
    public final double falseResult;
    public final double falseGradient;

    @Override
    public String toString() {
        return "BranchValues:  (point:  " + point + ", trueResult:  " + trueResult + ", trueGradient:  " + trueGradient
                + ", falseResult:  " + falseResult + ", falseGradient:   " + falseGradient + ")";
    }

    public BranchValues (double point, double trueResult, double trueGradient, double falseResult, double falseGradient) {
        this.point = point;
        this.trueResult = trueResult;
        this.trueGradient = trueGradient;
        this.falseResult = falseResult;
        this.falseGradient = falseGradient;
    }
}
