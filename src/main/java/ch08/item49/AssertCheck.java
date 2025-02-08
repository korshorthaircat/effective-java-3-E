package ch08.item49;

public class AssertCheck {
    public static void main(String[] args) {
        int x = -1;
        assert x > 0 : "x는 0보다 커야 합니다!";
        System.out.println("프로그램 종료");

        // --ea 옵션 줄 경우 assert 동작
    }
}
