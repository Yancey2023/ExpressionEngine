package yancey.expression.ast.node;

import java.util.Map;

public abstract class BaseNode {
    public abstract double calculate(Map<String, Double> variables);
}
