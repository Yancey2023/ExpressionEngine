"""
A simple expression engine
Author: Yancey
License: MIT Licence
Website: https://github.com/Yancey2023/ExpressionEngine
"""

import math
from abc import ABCMeta, abstractmethod
from enum import Enum
from typing import Callable


class StringReader:
    def __init__(self, text: str):
        self._text = text
        self._index = 0

    def has_next(self) -> bool:
        return self._index < len(self._text)

    def peek(self) -> str:
        return self._text[self._index]

    def skip(self) -> None:
        if self._index < len(self._text):
            self._index += 1

    def read(self) -> str:
        peek = self.peek()
        self.skip()
        return peek

    def collect(self, filter_fun: Callable[[str], bool]) -> str:
        start = self._index
        while self.has_next() and filter_fun(self.peek()):
            self.skip()
        return self._text[start: self._index]


class TokenType(Enum):
    STRING = 0
    NUMBER = 1
    LEFT_BRACKET = 2
    RIGHT_BRACKET = 3
    ADD = 4
    MINUS = 5
    MULTIPLY = 6
    DIVIDE = 7
    POW = 8
    COMMA = 9


class Token:
    def __init__(self, token_type: TokenType, text: str):
        self._token_type = token_type
        self._text = text

    def get_token_type(self) -> TokenType:
        return self._token_type

    def get_text(self) -> str:
        return self._text


class Lexer:
    @staticmethod
    def lex(text: str) -> list[Token]:
        result = []
        string_reader = StringReader(text)
        while string_reader.has_next():
            match string_reader.peek():
                case ' ':
                    string_reader.skip()
                    continue
                case '+':
                    result.append(Token(TokenType.ADD, string_reader.read()))
                case '-':
                    result.append(Token(TokenType.MINUS, string_reader.read()))
                case '*':
                    result.append(Token(TokenType.MULTIPLY, string_reader.read()))
                case '/':
                    result.append(Token(TokenType.DIVIDE, string_reader.read()))
                case '^':
                    result.append(Token(TokenType.POW, string_reader.read()))
                case '(':
                    result.append(Token(TokenType.LEFT_BRACKET, string_reader.read()))
                case ')':
                    result.append(Token(TokenType.RIGHT_BRACKET, string_reader.read()))
                case ',':
                    result.append(Token(TokenType.COMMA, string_reader.read()))
                case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' | '.':
                    result.append(Token(TokenType.NUMBER, string_reader.collect(
                        lambda ch: ch in ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'])))
                case _:
                    result.append(Token(TokenType.STRING, string_reader.collect(
                        lambda ch: ch not in ['+', '-', '*', '/', '^', '(', ')', ',', ' '])))
        if string_reader.has_next():
            raise 'string not read completed'
        return result


class BaseNode:
    __metaclass__ = ABCMeta

    @abstractmethod
    def calculate(self, variables: dict[str, float]) -> float:
        pass


class NumberNode(BaseNode):
    def __init__(self, num: float):
        self._num = num

    def calculate(self, variables: dict[str, float]) -> float:
        return self._num


class VariableNode(BaseNode):
    def __init__(self, variable_name: str):
        self._variable_name = variable_name

    def calculate(self, variables: dict[str, float]) -> float:
        result = variables.get(self._variable_name, None)
        if result is not None:
            return result
        elif self._variable_name == 'e':
            return math.e
        if len(self._variable_name) > 1:
            result = 1
            for inner_variable_name in self._variable_name:
                result0 = variables.get(inner_variable_name, None)
                if result0 is not None:
                    result *= result0
                elif inner_variable_name == 'e':
                    result *= math.e
                else:
                    result = None
                    break
            if result is not None:
                return result

        raise f'unknown variable name: {self._variable_name}'


class FunctionType(Enum):
    ADD = 0
    MINUS = 1
    MULTIPY = 2
    DIVIDE = 3
    POW = 4
    SIN = 5
    COS = 6
    TAN = 7
    ASIN = 8
    ACOS = 9
    ATAN = 10
    SQRT = 11
    EXP = 12
    LOG = 13


class FunctionNode(BaseNode):
    def __init__(self, function_type: FunctionType, arguments: list[BaseNode]):
        self._function_type = function_type
        self._arguments = arguments

    def calculate(self, variables: dict[str, float]) -> float:
        match self._function_type:
            case FunctionType.ADD:
                return self._arguments[0].calculate(variables) + self._arguments[1].calculate(variables)
            case FunctionType.MINUS:
                return self._arguments[0].calculate(variables) - self._arguments[1].calculate(variables)
            case FunctionType.MULTIPY:
                return self._arguments[0].calculate(variables) * self._arguments[1].calculate(variables)
            case FunctionType.DIVIDE:
                return self._arguments[0].calculate(variables) / self._arguments[1].calculate(variables)
            case FunctionType.POW:
                return self._arguments[0].calculate(variables) ** self._arguments[1].calculate(variables)
            case FunctionType.SIN:
                return math.sin(self._arguments[0].calculate(variables))
            case FunctionType.COS:
                return math.cos(self._arguments[0].calculate(variables))
            case FunctionType.TAN:
                return math.tan(self._arguments[0].calculate(variables))
            case FunctionType.ASIN:
                return math.asin(self._arguments[0].calculate(variables))
            case FunctionType.ACOS:
                return math.acos(self._arguments[0].calculate(variables))
            case FunctionType.ATAN:
                return math.atan(self._arguments[0].calculate(variables))
            case FunctionType.SQRT:
                return math.sqrt(self._arguments[0].calculate(variables))
            case FunctionType.EXP:
                return math.exp(self._arguments[0].calculate(variables))
            case FunctionType.LOG:
                return math.log(self._arguments[0].calculate(variables), self._arguments[1].calculate(variables))
            case _:
                raise 'unknown function type'


class TokenReader:
    def __init__(self, tokens: list[Token]):
        self._tokens = tokens
        self._index = 0

    def has_next(self) -> bool:
        return self._index < len(self._tokens)

    def peek(self) -> Token:
        return self._tokens[self._index]

    def skip(self) -> None:
        if self._index < len(self._tokens):
            self._index += 1

    def read(self) -> Token:
        peek = self.peek()
        self.skip()
        return peek


class Parser:
    def __init__(self, token_reader: TokenReader):
        self._token_reader = token_reader

    @staticmethod
    def parse(tokens: list[Token]) -> BaseNode:
        token_reader = TokenReader(tokens)
        result = Parser(token_reader).parse_expression()
        if token_reader.has_next():
            raise 'tokens not read completed'
        return result

    def parse_expression(self) -> BaseNode:
        result = self.parse_term()
        while self._token_reader.has_next():
            match self._token_reader.peek().get_token_type():
                case TokenType.ADD:
                    self._token_reader.skip()
                    result = FunctionNode(FunctionType.ADD, [result, self.parse_term()])
                case TokenType.MINUS:
                    self._token_reader.skip()
                    result = FunctionNode(FunctionType.MINUS, [result, self.parse_term()])
                case _:
                    break
        return result

    def parse_factor(self) -> BaseNode:
        if not self._token_reader.has_next():
            raise 'token reader end'
        match self._token_reader.peek().get_token_type():
            case TokenType.LEFT_BRACKET:
                self._token_reader.skip()
                result = self.parse_expression()
                if not self._token_reader.has_next():
                    raise 'token reader end'
                if self._token_reader.peek().get_token_type() != TokenType.RIGHT_BRACKET:
                    raise 'no right bracket'
                self._token_reader.skip()
                return result
            case TokenType.NUMBER:
                return NumberNode(float(self._token_reader.read().get_text()))
            case TokenType.STRING:
                text = self._token_reader.read().get_text()
                match text:
                    case 'sin':
                        return FunctionNode(FunctionType.SIN, self.parse_arguments(1))
                    case 'cos':
                        return FunctionNode(FunctionType.COS, self.parse_arguments(1))
                    case 'tan':
                        return FunctionNode(FunctionType.TAN, self.parse_arguments(1))
                    case 'asin':
                        return FunctionNode(FunctionType.ASIN, self.parse_arguments(1))
                    case 'acos':
                        return FunctionNode(FunctionType.ACOS, self.parse_arguments(1))
                    case 'atan':
                        return FunctionNode(FunctionType.ATAN, self.parse_arguments(1))
                    case 'sqrt':
                        return FunctionNode(FunctionType.SQRT, self.parse_arguments(1))
                    case 'exp':
                        return FunctionNode(FunctionType.EXP, self.parse_arguments(1))
                    case 'pow':
                        return FunctionNode(FunctionType.POW, self.parse_arguments(2))
                    case 'log':
                        return FunctionNode(FunctionType.LOG, self.parse_arguments(2))
                    case _:
                        return VariableNode(text)
            case TokenType.ADD:
                self._token_reader.skip()
                return self.parse_factor()
            case TokenType.MINUS:
                self._token_reader.skip()
                return FunctionNode(FunctionType.MULTIPY, [NumberNode(-1), self.parse_factor()])
            case _:
                raise 'error token type'

    def parse_term(self) -> BaseNode:
        result = self.parse_power()
        while self._token_reader.has_next():
            match self._token_reader.peek().get_token_type():
                case TokenType.MULTIPLY:
                    self._token_reader.skip()
                    result = FunctionNode(FunctionType.MULTIPY, [result, self.parse_power()])
                case TokenType.DIVIDE:
                    self._token_reader.skip()
                    result = FunctionNode(FunctionType.DIVIDE, [result, self.parse_power()])
                case TokenType.LEFT_BRACKET | TokenType.STRING | TokenType.NUMBER:
                    result = FunctionNode(FunctionType.MULTIPY, [result, self.parse_power()])
                case _:
                    break
        return result

    def parse_power(self) -> BaseNode:
        if not self._token_reader.has_next():
            raise 'token reader end'
        result = self.parse_factor()
        while self._token_reader.has_next():
            if self._token_reader.peek().get_token_type() != TokenType.POW:
                break
            self._token_reader.skip()
            result = FunctionNode(FunctionType.POW, [result, self.parse_factor()])
        return result

    def parse_arguments(self, n_arguments: int) -> list[BaseNode]:
        if not self._token_reader.has_next():
            raise 'token reader end'
        if self._token_reader.peek().get_token_type() != TokenType.LEFT_BRACKET:
            raise 'require left bracket'
        self._token_reader.skip()
        nodes = []
        if n_arguments > 0:
            nodes.append(self.parse_expression())
            for _ in range(1, n_arguments):
                if not self._token_reader.has_next():
                    raise 'token reader end'
                if self._token_reader.peek().get_token_type() != TokenType.COMMA:
                    raise 'require comma'
                self._token_reader.skip()
                nodes.append(self.parse_expression())
        if not self._token_reader.has_next():
            raise 'token reader end'
        if self._token_reader.peek().get_token_type() != TokenType.RIGHT_BRACKET:
            raise 'require right bracket'
        self._token_reader.skip()
        return nodes


class Expression:
    def __init__(self, text: str):
        self.root = Parser.parse(Lexer.lex(text))

    def calculate(self, variables: dict[str, float]) -> float:
        return self.root.calculate(variables)
