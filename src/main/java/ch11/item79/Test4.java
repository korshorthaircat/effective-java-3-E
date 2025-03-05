package ch11.item79;

import ch11.item79.fixed.ObservableSet2;
import ch11.item79.observer.SetObserver2;

import java.util.HashSet;

/**
 * 외계인메서드가 동기화 영역 바깥에서 호출될 경우(열린 호출) 테스트
 */
public class Test4 {
    public static void main(String[] args) {
        ObservableSet2<Integer> observableSet = new ObservableSet2<>(new HashSet<>());

        // 집합에 추가된 정숫값을 출력하다가, 그 갑시 23이 되면 자기 자신을 제거(구독해지)하는 관찰자 추가
        observableSet.addObserver(new SetObserver2<>() {
            @Override
            public void added(ObservableSet2<Integer> observableSet, Integer i) {
                System.out.println(i);
                if (i == 23)
                    observableSet.removeObserver(this);
            }
        });

        for (int i = 0; i < 100; i++)
            observableSet.add(i);
    }
    /**
     * 실행결과: 예외발생, 교착상태가 사라짐
     */
}
