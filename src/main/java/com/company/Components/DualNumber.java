package com.company.Components;

// represents dual numbers (value and gradient at point)
public class DualNumber {

    private double value;
    private double epsilonCoefficient;

    public DualNumber(Double value, Double epsilonCoefficient) {
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
        return new DualNumber(value, (proportion*epsilonCoefficient) + ((1-proportion)*n.getEpsilonCoefficient()));
    }

    @Override
    public String toString() {
        return "Dual Number:  " + value + " + " + epsilonCoefficient + "epsilon";
    }

    public void set(Tuple2<Double, Double> value) {
        this.value = value.first;
        this.epsilonCoefficient = value.second;
    }

    public Tuple2<Double, Double> get() {
        return new Tuple2<Double, Double>(value, epsilonCoefficient);
    }

    public DualNumber add(DualNumber n) {
        return new DualNumber(value + n.get().first, epsilonCoefficient + n.get().second);
    }

    public DualNumber subtract(DualNumber n) {
        return new DualNumber(value - n.get().first, epsilonCoefficient - n.get().second);
    }

    public DualNumber multiply(DualNumber n) {
        return new DualNumber(value * n.get().first, (value * n.get().second) + (epsilonCoefficient * n.get().first));
    }

    public DualNumber divide(DualNumber n) {
        return multiply(new DualNumber((1/n.get().first), (1/n.get().second)));
    }
}
