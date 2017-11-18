import com.company.Analysis.OptimisationTest
import com.company.Analysis.TestPrograms
import com.company.Analysis.VariantInnerPrecision
import com.company.Analysis.VariantInstantFullPrecision
import com.company.Optimisation.GradientDescent
import com.company.Optimisation.GradientDescentWithAnnealing
import com.company.Statements.ProgramTracker
import spock.lang.Shared
import spock.lang.Specification

class EndToEndFunctionalitySpec extends Specification {

    // these are the default values that will be used throughout; subject to change, so need to be common when testing general use
    private static double gamma = 0.5;
    private static double range = 0.1;
    private static double coolingSpeed = 0.01;

    @Shared List<OptimisationTest> testPrograms;

    def setup() {
        testPrograms = TestPrograms.testPrograms();
    }

    def "It works with very standard parameters" () {
        when:
        GradientDescent algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
        def precision = 0.1;
        def startingPoint = 5;

        then:
        testPrograms.each { program ->
            runOptimisationTest(program, algorithm, precision, startingPoint);
        }
    }

    def "It works with various precisions" () {
        when:
        GradientDescent algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
        double[] precisions = [0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001, 0.00000001]
        def startingPoint = 5;

        then:
        testPrograms.each { program ->
            precisions.each { precision ->
                runOptimisationTest(program, algorithm, precision, startingPoint)

            }
        }
    }

    def "It works with various starting points" () {
        when:
        double precision = 0.1
        double[] startingPoints = [5, 6, 10, 15]
        double[] startingRanges = [0.05, 0.1, 0.15]

        then:
        testPrograms.each { program ->
            startingPoints.each { startingPoint ->
                startingRanges.each {startingRange ->
                    GradientDescent algorithm = new GradientDescentWithAnnealing(gamma, startingRange, coolingSpeed);
                    runOptimisationTest(program, algorithm, precision, startingPoint)
                }
            }
        }
    }

    def "It works with various cooling speeds" () {
        when:
        double precision = 0.1
        double[] coolingSpeeds = [0.1, 0.01, 0.001]
        def startingPoint = 5;

        then:
        testPrograms.each { program ->
            coolingSpeeds.each { coolingSpeed ->
                GradientDescent algorithm = new GradientDescentWithAnnealing(gamma, range, coolingSpeed);
                runOptimisationTest(program, algorithm, precision, startingPoint)
            }
        }
    }

    def "It works with instant full precision" () {
        when:
        GradientDescent algorithm = new VariantInstantFullPrecision(gamma);
        def precision = 0.1;
        def startingPoint = 5;

        then:
        testPrograms.each { program ->
            runOptimisationTest(program, algorithm, precision, startingPoint);
        }
    }

    def "It works with a different method of calculating inner precision" () {
        when:
        GradientDescent algorithm = new VariantInnerPrecision(gamma, range, coolingSpeed);
        def precision = 0.1;
        def startingPoint = 5;

        then:
        testPrograms.each { program ->
            runOptimisationTest(program, algorithm, precision, startingPoint);
        }
    }

    private static void runOptimisationTest(OptimisationTest test, GradientDescent<Double[]> algorithmVariant,
                                            double precision, double startingPoint) {
        ProgramTracker tracker = new ProgramTracker(test.getStatementTree());
        Double[] startingValues = new Double[test.getExpectedAnswer().length];
        Arrays.fill(startingValues, startingPoint);
        algorithmVariant.giveProgram(tracker, test.getExpectedAnswer().length, startingValues);

        Double[] results;
        if (test.isMaximisation()) {
            results = algorithmVariant.findMaximum(precision);
        } else {
            results = algorithmVariant.findMinimum(precision);
        }

        // detect any failures
        for (int i = 0; i < results.length; i++) {
            assert Math.abs(results[i] - test.getExpectedAnswer()[i]) < precision
        }
    }

}
