/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.ast.node;

import java.util.List;
import java.util.Map;

public class FunctionNode extends BaseNode {

    public enum Type {
        ADD,
        MINUS,
        MULTIPY,
        DIVIDE,
        POW,
        SIN,
        COS,
        TAN,
        ASIN,
        ACOS,
        ATAN,
        SQRT,
        EXP,
        LOG,
    }

    private final Type type;
    private final List<BaseNode> arguments;

    public FunctionNode(Type type, List<BaseNode> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return switch (type) {
            case ADD -> arguments.get(0).calculate(variables) + arguments.get(1).calculate(variables);
            case MINUS -> arguments.get(0).calculate(variables) - arguments.get(1).calculate(variables);
            case MULTIPY -> arguments.get(0).calculate(variables) * arguments.get(1).calculate(variables);
            case DIVIDE -> arguments.get(0).calculate(variables) / arguments.get(1).calculate(variables);
            case POW -> Math.pow(arguments.get(0).calculate(variables), arguments.get(1).calculate(variables));
            case SIN -> Math.sin(arguments.get(0).calculate(variables));
            case COS -> Math.cos(arguments.get(0).calculate(variables));
            case TAN -> Math.tan(arguments.get(0).calculate(variables));
            case ASIN -> Math.asin(arguments.get(0).calculate(variables));
            case ACOS -> Math.acos(arguments.get(0).calculate(variables));
            case ATAN -> Math.atan(arguments.get(0).calculate(variables));
            case SQRT -> Math.sqrt(arguments.get(0).calculate(variables));
            case EXP -> Math.exp(arguments.get(0).calculate(variables));
            case LOG -> Math.log(arguments.get(0).calculate(variables)) /
                    Math.log(arguments.get(1).calculate(variables));
        };
    }

}
