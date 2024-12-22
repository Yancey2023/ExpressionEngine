/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.lexer;

import java.util.function.Function;

public class StringReader {
    private final String str;
    private int index;

    public StringReader(String str) {
        this.str = str;
        this.index = 0;
    }

    public boolean hasNext() {
        return index < str.length();
    }

    public char peek() {
        return str.charAt(index);
    }

    public void skip() {
        if (index < str.length()) {
            index += 1;
        }
    }

    public char read() {
        char peek = peek();
        skip();
        return peek;
    }

    public String collect(Function<Character, Boolean> filter) {
        int from = index;
        while (hasNext() && filter.apply(peek())) {
            skip();
        }
        return str.substring(from, index);
    }
}
