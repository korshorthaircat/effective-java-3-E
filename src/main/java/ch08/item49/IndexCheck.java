package ch08.item49;

import java.util.Objects;

public class IndexCheck {
    public static void main(String[] args) {
        int[] array = {10, 20, 30, 40, 50};

        // (1) checkIndex(int index, int length)
        int index = 3;

        // 유효한 인덱스인지 검사
        Objects.checkIndex(index, array.length);
        System.out.println("값: " + array[index]);

//        Objects.checkIndex(5, 5); // Exception 발생 (배열 크기가 5인데 index 5는 유효하지 않음)
//        Objects.checkIndex(-1, 5); // Exception 발생 (음수 인덱스)

        // (2) checkFromToIndex(int fromIndex, int toIndex, int length)
        int fromIndex = 1;
        int toIndex = 4;

        // 유효한 범위인지 확인
        Objects.checkFromToIndex(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; i++) {
            System.out.print(array[i] + " "); // 20 30 40
        }
//        Objects.checkFromToIndex(3, 6, 5); // Exception 발생 (toIndex 6은 범위를 초과)
//        Objects.checkFromToIndex(4, 2, 5); // Exception 발생 (fromIndex가 toIndex보다 큼)
//        Objects.checkFromToIndex(-1, 3, 5); // Exception 발생 (음수 인덱스)

        // (3) checkFromIndexSize(int fromIndex, int size, int length)
        int fromIdx = 1;
        int size = 3;

        // 유효한 범위인지 확인
        Objects.checkFromIndexSize(fromIdx, size, array.length);
        for (int i = fromIndex; i < fromIndex + size; i++) {
            System.out.print(array[i] + " "); // 20 30 40
        }
//        Objects.checkFromIndexSize(2, 4, 5); // Exception 발생 (2 + 4 > 5)
//        Objects.checkFromIndexSize(4, 2, 5); // Exception 발생 (4 + 2 > 5)
//        Objects.checkFromIndexSize(-1, 3, 5); // Exception 발생 (음수 인덱스)

    }
}
