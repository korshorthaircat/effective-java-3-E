package ch11.fixed1;

import java.util.concurrent.TimeUnit;

/**
 * 수정한 코드 예시 1)
 * 공유중인 가변 데이터에 대한 읽기, 쓰기를 모두 동기화 처리
 *
 * -> 가시성 문제 및 JIT 최적화에 의한 응답 불가(liveness filure) 문제 해결
 */
public class StopThread {
    private static boolean stopRequested;

    /**
     * 읽기, 쓰기 메서드 모두 동기화되하지 않으면, 동작을 보장하지 않는다.
     *
     * 만일 쓰기 메서드만 동기화하고, 읽기 메서드를 동기화하지 않으면 -> 가시성(visibility) 문제가 발생할 수 있다.
     * Java 메모리 모델(JMM)에서는 한 스레드에서 변경한 값을 다른 스레드가 즉시 볼 수 있다는 보장이 없다.
     * 이유는 CPU 캐시와 메인 메모리 사이의 일관성 문제 때문이다.
     *
     */
    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }


    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}  