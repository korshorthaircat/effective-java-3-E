package ch07.item42;

import java.util.function.DoubleBinaryOperator;

/**
 * 상수별로 다르게 동작해야 하는 enum (2) 람다 사용
 * 함수 객체를 인스턴스 필드에 저장해 상수별 동작을 구현한 enum
 */
public enum Operation2 {
    PLUS  ("+", (x, y) -> x + y),
    MINUS ("-", (x, y) -> x - y),
    TIMES ("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final DoubleBinaryOperator op;

    Operation2(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }

    public static void main(String[] args) {
        double x = 10.5;
        double y = 5.0;
        for (Operation2 op : Operation2.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
    }
}
