/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */
package yancey.expression.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private Lexer() {
    }

    public static List<Token> lex(String string) {
        List<Token> result = new ArrayList<>();
        StringReader stringReader = new StringReader(string);
        while (stringReader.hasNext()) {
            Token.TokenType tokenType = switch (stringReader.peek()) {
                case '+' -> Token.TokenType.ADD;
                case '-' -> Token.TokenType.MINUS;
                case '*' -> Token.TokenType.MULTIPLY;
                case '/' -> Token.TokenType.DIVIDE;
                case '^' -> Token.TokenType.POW;
                case '(' -> Token.TokenType.LEFT_BRACKET;
                case ')' -> Token.TokenType.RIGHT_BRACKET;
                case ',' -> Token.TokenType.COMMA;
                case ' ' -> Token.TokenType.WHITESPACE;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> Token.TokenType.NUMBER;
                default -> Token.TokenType.STRING;
            };
            result.add(switch (tokenType) {
                case STRING -> new Token(Token.TokenType.STRING, stringReader.collect(
                        character -> "+-*/^(), ".chars().allMatch(value -> value != character)));
                case NUMBER -> new Token(Token.TokenType.NUMBER, stringReader.collect(
                        character -> (character >= '0' && character <= '9') || character == '.'));
                case WHITESPACE -> new Token(Token.TokenType.WHITESPACE, stringReader.collect(
                        character -> character == ' '));
                default -> new Token(tokenType, String.valueOf(stringReader.read()));
            });
        }
        if (stringReader.hasNext()) {
            throw new RuntimeException("string not read completed");
        }
        return result;
    }

}
