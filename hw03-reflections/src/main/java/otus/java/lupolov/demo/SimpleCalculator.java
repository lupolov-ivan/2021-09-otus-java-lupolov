package otus.java.lupolov.demo;

public class SimpleCalculator {

    private final int operand1;
    private final int operand2;

    public SimpleCalculator(int operand1, int operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public int add() {
        return operand1 + operand2;
    }

    public int sub() {
        return operand1 - operand2;
    }

    public int multiply() {
        return operand1 * operand2;
    }

    public int divide() {
        return operand1/operand2;
    }

    @Override
    public String toString() {
        return "SimpleCalculator{" +
                "operand1=" + operand1 +
                ", operand2=" + operand2 +
                '}';
    }
}
