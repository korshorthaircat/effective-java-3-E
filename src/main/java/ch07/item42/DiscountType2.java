package ch07.item42;

import java.util.function.DoubleUnaryOperator;

public enum DiscountType2 {
    FIXED(0, amount -> amount - 10),
    PERCENTAGE(0.10, amount -> amount * 0.90),
    CONDITIONAL(0.20, null); // How to handle conditional logic here?
    // 람다는 minimunAmount같은 인스턴스 필드를 참조할 수 없기 때문에 if (amount >= minimumAmount) 같은 로직을 가질 수 없다.
    // enum에서 minimumAmount를 정의하려고해도, 람다는 런타임에서 enum인스턴스를 볼 수 없으므로 람다 내에서 참조할 수 없다.

    private final double rate;
    private final DoubleUnaryOperator operator;

    DiscountType2(double rate, DoubleUnaryOperator operator) {
        this.rate = rate;
        this.operator = operator;
    }

    public double applyDiscount(double amount) {
        return operator.applyAsDouble(amount); // This would fail for CONDITIONAL
    }
}