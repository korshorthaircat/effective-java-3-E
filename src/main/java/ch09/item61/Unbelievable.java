package ch09.item61;

// 기이하게 동작하는 프로그램
public class Unbelievable {
    static Integer i;

    public static void main(String[] args) {
        if (i == 42) // NullPointerException
            System.out.println("Unbelievable");
    }
}