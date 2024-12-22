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
            char peek = stringReader.peek();
            if (peek == ' ') {
                continue;
            }
            result.add(switch (peek) {
                case '+' -> new Token(Token.TokenType.ADD, String.valueOf(stringReader.read()));
                case '-' -> new Token(Token.TokenType.MINUS, String.valueOf(stringReader.read()));
                case '*' -> new Token(Token.TokenType.MULTIPLY, String.valueOf(stringReader.read()));
                case '/' -> new Token(Token.TokenType.DIVIDE, String.valueOf(stringReader.read()));
                case '^' -> new Token(Token.TokenType.POW, String.valueOf(stringReader.read()));
                case '(' -> new Token(Token.TokenType.LEFT_BRACKET, String.valueOf(stringReader.read()));
                case ')' -> new Token(Token.TokenType.RIGHT_BRACKET, String.valueOf(stringReader.read()));
                case ',' -> new Token(Token.TokenType.COMMA, String.valueOf(stringReader.read()));
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' ->
                        new Token(Token.TokenType.NUMBER, stringReader.collect(
                                character -> (character >= '0' && character <= '9') || character == '.'));
                default -> new Token(Token.TokenType.STRING, stringReader.collect(
                        character -> !List.of('+', '-', '*', '/', '^', '(', ')', ',').contains(character)));
            });
        }
        if (stringReader.hasNext()) {
            throw new RuntimeException("string not read completed");
        }
        return result;
    }

}
