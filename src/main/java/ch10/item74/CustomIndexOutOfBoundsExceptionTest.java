package ch10.item74;

import java.util.ArrayList;
import java.util.List;

public class CustomIndexOutOfBoundsExceptionTest {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        try {
            System.out.println("array[10] = " + array[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }

        try {
            checkIndex(array, 10);
        } catch (Exception e) {
            System.out.println(e);
        }

        List<Integer> integerListImmutable = List.of(1, 2, 3, 4, 5);
//        integerListImmutable.add(100); // Immutable object is modified
        System.out.println("Class of List: " + integerListImmutable.getClass().getName());
        // 구현체: java.util.ImmutableCollections$ListN
        try {
            System.out.println(integerListImmutable.get(10));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }
        // IndexOutOfBoundsException이 발생할 것으로 예상했으나 ArrayIndexOutOfBoundsException이 출력됨
        // ImmutableCollections의  E get(int index) 구현부에 도달하지도 않음. Objects.checkIndex(index, size)를 탈 수 없음
        // 원인 1) JIT(Just-In-Time) 컴파일러 최적화 때문에?
        // 원인 2) List.of()의 내부 구현이 배열을 직접 사용하고 있어서?
        // 원인 3) 특정 JDK 버전에서 get(int index)가 최적화되었을 가능성

        List<Integer> integerList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        try {
            System.out.println(integerList.get(10));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }

    private static void checkIndex(int[] array, int index) {
        if (index < 0 || index >= array.length) {
            throw new CustomIndexOutOfBoundsException(0, array.length, index);
        }
        System.out.println("올바른 인덱스: " + index);
    }

}
