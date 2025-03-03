package ch11.fixed3;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * java.util.cuncurrent.atomic의 AtomicLong 사용
 *
 * volatile은 동기화의 두 기능 중, 통신 쪽만 지원하지만
 * java.util.cuncurrent.atomic 패키지는 원자성(배타적 실행)까지 지원함
 * 성능도 우수함
 */
public class SerialNumber {
    private static final AtomicLong nextSerialNumber = new AtomicLong();

    public static synchronized long generateSerialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    public static void main(String[] args) {
        Set<Long> serialNumbers = new HashSet<>();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 여러 개의 스레드가 동시에 serial number를 생성
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                long num = generateSerialNumber();
//                System.out.println("Generated Serial: " + num);

                synchronized (serialNumbers) { // Set은 스레드 안전하지 않으므로 동기화
                    if (!serialNumbers.add(num)) {
                        System.out.println("중복된 SerialNumber 발견: " + num);
                    }
                }
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("총 생성된 Serial 개수: " + serialNumbers.size());
    }
}
