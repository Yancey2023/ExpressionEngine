/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.ast;

import yancey.expression.ast.node.BaseNode;
import yancey.expression.lexer.Lexer;
import yancey.expression.parser.Parser;

import java.util.Map;

public class Expression {

    private final BaseNode root;

    public Expression(String string) {
        this.root = Parser.parse(Lexer.lex(string));
    }

    public double calculate(Map<String, Double> variables) {
        return root.calculate(variables);
    }

}
