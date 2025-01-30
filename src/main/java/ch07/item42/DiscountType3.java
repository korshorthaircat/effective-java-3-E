package ch07.item42;
import java.util.function.DoubleUnaryOperator;

public enum DiscountType3 {
    FIXED(0, amount -> amount - 10),
    PERCENTAGE(0.10, amount -> amount * 0.90),
    CONDITIONAL(0.20, amount -> {
        final double MINIMUM_AMOUNT = 100; // 인스턴스 필드가 아니라 지역변수로 관리됨
        return amount >= MINIMUM_AMOUNT ? amount * 0.80 : amount;
    });

    private final double rate;
    private final DoubleUnaryOperator operator;

    DiscountType3(double rate, DoubleUnaryOperator operator) {
        this.rate = rate;
        this.operator = operator;
    }

    public double applyDiscount(double amount) {
        return operator.applyAsDouble(amount);
    }

    public static void main(String[] args) {
        double amount = 120;

        for (DiscountType1 type : DiscountType1.values()) {
            System.out.printf("%s 할인 적용: %.2f%n", type, type.applyDiscount(amount));
        }
    }
}
