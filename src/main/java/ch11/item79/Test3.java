package ch11.item79;

import ch11.item79.broken.ObservableSet1;
import ch11.item79.observer.SetObserver1;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 동기화 영역 내에서 외계인메서드(added)호출시 교착상태에 빠지는 경우
 */
public class Test3 {
    public static void main(String[] args) {
        ObservableSet1<Integer> set = new ObservableSet1<>(new HashSet<>());

        // 구독해지를 하는 관찰자를 추가함
        // removeObserver를 직접 호출하지 않고, 실행자 서비스(ExecutorService)를 사용해 다른 스레드에 부탁하기
        // (쓸데없이 백그라운드 스레드를 사용하는 관찰자)
        set.addObserver(new SetObserver1<>() {
            public void added(ObservableSet1<Integer> observableSet, Integer integer) {
                System.out.println(integer);
                if (integer == 23) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    try {
                        executorService.submit(() -> observableSet.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) { // 다중 캐치는 자바7부터 지원
                        throw new AssertionError(ex);
                    } finally {
                        executorService.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
/**
 * 실행결과: 23까지 출력. 교착 상태
 *
 * 이유: 백그라운드 스레드가 observableSet.removeObserver 호출하며 관찰자를 잠그려 시도하지만 락을 얻을 수 없다.
 * 메인 스레드가 이미 락을 쥐고 있기 때문이다.
 * 그와 동시에 메인 스레드는 백그라운드스레드가 관찰자를 제거하기만을 기다리는 중이다. 바로 교착상태다.
 */