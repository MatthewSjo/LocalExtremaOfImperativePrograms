package com.company.Analysis;

import com.company.Optimisation.GradientDescent;
import com.company.Optimisation.GradientDescentWithAnnealing;
import com.company.Statements.ProgramTracker;
import com.company.Components.Tuple2;

import java.util.ArrayList;
import java.util.List;

// provides a number of statically invoked tests (class essentially static)
public final class TestingSuite {

    // these are the default values that will be used throughout; subject to change, so need to be common when testing general use
    private static double gamma = 0.5;
    private static double range = 0.1;
    private static double coolingSpeed = 0.01;

    // private constructor; make class effectively static
    private TestingSuite() {}

    // tests how many runs are required for a variety of precisions
    public static void experimentWithStandardImplementation(List<OptimisationTest> programsToTest, double precision[], double startingPoint) {
        GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
        for (int i = 0; i < precision.length; i++) {
            System.out.println("Test precision " + precision[i]);
            advancedTest(programsToTest, algorithm, precision[i], startingPoint);
        }
    }

    // test how number of runs varies for different starting points and ranges
    public static void testStartingPointEffect(List<OptimisationTest> programsToTest, double precision, double startingPoint[], double startingRange[]) {
        for (double range: startingRange) {
            for (double startPoint : startingPoint) {
                System.out.println("range = " + range + ", starting point = " + startPoint);
                GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
                advancedTest(programsToTest, algorithm, precision, startPoint);
            }
        }
    }

    // test the difference having no outer loop makes for an array of starting points
    public static void testNoOuterLoop(List<OptimisationTest> programsToTest, double precision, double startingPoint[]) {
        for (double startPoint : startingPoint) {
            System.out.println("starting point = " + startPoint);
            VariantInstantFullPrecision algorithm = new VariantInstantFullPrecision(gamma);
            advancedTest(programsToTest, algorithm, precision, startPoint);
        }
    }

    // test what happens when we calculate inner precision differently
    public static void testInnerPrecisionVariants(List<OptimisationTest> programsToTest, double precision[], double startingPoint) {
        VariantInnerPrecision algorithm = new VariantInnerPrecision(gamma, range, coolingSpeed);
        for (int i = 0; i < precision.length; i++) {
            System.out.println("Test precision " + precision[i]);
            advancedTest(programsToTest, algorithm, precision[i], startingPoint);
        }
    }

    // test the difference cooling speed makes for a variety of precisions
    public static void testCoolingSpeed(List<OptimisationTest> programsToTest, double[] precisions, double[] coolingSpeeds) {
        for (double precision: precisions) {
            for (double coolingSpeed : coolingSpeeds) {
                System.out.println("Precision:  " + precision + ", Cooling speed:  " + coolingSpeed);
                GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
                advancedTest(programsToTest, algorithm, precision, 5);
            }
        }
    }

    // return number of failures
    private static void advancedTest(List<OptimisationTest> programsToTest, GradientDescent<Double[]> algorithmVariant,
                                    double precision, double startingPoint) {
        int totalRuns = 0;
        List<Tuple2<String, Double[]>> failures = new ArrayList<>();
        for (OptimisationTest test : programsToTest) {
            ProgramTracker tracker = new ProgramTracker(test.getStatementTree());
            Double[] startingValues = new Double[test.getExpectedAnswer().length];
            java.util.Arrays.fill(startingValues, startingPoint);
            algorithmVariant.giveProgram(tracker, test.getExpectedAnswer().length, startingValues);

            Double[] results;
            if (test.isMaximisation()) {
                results = algorithmVariant.findMaximum(precision);
            } else {
                results = algorithmVariant.findMinimum(precision);
            }

            for (int i = 0; i < results.length; i++) {
                assert Math.abs(results[i] - test.getExpectedAnswer()[i]) < precision;
                if (Math.abs(results[i] - test.getExpectedAnswer()[i]) >= precision) {
                    failures.add(new Tuple2<>(test.getTestName(), results));
                }
            }

            totalRuns += tracker.TimesRun();
        }

        System.out.println("Average runs:  " + (totalRuns/programsToTest.size()));
        System.out.println("Failures:  " + failures.size());
    }
}
