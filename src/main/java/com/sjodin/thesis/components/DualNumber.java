package com.sjodin.thesis.components;

// represents dual numbers (value and gradient at point)
public class DualNumber {

    private double value;
    private double epsilonCoefficient;

    public DualNumber(double value, double epsilonCoefficient) {
        this.value = value;
        this.epsilonCoefficient = epsilonCoefficient;
    }

    public double getValue() {
        return value;
    }

    public double getEpsilonCoefficient() {
        return epsilonCoefficient;
    }

    public DualNumber spliceCoefficients(DualNumber n, double proportion) {
        return new DualNumber(value, (proportion*epsilonCoefficient) + ((1-proportion)*n.epsilonCoefficient));
    }

    @Override
    public String toString() {
        return "Dual Number:  " + value + " + " + epsilonCoefficient + "epsilon";
    }

    public DualNumber add(DualNumber n) {
        return new DualNumber(value + n.value, epsilonCoefficient + n.epsilonCoefficient);
    }

    public DualNumber subtract(DualNumber n) {
        return new DualNumber(value - n.value, epsilonCoefficient - n.epsilonCoefficient);
    }

    public DualNumber multiply(DualNumber n) {
        return new DualNumber(value * n.value, (value * n.epsilonCoefficient) + (epsilonCoefficient * n.value));
    }

    public DualNumber divide(DualNumber n) {
        return multiply(new DualNumber((1/n.value), (1/n.epsilonCoefficient)));
    }
}
