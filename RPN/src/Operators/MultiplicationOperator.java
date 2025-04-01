package Operators;

public class MultiplicationOperator implements Operator {
    @Override
    public int apply(int a, int b) { return a * b;     }

    @Override
    public String getSymbol() { return "*"; }
}