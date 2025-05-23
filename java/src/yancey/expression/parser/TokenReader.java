/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.parser;

import yancey.expression.lexer.Token;

import java.util.List;

public class TokenReader {

    private final List<Token> tokens;
    private int index;

    public TokenReader(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public boolean hasNext() {
        return index < tokens.size();
    }

    public Token peek() {
        return tokens.get(index);
    }

    public void skip() {
        if (index < tokens.size()) {
            index += 1;
        }
    }

    public Token read() {
        Token peek = peek();
        skip();
        return peek;
    }
}
