package com.sjodin.thesis.analysis;

import com.sjodin.thesis.programs.StatementTree;

// immutable POJO for storing all of the information about a test (more than just the program itself)
public class OptimisationTest {
    // for debugging purposes, need to know names of tests
    private String testName;

    // true for maximisation, false for minimisation
    private boolean isMaximisation;

    // array of doubles representing the expected answer for each parameter
    private double[] expectedAnswer;

    // the actual program we intend to test
    private StatementTree statementTree;

    public OptimisationTest(String name, boolean isMaximisation, double[] expectedAnswer, StatementTree statementTree) {
        this.testName = name;
        this.isMaximisation = isMaximisation;
        this.expectedAnswer = expectedAnswer;
        this.statementTree = statementTree;
    }

    public boolean isMaximisation() {
        return isMaximisation;
    }

    public double[] getExpectedAnswer() {
        return expectedAnswer;
    }

    public StatementTree getStatementTree() {
        return statementTree;
    }

    public String toString() {
        return testName;
    }
}
