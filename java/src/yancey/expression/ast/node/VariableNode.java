package yancey.expression.ast.node;

import java.util.Map;
import java.util.Objects;

public class VariableNode extends BaseNode {

    private final String variableName;

    public VariableNode(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        Double result = variables.get(variableName);
        if (result != null) {
            return result;
        }
        if (Objects.equals(variableName, "e")) {
            return Math.E;
        }
        throw new RuntimeException("unknown variable name: " + variableName);
    }

}
