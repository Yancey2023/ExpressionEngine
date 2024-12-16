package yancey.expression.ast.node;

import java.util.List;
import java.util.Map;

public class FunctionNode extends BaseNode {

    public enum Type {
        SIN,
        COS,
        TAN,
        ASIN,
        ACOS,
        ATAN,
        SQRT,
        EXP,
        POW,
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
            case SIN -> Math.sin(arguments.get(0).calculate(variables));
            case COS -> Math.cos(arguments.get(0).calculate(variables));
            case TAN -> Math.tan(arguments.get(0).calculate(variables));
            case ASIN -> Math.asin(arguments.get(0).calculate(variables));
            case ACOS -> Math.acos(arguments.get(0).calculate(variables));
            case ATAN -> Math.atan(arguments.get(0).calculate(variables));
            case SQRT -> Math.sqrt(arguments.get(0).calculate(variables));
            case EXP -> Math.exp(arguments.get(0).calculate(variables));
            case POW -> Math.pow(
                    arguments.get(0).calculate(variables),
                    arguments.get(1).calculate(variables)
            );
            case LOG -> Math.log(arguments.get(0).calculate(variables)) /
                    Math.log(arguments.get(1).calculate(variables));
        };
    }

}
