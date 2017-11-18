package com.company.Components;

public class Tuple2<T,U> {
    public final T first;
    public final U second;

    @Override
    public String toString() {
        return "(" + first.toString() + "," + second.toString() + ")";
    }

    public Tuple2 (T first, U second) {
        this.first = first;
        this.second = second;
    }
}
