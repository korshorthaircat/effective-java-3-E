package ch07.item42;

/**
 * 상수별로 다르게 동작해야 하는 enum (1) 추상 메서드 사용
 * 상수별 클래스 몸체와 데이터를 사용한 enum
 */
public enum Operation1 {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;
    Operation1(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public abstract double apply(double x, double y);

    public static void main(String[] args) {
        double x = 10.5;
        double y = 5.0;
        for (Operation2 op : Operation2.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
    }
}
