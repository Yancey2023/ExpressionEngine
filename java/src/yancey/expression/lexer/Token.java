/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */
package yancey.expression.lexer;

public class Token {

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
        WHITESPACE,
    }

    private final TokenType tokenType;
    private final String string;

    public Token(TokenType tokenType, String string) {
        this.tokenType = tokenType;
        this.string = string;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getString() {
        return string;
    }
}
