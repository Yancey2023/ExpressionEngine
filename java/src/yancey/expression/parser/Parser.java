/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

package yancey.expression.parser;

import yancey.expression.ast.node.BaseNode;
import yancey.expression.ast.node.FunctionNode;
import yancey.expression.ast.node.NumberNode;
import yancey.expression.ast.node.VariableNode;
import yancey.expression.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final TokenReader tokenReader;

    private Parser(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    public static BaseNode parse(List<Token> tokens) {
        TokenReader tokenReader = new TokenReader(tokens);
        BaseNode result = new Parser(tokenReader).parseExpression();
        if (tokenReader.hasNext()) {
            throw new RuntimeException("tokens not read completed");
        }
        return result;
    }

    private BaseNode parseExpression() {
        BaseNode result = parseTerm();
        while (tokenReader.hasNext()) {
            Token.TokenType tokenType = tokenReader.peek().getTokenType();
            if (tokenType == Token.TokenType.ADD) {
                tokenReader.skip();
                result = new FunctionNode(FunctionNode.Type.ADD, List.of(result, parseTerm()));
            } else if (tokenType == Token.TokenType.MINUS) {
                tokenReader.skip();
                result = new FunctionNode(FunctionNode.Type.MINUS, List.of(result, parseTerm()));
            } else {
                break;
            }
        }
        return result;
    }

    private BaseNode parseFactor() {
        if (!tokenReader.hasNext()) {
            throw new RuntimeException("token reader end");
        }
        switch (tokenReader.peek().getTokenType()) {
            case LEFT_BRACKET -> {
                tokenReader.skip();
                BaseNode result = parseExpression();
                if (!tokenReader.hasNext()) {
                    throw new RuntimeException("token reader end");
                }
                if (tokenReader.peek().getTokenType() != Token.TokenType.RIGHT_BRACKET) {
                    throw new RuntimeException("no right bracket");
                }
                tokenReader.skip();
                return result;
            }
            case NUMBER -> {
                return new NumberNode(Double.parseDouble(tokenReader.read().getString()));
            }
            case STRING -> {
                String string = tokenReader.read().getString();
                return switch (string) {
                    case "sin" -> new FunctionNode(FunctionNode.Type.SIN, parseArguments(1));
                    case "cos" -> new FunctionNode(FunctionNode.Type.COS, parseArguments(1));
                    case "tan" -> new FunctionNode(FunctionNode.Type.TAN, parseArguments(1));
                    case "asin" -> new FunctionNode(FunctionNode.Type.ASIN, parseArguments(1));
                    case "acos" -> new FunctionNode(FunctionNode.Type.ACOS, parseArguments(1));
                    case "atan" -> new FunctionNode(FunctionNode.Type.ATAN, parseArguments(1));
                    case "sqrt" -> new FunctionNode(FunctionNode.Type.SQRT, parseArguments(1));
                    case "exp" -> new FunctionNode(FunctionNode.Type.EXP, parseArguments(1));
                    case "pow" -> new FunctionNode(FunctionNode.Type.POW, parseArguments(2));
                    case "log" -> new FunctionNode(FunctionNode.Type.LOG, parseArguments(2));
                    default -> new VariableNode(string);
                };
            }
            case ADD -> {
                tokenReader.skip();
                return parseFactor();
            }
            case MINUS -> {
                tokenReader.skip();
                return new FunctionNode(FunctionNode.Type.MULTIPY, List.of(new NumberNode(-1), parseFactor()));
            }
            default -> throw new RuntimeException("error token type");
        }
    }

    private BaseNode parseTerm() {
        BaseNode result = parsePower();
        outer:
        while (tokenReader.hasNext()) {
            switch (tokenReader.peek().getTokenType()) {
                case MULTIPLY -> {
                    tokenReader.skip();
                    result = new FunctionNode(FunctionNode.Type.MULTIPY, List.of(result, parsePower()));
                }
                case DIVIDE -> {
                    tokenReader.skip();
                    result = new FunctionNode(FunctionNode.Type.DIVIDE, List.of(result, parsePower()));
                }
                case LEFT_BRACKET, STRING, NUMBER ->
                        result = new FunctionNode(FunctionNode.Type.MULTIPY, List.of(result, parsePower()));
                default -> {
                    break outer;
                }
            }
        }
        return result;
    }

    private BaseNode parsePower() {
        if (!tokenReader.hasNext()) {
            throw new RuntimeException("token reader end");
        }
        BaseNode result = parseFactor();
        while (tokenReader.hasNext()) {
            if (tokenReader.peek().getTokenType() != Token.TokenType.POW) {
                break;
            }
            tokenReader.skip();
            result = new FunctionNode(FunctionNode.Type.POW, List.of(result, parseFactor()));
        }
        return result;
    }

    private List<BaseNode> parseArguments(int nArguments) {
        if (!tokenReader.hasNext()) {
            throw new RuntimeException("token reader end");
        }
        if (tokenReader.peek().getTokenType() != Token.TokenType.LEFT_BRACKET) {
            throw new RuntimeException("require left bracket");
        }
        tokenReader.skip();
        List<BaseNode> nodes = new ArrayList<>();
        if (nArguments > 0) {
            nodes.add(parseExpression());
        }
        for (int i = 1; i < nArguments; i++) {
            if (!tokenReader.hasNext()) {
                throw new RuntimeException("token reader end");
            }
            if (tokenReader.peek().getTokenType() != Token.TokenType.COMMA) {
                throw new RuntimeException("require comma");
            }
            tokenReader.skip();
            nodes.add(parseExpression());
        }
        if (!tokenReader.hasNext()) {
            throw new RuntimeException("token reader end");
        }
        if (tokenReader.peek().getTokenType() != Token.TokenType.RIGHT_BRACKET) {
            throw new RuntimeException("require right bracket");
        }
        tokenReader.skip();
        return nodes;
    }

}
