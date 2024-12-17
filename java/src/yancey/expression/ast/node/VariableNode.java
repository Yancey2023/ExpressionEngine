/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */
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
        } else if (Objects.equals(variableName, "e")) {
            return Math.E;
        }
        if (variableName.length() > 1) {
            result = 1.0;
            for (char ch : variableName.toCharArray()) {
                String variableName0 = String.valueOf(ch);
                Double result0 = variables.get(variableName0);
                if (result0 != null) {
                    result *= result0;
                } else if (Objects.equals(variableName0, "e")) {
                    result *= Math.E;
                }
            }
            return result;
        }
        throw new RuntimeException("unknown variable name: " + variableName);
    }

}
