package ch11.broken;

import java.util.concurrent.TimeUnit;

/**
 * 공유중인 가변 데이터에 대하여 쓰기에만 동기화 처리
 *
 * 만일 쓰기 메서드만 동기화하고, 읽기 메서드를 동기화하지 않으면 -> 가시성(visibility) 문제가 발생할 수 있다.
 *      Java 메모리 모델(JMM)에서는 한 스레드에서 변경한 값을 다른 스레드가 즉시 볼 수 있다는 보장이 없다.
 *      이유는 CPU 캐시와 메인 메모리 사이의 일관성 문제 때문이다.
 */
public class StopThread2 {
    private static boolean stopRequested;

    private static synchronized void requestStop() { // 쓰기만 동기화
        stopRequested = true;
    }
//    synchronized 키워드를 사용시, 해당 메서드를 호출하는 스레드는 JVM이 보장하는 "동기화 블록"을 진입/이탈할 때 메모리 가시성을 보장받음
//    즉, synchronized 블록을 빠져나올 때, 해당 스레드가 변경한 변수의 값이 메인 메모리에 반영됨
//    그리고 다른 스레드가 synchronized 블록에 진입할 때 최신 값을 가져올 수 있도록 보장함


    private static boolean stopRequested() { // 읽기는 동기화 X
        return stopRequested;
    }


    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested()) // 읽기 메서드는 동기화되지 않았으므로, 최신값이 반영되지 않은 채 CPU 캐시에서 읽을 가능성이 큼
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop(); // stopRequested = true가 메모리에 반영되도록 강제됩
    }
}  