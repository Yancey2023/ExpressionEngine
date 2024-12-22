/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.ast.node;

import java.util.Map;

public abstract class BaseNode {
    public abstract double calculate(Map<String, Double> variables);
}
