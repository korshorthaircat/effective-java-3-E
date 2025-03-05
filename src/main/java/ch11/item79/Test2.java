package ch11.item79;

import ch11.item79.broken.ObservableSet1;
import ch11.item79.fixed.ObservableSet2;
import ch11.item79.observer.SetObserver1;
import ch11.item79.observer.SetObserver2;

import java.util.HashSet;

/**
 * 동기화 영역 내에서 외계인메서드(added)호출시 예외 터지는 경우
 */
public class Test2 {
    public static void main(String[] args) {
        ObservableSet1<Integer> set = new ObservableSet1<>(new HashSet<>());

        // 집합에 추가된 정숫값을 출력하다가, 그 갑시 23이 되면 자기 자신을 제거(구독해지)하는 관찰자 추가
        set.addObserver(new SetObserver1<Integer>() {
            @Override
            public void added(ObservableSet1<Integer> observableSet, Integer integer) {
                System.out.println(integer);
                if (integer == 23)
                    observableSet.removeObserver(this);
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
    /**
     * 실행결과: 23까지 출력 후 ConcurrentModificationException 터짐
     *
     * 이유: 관찰자의 added 메서드 호출 일어난 시점이, ObservableSet의 notifyElementAdded에서 observers 리스트 순회하는 시점이기 때문.
     * added메서드는 remobeObserver를 호출하고, 이 메서드는 다시 observers.remove 메서드를 호출한다.
     * 리스트에서 원소를 제거하려 하는데 마침 지금은 이 리스트를 순회하는 도중이므로 허용되지 않는 동작이다.
     * notifyElmentAdded 메서드에서 수행하는 순회가 동기화 블록 안에 있으므로
     * 동시 수정이 일어나지 않도록 보장하지만, 정작 자신이 콜백을 거쳐 되돌아와 수정하는것까지 막지는 못한다.
     */
}

