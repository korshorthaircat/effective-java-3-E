package ch11.item78.fixed2;

import java.util.concurrent.TimeUnit;

/**
 * 수정한 코드 예시 2)
 * 공유중인 가변 데이터를 volatile로 선언
 *
 * -> 배타적 수행과는 상관 없지만(스레드간 통신 기능과만 관련),
 * volatile을 사용하면 쓰기 연산이 즉시 메모리에 반영되고, 다른 스레드에서도 항상 최신 값을 읽도록 보장됨.
 */
public class StopThread {
    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
