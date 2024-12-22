/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.ast.node;

import java.util.Map;

public class NumberNode extends BaseNode {

    private final double number;

    public NumberNode(double number) {
        this.number = number;
    }

    @Override
    public double calculate(Map<String, Double> variables) {
        return number;
    }

}
