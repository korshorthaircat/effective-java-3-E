package ch11.item78.broken;

import java.util.concurrent.TimeUnit;

/**
 * 잘못된 코드 예시)
 *
 * 공유중인 가변 데이터에 접근할 때 동기화를 제거함
 * (boolean 필드를 읽고 쓰는 작업이 원자적이므로, 괜찮은가? -> NO!)
 */
public class StopThread1 {
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1); // 내부적으로 Thread.sleep(1000)과 동일한 동작
        // TimeUnit: java.util.concurrent 패키지에 포함된 시간 단위 변환 및 제어를 위한 열거형(enum)
        stopRequested = true;
    }
}
/**
 * 실행결과: 프로그램이 종료되지 않음
 * 동기화하지 않음으로써 메인스레드가 수정한 값(stopRequested)을 백그라운드 스레드가 언제쯤 보게 될지 보증할 수 없다.
 * 가시성 문제 발생.
 *
 * 이 프로그램은 응답 불가(liveness filure)상태가 되어 더 이상 진전이 없다.
 *  OpenJDK의 서버 VM이 HotSpot JIT 컴파일러의 최적화 기법 중 하나인 호스팅 (hoisting) 을 적용할 가능성이 있기 때문
 *      호이스팅(hoisting):루프 내부에서 변경되지 않는 조건을 루프 밖으로 이동 시키는 최적화 기법
 *      JIT 컴파일러는 stopRequested가 변경되지 않는다고 가정하고, while (!stopRequested)의 값을 캐싱하여 루프 밖으로 빼낼 수도 있음
 *          예) if (!stopRequested) { while (true) {i++;} }
 * 호이스팅이 적용되면 backgroundThread에서 stopRequested를 한 번 읽고(캐싱) 반복문을 실행할 때 계속 false인 상태로 유지할 가능성이 있음. 이렇게 되면 stopRequested = true;를 main 스레드에서 변경해도 backgroundThread에서 그 변경 사항을 감지하지 못해 무한 루프에 빠지게 됨
 */