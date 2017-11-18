package com.sjodin.thesis.components;

import com.sjodin.thesis.expressions.ExpressionTree;

// Stores the idea of a condition of various kinds.
// Returns not just the result, but how close it was to the other result.
public class ConditionWithDistance {

    private Type type;
    private ExpressionTree firstExpression;
    private ExpressionTree secondExpression;

    private enum Type {
        EQ, // equal to
        GT, // greater than
        LT, // less than
        GTE, // greater than or equal to
        LTE // less than or equal to
    }

    private static ConditionWithDistance generateCondition(Type type, ExpressionTree firstExpression, ExpressionTree secondExpresssion) {
        ConditionWithDistance condition = new ConditionWithDistance();
        condition.type = type;
        condition.firstExpression = firstExpression;
        condition.secondExpression = secondExpresssion;
        return condition;
    }

    public boolean isEqualityTest() {
        return type == Type.EQ;
    }

    public static ConditionWithDistance greaterThan(ExpressionTree firstExpression, ExpressionTree secondExpression) {
        return generateCondition(Type.GT, firstExpression, secondExpression);
    }

    public static ConditionWithDistance lessThan(ExpressionTree firstExpression, ExpressionTree secondExpression) {
        return generateCondition(Type.LT, firstExpression, secondExpression);
    }

    public static ConditionWithDistance greaterThanEqual(ExpressionTree firstExpression, ExpressionTree secondExpression) {
        return generateCondition(Type.GTE, firstExpression, secondExpression);
    }

    public static ConditionWithDistance lessThanEqual(ExpressionTree firstExpression, ExpressionTree secondExpression) {
        return generateCondition(Type.LTE, firstExpression, secondExpression);
    }

    public static ConditionWithDistance equalTo(ExpressionTree firstExpression, ExpressionTree secondExpression) {
        return generateCondition(Type.EQ, firstExpression, secondExpression);
    }

    // return whether the condition returns true or false, as well as how close to the boundary the result was
    public Tuple2<Boolean, Double> run(State<DualNumber> state) {
        DualNumber firstValue = firstExpression.run(state);
        DualNumber secondValue = secondExpression.run(state);
        double point = firstValue.getValue() - secondValue.getValue();
        switch (type) {
            case EQ: return new Tuple2<>(point == 0, point);
            case GT: return new Tuple2<>(point > 0, point);
            case LT: return new Tuple2<>(point < 0, point);
            case GTE: return new Tuple2<>(point >= 0, point);
            case LTE: return new Tuple2<>(point <= 0, point);
            default:  return new Tuple2<>(true, 0.0);
        }
    }
}
