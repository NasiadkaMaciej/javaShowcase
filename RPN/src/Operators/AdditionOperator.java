package Operators;

public class AdditionOperator implements Operator {
    @Override
    public int apply(int a, int b) {
        return a + b;
    }

    @Override
    public String getSymbol() {
        return "+";
    }
}
