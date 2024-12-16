/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */
package yancey.expression.ast.node;

import java.util.Map;

public class PowNode extends BaseNode {

    private final BaseNode node1, node2;

    public PowNode(BaseNode node1, BaseNode node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return Math.pow(node1.calculate(variables), node2.calculate(variables));
    }

}
