package com.company;

import com.company.Analysis.OptimisationTest;
import com.company.Analysis.TestPrograms;
import com.company.Analysis.TestingSuite;

import java.util.*;

// Main - for interacting with the system and testing it
public class Main {

    public static void main(String[] args) {
        // main can be used for running the tests by which we analyse the implementation
        // see the "TestingSuite" class to find more examples of tests to be run
        List<OptimisationTest> testPrograms = new TestPrograms().testPrograms();

        // this runs the normal version of the algorithm on 29 standard testing programs at a number of precisions,
        // with a starting point of 5 (increase to start further from local maxima and minima)
        TestingSuite.experimentWithStandardImplementation(testPrograms, new double[]{0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001, 0.00000001}, 5);

        System.out.println();

        // here's a function that shows the impact of having no outer loop
        System.out.println("Implementation with no outer loop:");
        TestingSuite.testNoOuterLoop(testPrograms, 0.001, new double[]{5.0});
        TestingSuite.testNoOuterLoop(testPrograms, 0.0001, new double[]{5.0});
    }
}
