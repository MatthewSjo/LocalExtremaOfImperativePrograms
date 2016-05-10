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

    // the boolean return represents whether any tests failed (returns true if all successful)
    // the purpose of this function is to test that optimisation works as it should with our actual implementation
    public static boolean TestBasicFunctionality(List<OptimisationTest> programsToTest, double precision, double startingPoint) {
        int totalRuns = 0;
        List<Tuple2<String, Double[]>> failures = new ArrayList<>();
        for (OptimisationTest test : programsToTest) {
            ProgramTracker tracker = new ProgramTracker(test.getStatementTree());
            Double[] startingValues = new Double[test.getExpectedAnswer().length];
            java.util.Arrays.fill(startingValues, startingPoint);
            GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(tracker, test.getExpectedAnswer().length, gamma, startingValues, range, coolingSpeed);

            Double[] results = new Double[]{};
            if (test.isMaximisation()) {
                results = algorithm.findMaximum(precision);
            } else {
                results = algorithm.findMinimum(precision);
            }

            // detect any failures
            for (int i = 0; i < results.length; i++) {
                if (Math.abs(results[i] - test.getExpectedAnswer()[i]) >= precision) {
                    failures.add(new Tuple2<>(test.getTestName(), results));
                }
            }

            totalRuns += tracker.TimesRun();
        }

        // output results
        System.out.println("Average number of program runs per test:  " + (totalRuns/programsToTest.size()));
        // output failures
        if (failures.size() > 0) {
            System.out.println("There were failures!  The following tests failed:");
            for (Tuple2<String, Double[]> failure : failures) {
                System.out.println(failure.first);
                System.out.print("Results:  ");
                for (int i = 0; i < failure.second.length; i++) {
                    System.out.print(i + " - " + failure.second[i] + " ; ");
                }
                System.out.println();
            }
        }

        // return true if all tests successful, false otherwise
        return failures.size() == 0;
    }

    // return average runs and number of failures for some arbitrary tests
    private static Tuple2<Integer, Integer> AdvancedTest(List<OptimisationTest> programsToTest, GradientDescent<Double[]> algorithmVariant,
                                                  double precision, double startingPoint) {
        int totalRuns = 0;
        List<Tuple2<String, Double[]>> failures = new ArrayList<>();
        for (OptimisationTest test : programsToTest) {
            ProgramTracker tracker = new ProgramTracker(test.getStatementTree());
            Double[] startingValues = new Double[test.getExpectedAnswer().length];
            java.util.Arrays.fill(startingValues, startingPoint);
            algorithmVariant.giveProgram(tracker, test.getExpectedAnswer().length, startingValues);

            Double[] results = new Double[]{};
            if (test.isMaximisation()) {
                results = algorithmVariant.findMaximum(precision);
            } else {
                results = algorithmVariant.findMinimum(precision);
            }

            for (int i = 0; i < results.length; i++) {
                if (Math.abs(results[i] - test.getExpectedAnswer()[i]) >= precision) {
                    failures.add(new Tuple2<>(test.getTestName(), results));
                }
            }

            totalRuns += tracker.TimesRun();
        }

        // return average runs and number of failures
        return new Tuple2<>((totalRuns/programsToTest.size()), failures.size());
    }

    // tests how many runs are required for a variety of precisions
    public static void experimentWithStandardImplementation(List<OptimisationTest> programsToTest, double precision[], double startingPoint) {
        GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(null, 0, gamma, new Double[]{}, range, coolingSpeed);
        for (int i = 0; i < precision.length; i++) {
            System.out.println("Test precision " + precision[i]);
            Tuple2<Integer, Integer> results = AdvancedTest(programsToTest, algorithm, precision[i], startingPoint);
            System.out.println("Average runs:  " + results.first);
            System.out.println("Failures:  " + results.second);
        }
    }

    // test how number of runs varies for different starting points and ranges
    public static void testStartingPointEffect(List<OptimisationTest> programsToTest, double precision, double startingPoint[], double startingRange[]) {
        for (double range: startingRange) {
            for (double startPoint : startingPoint) {
                System.out.println("range = " + range + ", starting point = " + startPoint);
                GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(null, 0, gamma, new Double[]{}, range, coolingSpeed);
                Tuple2<Integer, Integer> results = AdvancedTest(programsToTest, algorithm, precision, startPoint);
                System.out.println("Average runs:  " + results.first);
                System.out.println("Failures:  " + results.second);
            }
        }
    }

    // test the difference having no outer loop makes for an array of starting points
    public static void testNoOuterLoop(List<OptimisationTest> programsToTest, double precision, double startingPoint[]) {
        for (double startPoint : startingPoint) {
            System.out.println("starting point = " + startPoint);
            VariantInstantFullPrecision algorithm = new VariantInstantFullPrecision(null, 0, gamma, new Double[]{});
            Tuple2<Integer, Integer> results = AdvancedTest(programsToTest, algorithm, precision, startPoint);
            System.out.println("Average runs:  " + results.first);
            System.out.println("Failures:  " + results.second);
        }
    }

    // test what happens when we calculate inner precision differently
    public static void testInnerPrecisionVariants(List<OptimisationTest> programsToTest, double precision[], double startingPoint) {
        VariantInnerPrecision algorithm = new VariantInnerPrecision(null, 0, gamma, new Double[]{}, range, coolingSpeed);
        for (int i = 0; i < precision.length; i++) {
            System.out.println("Test precision " + precision[i]);
            Tuple2<Integer, Integer> results = AdvancedTest(programsToTest, algorithm, precision[i], startingPoint);
            System.out.println("Average runs:  " + results.first);
            System.out.println("Failures:  " + results.second);
        }
    }

    // test the difference cooling speed makes for a variety of precisions
    public static void testCoolingSpeed(List<OptimisationTest> programsToTest, double[] precisions, double[] coolingSpeeds) {
        for (double precision: precisions) {
            for (double coolingSpeed : coolingSpeeds) {
                System.out.println("Precision:  " + precision + ", Cooling speed:  " + coolingSpeed);
                GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(null, 0, gamma, new Double[]{}, range, coolingSpeed);
                Tuple2<Integer, Integer> results = AdvancedTest(programsToTest, algorithm, precision, 5);
                System.out.println("Average runs:  " + results.first);
                System.out.println("Failures:  " + results.second);
            }
        }
    }
}
