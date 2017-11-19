package com.sjodin.thesis.algorithm;

import com.sjodin.thesis.programs.StatementTree;

// An interface for Gradient Descent over some kind of number (dual numbers, generally).
// Can find minimum or maximum, as well as giving a program to optimise
public interface GradientDescent<T> {

    T findMinimum(double precision);

    T findMaximum(double precision);

    void giveProgram(StatementTree program, int parameters, Double[] startPoint);
}
