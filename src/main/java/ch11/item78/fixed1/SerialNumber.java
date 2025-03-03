package ch11.item78.fixed1;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SerialNumber {
    private static int nextSerialNumber = 0; // 메서드에 synchronized 붙일 경우 volatile 키워드는 불필요해짐. 사용해도 동작에는 문제는 없지만 불필요하다.

    public static synchronized int generateSerialNumber() {
        return nextSerialNumber++;
    }
//    volatile
//    변수의 값을 메인 메모리에서 직접 읽고 쓰도록 보장함.
//    가시성(visibility) 문제 해결: 여러 스레드에서 이 변수 값을 읽을 때 최신 값을 보장.
//    하지만 원자성을 보장하지 않음 → nextSerialNumber++는 읽기 → 증가 → 저장 3단계이므로 volatile만으로는 경쟁 상태(race condition)가 발생할 수 있음.
//
//    synchronized
//    원자성(atomicity) 보장: synchronized 메서드가 호출되면 하나의 스레드만 실행 가능.
//    메서드 내부에서 실행되는 모든 연산이 하나의 블록처럼 실행되도록 보장.
//            동시에 가시성(visibility) 문제도 해결함 → 메서드가 끝난 후 변수 값이 다른 스레드에서 최신 상태로 보임.


    public static void main(String[] args) {
        Set<Integer> serialNumbers = new HashSet<>();

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
