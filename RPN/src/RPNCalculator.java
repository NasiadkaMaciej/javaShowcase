import Operators.*;

import java.util.HashMap;
import java.util.Map;

public class RPNCalculator {
    private final Map<String, Operator> operators;

    // Constructor to initialize the operators map
    public RPNCalculator() {
        operators = new HashMap<>();
        operators.put("+", new AdditionOperator());
        operators.put("-", new SubtractionOperator());
        operators.put("*", new MultiplicationOperator());
    }

    // Evaluate the given expression in Reverse Polish Notation
    public int evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty())
            throw new IllegalArgumentException("Expression cannot be empty");

        Stack<Integer> stack = new Stack<>();
        // Split the expression by whitespaces
        String[] tokens = expression.trim().split("\\s+");

        // Process each token
        for (String token : tokens) {
            // If the token is a number, push it to the stack
            if (isNumeric(token)) stack.push(Integer.parseInt(token));
            // If the token is an operator, pop two operands from the stack, perform the operation, and push the result back
            else if (operators.containsKey(token)) {
                if (stack.isEmpty())
                    throw new IllegalArgumentException("Invalid expression: insufficient operands");
                int b = stack.pop();
                if (stack.isEmpty())
                    throw new IllegalArgumentException("Invalid expression: insufficient operands");
                int a = stack.pop();

                // Apply the operator and push the result back to the stack
                stack.push(operators.get(token).apply(a, b));
            } else throw new IllegalArgumentException("Invalid token: " + token);
        }

        // The result should be the only element in the stack
        if (stack.isEmpty())
            throw new IllegalArgumentException("Invalid expression: no result");

        int result = stack.pop();

        // The stack should be empty after evaluating the expression
        if (!stack.isEmpty())
            throw new IllegalArgumentException("Invalid expression: too many operands");

        return result;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}