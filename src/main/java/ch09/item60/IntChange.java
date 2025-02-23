package ch09.item60;

public class IntChange {
    public static void main(String[] args) {
        int itemsBought = 0;
        int funds = 100; // 100센트=1달러. 소수를 사용하지 않고, 센트(₩, $ 단위가 아닌 작은 단위) 단위로 변환해서 연산하는 방법
        for (int price = 10; funds >= price; price += 10) {
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + " 개 구입");
        System.out.println("잔돈(센트): " + funds);
    }
}
