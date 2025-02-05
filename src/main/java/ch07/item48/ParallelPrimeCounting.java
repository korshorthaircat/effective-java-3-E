package ch07.item48;

import java.math.BigInteger;
import java.util.stream.LongStream;

public class ParallelPrimeCounting {
    /**
     * n보다 작거나 같은 소수의 개수 반환
     *
     * @param n
     * @return n보다 작거나 같은 소수의 개수
     */
    static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
    // 병렬화에 적합하다.
    // 이유: 
    // LongStream.rangeClosed(2, n)이 원 타입 스트림이므로, 내부적으로 효율적인 분할(split)이 가능함
    // count()는 순서를 유지할 필요 없는 연산임
    // 각 숫자에 대해 다른 숫자와 독립적으로 소수 판별 연산을 하면 되므로, 경합 조건이 발생하지 않음


    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println("startTime = " + startTime);

        System.out.println(pi(10_000_000));

        long endTime = System.nanoTime();
        System.out.println("endTime = " + endTime);
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");

        // 출력 결과:
//        (1) 병렬화하지 않을 때
//         startTime = 105715665733916
//        664579
//        endTime = 105731267078666
//        Execution Time: 15601 ms
//
//        (2) 병렬화했을 때 - parallel()
//        startTime = 105779176134458
//        664579
//        endTime = 105783155160291
//        Execution Time: 3979 ms
    }
}
