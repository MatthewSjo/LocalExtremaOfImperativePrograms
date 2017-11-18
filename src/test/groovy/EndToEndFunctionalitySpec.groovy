import com.company.Analysis.OptimisationTest
import com.company.Analysis.TestPrograms
import com.company.Components.Tuple2
import com.company.Optimisation.GradientDescentWithAnnealing
import com.company.Statements.ProgramTracker
import spock.lang.Specification

class EndToEndFunctionalitySpec extends Specification {

    // these are the default values that will be used throughout; subject to change, so need to be common when testing general use
    private static double gamma = 0.5;
    private static double range = 0.1;
    private static double coolingSpeed = 0.01;

    def "Test basic functionality"() {
        when:
        def testPrograms = TestPrograms.testPrograms();
        def precision = 0.1;
        def startingPoint = 5;

        then:
        testPrograms.each {
            testBasicFunctionalityOnPrograms(it, precision, startingPoint);
        }
    }

    private static void testBasicFunctionalityOnPrograms(OptimisationTest test, double precision, double startingPoint) {
        ProgramTracker tracker = new ProgramTracker(test.getStatementTree());
        Double[] startingValues = new Double[test.getExpectedAnswer().length];
        Arrays.fill(startingValues, startingPoint);
        GradientDescentWithAnnealing algorithm = new GradientDescentWithAnnealing(tracker, test.getExpectedAnswer().length, gamma, startingValues, range, coolingSpeed);

        Double[] results;
        if (test.isMaximisation()) {
            results = algorithm.findMaximum(precision);
        } else {
            results = algorithm.findMinimum(precision);
        }

        // detect any failures
        for (int i = 0; i < results.length; i++) {
            assert Math.abs(results[i] - test.getExpectedAnswer()[i]) <= precision
        }
    }

}
