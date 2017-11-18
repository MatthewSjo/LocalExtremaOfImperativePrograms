package com.company.Optimisation;

import com.company.Statements.StatementTree;

// An interface for Gradient Descent over some kind of number (dual numbers, generally)
// Can find minimum or maximum, as well as giving a program to optimise
public interface GradientDescent<T> {

    T findMinimum(double precision);

    T findMaximum(double precision);

    void giveProgram(StatementTree program, int parameters, Double[] startPoint);
}
