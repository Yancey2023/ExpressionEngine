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
    WHITESPACE = 10


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
            peek = string_reader.peek()
            if peek == '+':
                token_type = TokenType.ADD
            elif peek == '-':
                token_type = TokenType.MINUS
            elif peek == '*':
                token_type = TokenType.MULTIPLY
            elif peek == '/':
                token_type = TokenType.DIVIDE
            elif peek == '^':
                token_type = TokenType.POW
            elif peek == '(':
                token_type = TokenType.LEFT_BRACKET
            elif peek == ')':
                token_type = TokenType.RIGHT_BRACKET
            elif peek == ',':
                token_type = TokenType.COMMA
            elif peek == ' ':
                token_type = TokenType.WHITESPACE
            elif peek in ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']:
                token_type = TokenType.NUMBER
            else:
                token_type = TokenType.STRING
            if token_type == TokenType.STRING:
                token = Token(TokenType.STRING, string_reader.collect(
                    lambda ch: ch not in ['+', '-', '*', '/', '^', '(', ')', ',', ' ']))
            elif token_type == TokenType.NUMBER:
                token = Token(TokenType.NUMBER, string_reader.collect(
                    lambda ch: ch in ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']))
            elif token_type == TokenType.WHITESPACE:
                token = Token(TokenType.WHITESPACE, string_reader.collect(
                    lambda ch: ch == ' '))
            else:
                token = Token(token_type, string_reader.read())
            result.append(token)
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
        else:
            raise f'unknown variable name: {self._variable_name}'


class FunctionType(Enum):
    SIN = 0
    COS = 1
    TAN = 2
    ASIN = 3
    ACOS = 4
    ATAN = 5
    SQRT = 6
    EXP = 7
    POW = 8
    LOG = 9


class FunctionNode(BaseNode):
    def __init__(self, function_type: FunctionType, arguments: list[BaseNode]):
        self._function_type = function_type
        self._arguments = arguments

    def calculate(self, variables: dict[str, float]) -> float:
        if self._function_type == FunctionType.SIN:
            return math.sin(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.COS:
            return math.cos(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.TAN:
            return math.tan(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.ASIN:
            return math.asin(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.ACOS:
            return math.acos(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.ATAN:
            return math.atan(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.SQRT:
            return math.sqrt(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.EXP:
            return math.exp(self._arguments[0].calculate(variables))
        elif self._function_type == FunctionType.POW:
            return math.pow(self._arguments[0].calculate(variables), self._arguments[1].calculate(variables))
        elif self._function_type == FunctionType.LOG:
            return math.log(self._arguments[0].calculate(variables), self._arguments[1].calculate(variables))
        else:
            raise 'unknown function type'


class AddNode(BaseNode):
    def __init__(self, node1: BaseNode, node2: BaseNode):
        self._node1 = node1
        self._node2 = node2

    def calculate(self, variables: dict[str, float]) -> float:
        return self._node1.calculate(variables) + self._node2.calculate(variables)


class MinusNode(BaseNode):
    def __init__(self, node1: BaseNode, node2: BaseNode):
        self._node1 = node1
        self._node2 = node2

    def calculate(self, variables: dict[str, float]) -> float:
        return self._node1.calculate(variables) - self._node2.calculate(variables)


class MultiplyNode(BaseNode):
    def __init__(self, node1: BaseNode, node2: BaseNode):
        self._node1 = node1
        self._node2 = node2

    def calculate(self, variables: dict[str, float]) -> float:
        return self._node1.calculate(variables) * self._node2.calculate(variables)


class DivideNode(BaseNode):
    def __init__(self, node1: BaseNode, node2: BaseNode):
        self._node1 = node1
        self._node2 = node2

    def calculate(self, variables: dict[str, float]) -> float:
        return self._node1.calculate(variables) / self._node2.calculate(variables)


class PowNode(BaseNode):
    def __init__(self, node1: BaseNode, node2: BaseNode):
        self._node1 = node1
        self._node2 = node2

    def calculate(self, variables: dict[str, float]) -> float:
        return self._node1.calculate(variables) ** self._node2.calculate(variables)


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

    def skip_white_space(self) -> None:
        while self.has_next() and self.peek().get_token_type() == TokenType.WHITESPACE:
            self.skip()

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
        self._token_reader.skip_white_space()
        while self._token_reader.has_next():
            token_type = self._token_reader.peek().get_token_type()
            if token_type == TokenType.ADD:
                self._token_reader.skip()
                result = AddNode(result, self.parse_term())
            elif token_type == TokenType.MINUS:
                self._token_reader.skip()
                result = MinusNode(result, self.parse_term())
            else:
                break
            self._token_reader.skip_white_space()
        return result

    def parse_factor(self) -> BaseNode:
        self._token_reader.skip_white_space()
        if not self._token_reader.has_next():
            raise 'token reader end'
        token_type = self._token_reader.peek().get_token_type()
        if token_type == TokenType.LEFT_BRACKET:
            self._token_reader.skip()
            result = self.parse_expression()
            self._token_reader.skip_white_space()
            if not self._token_reader.has_next():
                raise 'token reader end'
            if self._token_reader.peek().get_token_type() != TokenType.RIGHT_BRACKET:
                raise 'no right bracket'
            self._token_reader.skip()
            return result
        elif token_type == TokenType.NUMBER:
            return NumberNode(float(self._token_reader.read().get_text()))
        elif token_type == TokenType.STRING:
            text = self._token_reader.read().get_text()
            if text == 'sin':
                return FunctionNode(FunctionType.SIN, self.parse_arguments(1))
            elif text == 'cos':
                return FunctionNode(FunctionType.COS, self.parse_arguments(1))
            elif text == 'tan':
                return FunctionNode(FunctionType.TAN, self.parse_arguments(1))
            elif text == 'asin':
                return FunctionNode(FunctionType.ASIN, self.parse_arguments(1))
            elif text == 'acos':
                return FunctionNode(FunctionType.ACOS, self.parse_arguments(1))
            elif text == 'atan':
                return FunctionNode(FunctionType.ATAN, self.parse_arguments(1))
            elif text == 'sqrt':
                return FunctionNode(FunctionType.SQRT, self.parse_arguments(1))
            elif text == 'exp':
                return FunctionNode(FunctionType.EXP, self.parse_arguments(1))
            elif text == 'pow':
                return FunctionNode(FunctionType.POW, self.parse_arguments(2))
            elif text == 'log':
                return FunctionNode(FunctionType.LOG, self.parse_arguments(2))
            else:
                return VariableNode(text)
        elif token_type == TokenType.ADD:
            self._token_reader.skip()
            return self.parse_factor()
        elif token_type == TokenType.MINUS:
            self._token_reader.skip()
            return MultiplyNode(NumberNode(-1), self.parse_factor())
        else:
            raise 'error token type'

    def parse_term(self) -> BaseNode:
        result = self.parse_power()
        self._token_reader.skip_white_space()
        while self._token_reader.has_next():
            token_type = self._token_reader.peek().get_token_type()
            if token_type == TokenType.MULTIPLY:
                self._token_reader.skip()
                result = MultiplyNode(result, self.parse_power())
            elif token_type == TokenType.DIVIDE:
                self._token_reader.skip()
                result = DivideNode(result, self.parse_power())
            elif token_type in [TokenType.LEFT_BRACKET, TokenType.STRING, TokenType.NUMBER]:
                result = MultiplyNode(result, self.parse_power())
            else:
                break
            self._token_reader.skip_white_space()
        return result

    def parse_power(self) -> BaseNode:
        self._token_reader.skip_white_space()
        if not self._token_reader.has_next():
            raise 'token reader end'
        result = self.parse_factor()
        self._token_reader.skip_white_space()
        while self._token_reader.has_next():
            if self._token_reader.peek().get_token_type() != TokenType.POW:
                break
            self._token_reader.skip()
            result = PowNode(result, self.parse_factor())
            self._token_reader.skip_white_space()
        return result

    def parse_arguments(self, n_arguments: int) -> list[BaseNode]:
        self._token_reader.skip_white_space()
        if not self._token_reader.has_next():
            raise 'token reader end'
        if self._token_reader.peek().get_token_type() != TokenType.LEFT_BRACKET:
            raise 'require left bracket'
        self._token_reader.skip()
        nodes = []
        if n_arguments > 0:
            nodes.append(self.parse_expression())
        for i in range(1, n_arguments):
            self._token_reader.skip_white_space()
            if not self._token_reader.has_next():
                raise 'token reader end'
            if self._token_reader.peek().get_token_type() != TokenType.COMMA:
                raise 'require comma'
            self._token_reader.skip()
            nodes.append(self.parse_expression())
        self._token_reader.skip_white_space()
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
