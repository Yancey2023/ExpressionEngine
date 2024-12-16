package yancey.expression.ast.node;

import java.util.Map;

public class AddNode extends BaseNode {

    private final BaseNode node1, node2;

    public AddNode(BaseNode node1, BaseNode node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return node1.calculate(variables) + node2.calculate(variables);
    }

}
