package ch07.item42;

public enum DiscountType1 {
    FIXED(0) {
        @Override
        public double applyDiscount(double amount) {
            return amount - 10;
        }
    },
    PERCENTAGE(0.10) {
        @Override
        public double applyDiscount(double amount) {
            return amount * (1 - rate);
        }
    },
    CONDITIONAL(0.20) {
        private final double minimumAmount = 100;

        @Override
        public double applyDiscount(double amount) {
            if (amount >= minimumAmount) {
                return amount * (1 - rate);
            }
            return amount;
        }
    };

    protected final double rate;

    DiscountType1(double rate) {
        this.rate = rate;
    }

    public abstract double applyDiscount(double amount);

    public static void main(String[] args) {
        double amount = 120;

        for (DiscountType1 type : DiscountType1.values()) {
            System.out.printf("%s 할인 적용 %.2f%n", type, type.applyDiscount(amount));
        }
    }
}
