package com.sjodin.thesis.analysis;

import com.sjodin.thesis.components.ConditionWithDistance;
import com.sjodin.thesis.components.DualNumber;
import com.sjodin.thesis.programs.expressions.*;
import com.sjodin.thesis.programs.statements.AssignmentStatement;
import com.sjodin.thesis.programs.statements.EndStatement;
import com.sjodin.thesis.programs.statements.IfStatement;
import com.sjodin.thesis.programs.statements.ReturnStatement;

import java.util.*;

// provides sets of programs on which to perform analysis of the algorithm
public class TestPrograms {

    // default list of programs to test
    public static List<OptimisationTest> testPrograms() {
        List<OptimisationTest> testingStatements = new ArrayList<>();

        // start with extremely simple examples of maximisation and minimisation
        testingStatements.add(
                new OptimisationTest("minimise basic x squared", false, new double[]{0.0},
                        new AssignmentStatement(0,new MultiplicationExpression(new GetValueExpression(0),new GetValueExpression(0)),
                            new ReturnStatement(new GetValueExpression(0))
                        )));
        testingStatements.add(
                new OptimisationTest("maximise 3 - (x-3)^2", true, new double[]{3.0},
                        new AssignmentStatement(0, new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(3.0, 0.0))),
                        new AssignmentStatement(1, new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)),
                        new AssignmentStatement(2, new SubtractionExpression(new ConstantExpression(new DualNumber(3.0, 0.0)), new GetValueExpression(1)),
                                new ReturnStatement(new GetValueExpression(2))
                        )
                )
        )));
        testingStatements.add(
                new OptimisationTest("maximise 3 - x^2", true, new double[]{0.0},
                        new AssignmentStatement(1, new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)),
                                        new AssignmentStatement(2, new SubtractionExpression(new ConstantExpression(new DualNumber(3.0, 0.0)), new GetValueExpression(1)),
                                                new ReturnStatement(new GetValueExpression(2))
                                        )
                                )
                        ));

        // now give minimisation and maximisation over a number of parameters
        testingStatements.add(
                new OptimisationTest("minimise x*y", false, new double[]{0.0, 0.0},
                        new AssignmentStatement(0,new MultiplicationExpression(new GetValueExpression(0),new GetValueExpression(1)),
                                new ReturnStatement(new GetValueExpression(0))
                        )
                )
        );
        testingStatements.add(
                new OptimisationTest("maximise 3 - ((x - 3)^2 + (y - 4)^2 + (z - 5)^2)", true, new double[]{3.0, 4.0, 5.0},
                        new AssignmentStatement(0, new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(3.0, 0.0))),
                                new AssignmentStatement(0, new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)),
                                        new AssignmentStatement(1, new SubtractionExpression(new GetValueExpression(1), new ConstantExpression(new DualNumber(4.0, 0.0))),
                                                new AssignmentStatement(1, new MultiplicationExpression(new GetValueExpression(1), new GetValueExpression(1)),
                                                        new AssignmentStatement(2, new SubtractionExpression(new GetValueExpression(2), new ConstantExpression(new DualNumber(5.0, 0.0))),
                                                                new AssignmentStatement(2, new MultiplicationExpression(new GetValueExpression(2), new GetValueExpression(2)),
                                                                        new AssignmentStatement(5, new AdditionExpression(new GetValueExpression(0), new AdditionExpression(new GetValueExpression(1), new GetValueExpression(2))),
                                                                                new AssignmentStatement(0, new SubtractionExpression(new ConstantExpression(new DualNumber(3.0, 0.0)), new GetValueExpression(5)),
                                                                                        new ReturnStatement(new GetValueExpression(0))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        ))
        );

        // Now give the simplest if statement (|x|)
        testingStatements.add(
                new OptimisationTest("minimise if x > 0 return x else return -x", false, new double[]{0.0},
                        new IfStatement(ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)), new ReturnStatement(new SubtractionExpression(
                                new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement()))
        );

        // Give the same program as a maximisation mirrored in the x axis
        testingStatements.add(
                new OptimisationTest("maximise if x < 0 return x else return -x", true, new double[]{0.0},
                        new IfStatement(ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement()))
        );

        // Also give |x| with reassignment, rather than returning from within the branches
        testingStatements.add(
                new OptimisationTest("minimise if x > 0 return x else return -x   (reassignment version)", false, new double[]{0.0},
                        new IfStatement(ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new EndStatement(),
                                new AssignmentStatement(0, new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0)), new EndStatement()),
                                new ReturnStatement(new GetValueExpression(0))))
        );
        testingStatements.add(
                new OptimisationTest("maximise if x < 0 return x else return -x   (reassignment version)", true, new double[]{0.0},
                        new IfStatement(ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new EndStatement(),
                                new AssignmentStatement(0, new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0)), new EndStatement()),
                                new ReturnStatement(new GetValueExpression(0))))
        );

        // Give a version of the standard disconnected minimum for each boundary condition type
        testingStatements.add(
                new OptimisationTest("find disconnected minimum using less than", false, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)))),
                                new ReturnStatement(new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected minimum using less than or equal to", false, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.lessThanEqual(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)))),
                                new ReturnStatement(new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected minimum using greater than", false, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected minimum using greater than or equal to", false, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.greaterThanEqual(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)))),
                                new EndStatement()
                        ))
        );

        // Give a version of simple disconnected maximum for each boundary condition type
        testingStatements.add(
                new OptimisationTest("find disconnected maximum using less than", true, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0))))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0)))))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected maximum using less than or equal to", true, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.lessThanEqual(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0))))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0)))))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected maximum using greater than", true, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0)))))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0))))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find disconnected maximum using greater than or equal to", true, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.greaterThanEqual(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new ConstantExpression(new DualNumber(2.0, 0.0)),
                                        new SubtractionExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0)))))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0))))),
                                new EndStatement()
                        ))
        );

        // Give examples of all kinds of disconnect, as minimum or maximum
        // positive up positive
        testingStatements.add(
                new OptimisationTest("find minimum when have to pass through positive gradient with a jump up", false, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement(),
                                new IfStatement(
                                        ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                        new ReturnStatement(new MultiplicationExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0)))),
                                        new ReturnStatement(new GetValueExpression(0)),
                                        new EndStatement()
                                )
                        )
                )
        );
        // positive down positive
        testingStatements.add(
                new OptimisationTest("find minimum in disconnect of positive gradient with a jump down", false, new double[]{2.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement(),
                                new IfStatement(
                                        ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                        new ReturnStatement(new GetValueExpression(0)),
                                        new ReturnStatement(new MultiplicationExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0)))),
                                        new EndStatement()
                                )
                        )
                )
        );
        // positive up negative
        testingStatements.add(
                new OptimisationTest("find maximum in disconnect of positive then negative gradient with a jump up", true, new double[]{2.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement(),
                                new IfStatement(
                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                        new ReturnStatement(new GetValueExpression(0)),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                                new MultiplicationExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))))),
                                        new EndStatement()
                                )
                        )
                )
        );
        // positive down negative
        testingStatements.add(
                new OptimisationTest("find maximum in disconnect of positive then negative gradient with a jump down", true, new double[]{2.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement(),
                                new IfStatement(
                                        ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                        new ReturnStatement(new MultiplicationExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0)))),
                                        new EndStatement()
                                )
                        )
                )
        );
        // negative up positive
        testingStatements.add(
                new OptimisationTest("find minimum in disconnect of negative gradient followed by positive with a jump up", false, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(-1.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement())
                )
        );
        // negative down positive
        testingStatements.add(
                new OptimisationTest("find minimum in disconnect of negative gradient followed by positive with a jump down", false, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.greaterThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(1.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement())
                )
        );
        // negative up negative
        testingStatements.add(
                new OptimisationTest("find maximum at disconnected maximum induced by negative gradient with a jump up in the middle", true, new double[]{2.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(5.0, 0.0)), new GetValueExpression(0))),
                                new EndStatement()
                        ))
        );
        // negative down negative
        testingStatements.add(
                new OptimisationTest("find maximum when it requires passing through region of negative gradient with jumps down", true, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(5.0, 0.0)), new GetValueExpression(0))),
                                new IfStatement(
                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(5.0, 0.0)), new GetValueExpression(0))),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                        new EndStatement()
                                ),
                                new EndStatement()
                        )
                )
        );


        // Give examples of use of equality in both minima and maxima
        testingStatements.add(
                new OptimisationTest("find minimum at equality point in program", false, new double[]{1.0},
                        new IfStatement(ConditionWithDistance.equalTo(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                new ReturnStatement(new MultiplicationExpression(new ConstantExpression(new DualNumber(-2.0, 0.0)),
                                        new GetValueExpression(0))),
                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)),
                                        new MultiplicationExpression(new GetValueExpression(0), new GetValueExpression(0)))),
                                new EndStatement()
                        ))
        );
        testingStatements.add(
                new OptimisationTest("find maximum at equality point in program", true, new double[]{2.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)),
                                new IfStatement(
                                        ConditionWithDistance.equalTo(new ConstantExpression(new DualNumber(2.0, 0.0)), new GetValueExpression(0)),
                                        new ReturnStatement(new MultiplicationExpression(new GetValueExpression(0), new ConstantExpression(new DualNumber(5.0, 0.0)))),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                        new EndStatement()
                                ),
                                new EndStatement()
                        )
                )
        );

        // Give a few examples of conflicting (i.e. close) branches
        // conflicting multiple jumps downwards
        testingStatements.add(
                new OptimisationTest("find maximum with repeated conflicting downward jumps", true, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new GetValueExpression(0)),
                                new IfStatement(
                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(5.0, 0.0)), new GetValueExpression(0))),
                                        new IfStatement(
                                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.5, 0.0))),
                                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(4.0, 0.0)), new GetValueExpression(0))),
                                                new IfStatement(
                                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(3.0, 0.0)), new GetValueExpression(0))),
                                                        new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                                        new EndStatement()
                                                ),
                                                new EndStatement()
                                        ),
                                        new EndStatement()
                                ),
                                new EndStatement()
                        )
                )
        );
        testingStatements.add(
                new OptimisationTest("find minimum with repeated conflicting upward jumps", false, new double[]{0.0},
                        new IfStatement(
                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(0.0, 0.0))),
                                new ReturnStatement(new SubtractionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                new IfStatement(
                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.0, 0.0))),
                                        new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(0.0, 0.0)), new GetValueExpression(0))),
                                        new IfStatement(
                                                ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(1.5, 0.0))),
                                                new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(2.0, 0.0)), new GetValueExpression(0))),
                                                new IfStatement(
                                                        ConditionWithDistance.lessThan(new GetValueExpression(0), new ConstantExpression(new DualNumber(2.0, 0.0))),
                                                        new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(4.0, 0.0)), new GetValueExpression(0))),
                                                        new ReturnStatement(new AdditionExpression(new ConstantExpression(new DualNumber(6.0, 0.0)), new GetValueExpression(0))),
                                                        new EndStatement()
                                                ),
                                                new EndStatement()
                                        ),
                                        new EndStatement()
                                ),
                                new EndStatement()
                        )
                )
        );

        return testingStatements;
    }
}
