# Expression Engine

A simple expression engine.

## Example

**Python:**

```python
from expression import Expression

formula = '1/2*(b)^2+3*b-4'
variables = {"a": 8.0, "b": 11.0}
result = Expression(formula).calculate(variables)
```

**Java:**

```java
import yancey.expression.ast.Expression;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String expression = "1/2*(b)^2+3*b-4";
        Map<String, Double> variables = Map.of("a", 8.0, "b", 11.0);
        double result = new Expression(expression).calculate(variables);
    }

}
```

## About

Author: Yancey

QQ: 1709185482

# License

MIT Licence
