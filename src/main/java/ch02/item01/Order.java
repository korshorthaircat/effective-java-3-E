package ch02.item01;

public class Order {
    private boolean prime;
    private boolean urgent;
    private Product product;

    public Order() {}

    //생성자는 이름을 가질 수 없으므로, 반환하는 객체의 특성을 제대로 설명하지 못한다.
    public Order(Product product, boolean prime) {
        this.product = product;
        this.prime = prime;
    }

//    public Order(Product product, boolean urgent) {
//        this.product = product;
//        this.prime = prime;
//    }
    //하나의 시그니처로는 생성자를 하나만 만들 수 있다.

    public Order(boolean urgent, Product product) {
        this.product = product;
        this.urgent = urgent;
    }
    //입력 매개변수의 순서를 다르게 한 생성자를 추가하는 식으로 이러한 제한을 피해갈 수도 있지만, 좋지 않다.
    //이러한 API를 사용하는 개발자는 각 생성자가 어떤 역할을 하는지 정확히 기억하기 어려워 엉뚱한 것을 호출할 수도 있다.

    //반면에 정적 팩터리 메서드는 이름을 가질 수 있으므로 반환하는 객체의 성격을 제대로 표현할 수 있다. (정적 팩터리 메서드 사용시의 장점(1))
    public static Order primeOrder(Product product) {
        Order order = new Order();
        order.prime = true;
        order.product = product;
        return order;
    }
    public static Order urgentOrder(Product product) {
        Order order = new Order();
        order.urgent = true;
        order.product = product;
        return order;
    }
}
