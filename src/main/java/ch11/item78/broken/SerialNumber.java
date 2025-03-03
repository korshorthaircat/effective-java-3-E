package ch11.item78.broken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 잘못된 코드!
 * volatile은 주의해 사용해야 함
 */
public class SerialNumber {
    private static volatile int nextSerialNumber = 0;

    public static int generateSerialNumber() {
        return nextSerialNumber++; // Non-atomic operation on volatile field 'nextSerialNumber'
        // 세 가지 연산으로 이루어졌기 때문.nextSerialNumber 값을 읽고, 값을 증가시키고, 새로운 값을 저장함
        // 여러 스레드가 동시에 실행하면 이 연산들 사이를 비집고 들어와 값이 덮어씌워지는 문제가 발생하여 중복된 시리얼 번호가 생성될 수 있음. -> 안전실패(safety failure)
    }

    public static void main(String[] args) {
        Set<Integer> serialNumbers = Collections.synchronizedSet(new HashSet<>());

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 여러 개의 스레드가 동시에 serial number를 생성
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                int num = generateSerialNumber();
//                System.out.println("Generated Serial: " + num);

                if (!serialNumbers.add(num)) {
                    System.out.println("중복된 SerialNumber 발견: " + num);
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
