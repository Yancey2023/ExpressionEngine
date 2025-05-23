/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.lexer;

public record Token(TokenType tokenType, String string) {

    public enum TokenType {
        STRING,
        NUMBER,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        ADD,
        MINUS,
        MULTIPLY,
        DIVIDE,
        POW,
        COMMA,
    }

}
