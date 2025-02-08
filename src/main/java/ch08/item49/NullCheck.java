package ch08.item49;

import java.util.Objects;

public class NullCheck {
    public static void main(String[] args) {
        String name = null;
//        String resultName = Objects.requireNonNull(name, "이름은 null일 수 없습니다.");
//        String resultName = Objects.requireNonNull(name, () -> "이름은 null일 수 없습니다.");
        // NullPointerException: 이름은 null일 수 없습니다.

        String safeName = Objects.requireNonNullElse(name, "기본값");
        System.out.println(safeName); // 기본값

        String safeName2 = Objects.requireNonNullElseGet(name, () -> "기본값 생성");
        System.out.println(safeName2); // 기본값 생성
    }
}