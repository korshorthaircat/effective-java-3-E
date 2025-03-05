package ch11.item79;

import ch11.item79.broken.ObservableSet1;

import java.util.HashSet;

/**
 * 기본적인 동작 테스트
 */
public class Test1 {
    public static void main(String[] args) {
        ObservableSet1<Integer> observableSet = new ObservableSet1<>(new HashSet<>());

        observableSet.addObserver((s, e) -> System.out.println(e));

        for (int i = 0; i < 100; i++)
            observableSet.add(i);
    }
}
