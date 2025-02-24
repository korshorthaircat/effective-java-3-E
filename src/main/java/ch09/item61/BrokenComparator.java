package ch09.item61;

import java.util.Comparator;

/**
 * 잘못 구현된 Comparator
 */
public class BrokenComparator {
    public static void main(String[] args) {
        // 잘못 구현된 Comparator
        Comparator<Integer> naturalOrder1 = (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);
        // 문제 원인:
        // i < j 검사시, i와 j가 참조하는 오토박싱된 Integer 인스턴스는 기본타입값으로 변환됨.
        // 이후 i == j 검사시 객체 참조의 식별성을 검사함. i와 j가 서로 다른 Integer인스턴스라면 값이 같더라도 검사 결과 false 리턴
        // 즉, 박싱된 기본 타입에 == 연산자 씀으로써 발생하는 오류

        // 제대로 구현한 Comparator
        Comparator<Integer> naturalOrder2 = (iBoxed, jBoxed) -> {
            int i = iBoxed, j = jBoxed; // 지역 변수를 두 개 두어, 각각 박싱된 Integer 매개변수의 값을 기본타입 정수로 저장함
            return i < j ? -1 : (i == j ? 0 : 1);
        };

        int result1 = naturalOrder1.compare(new Integer(42), new Integer(42));
        System.out.println("result1 = " + result1);
        // 출력 결과
//        result1 = 1
        // 두 인스턴스의 값이 같으므로 0을 출력해야 하지만, 1을 출력한다.

        int result2 = naturalOrder2.compare(new Integer(42), new Integer(42));
        System.out.println("result2 = " + result2);
        // 출력 결과
//        result2 = 0
    }
}
