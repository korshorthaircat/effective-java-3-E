package ch11.item79;

import ch11.item79.fixed.ObservableSet3;
import ch11.item79.observer.SetObserver3;

import java.util.HashSet;

/**
 * 자바 동시성 컬렉션 라이브러리의 copyOnWriteArrayList 사용 테스트
 */
public class Test5 {
    public static void main(String[] args) {
        ObservableSet3<Integer> observableSet = new ObservableSet3<>(new HashSet<>());

        // 집합에 추가된 정숫값을 출력하다가, 그 갑시 23이 되면 자기 자신을 제거(구독해지)하는 관찰자 추가
        observableSet.addObserver(new SetObserver3<>() {
            @Override
            public void added(ObservableSet3<Integer> observableSet, Integer i) {
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
