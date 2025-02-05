package ch07.item48;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

public class ParallelMersennePrimes {
    static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println("startTime = " + startTime);

        // 스트림을 사용해 처음 20개의 메르센 소수 생성
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
//                .parallel() // 병렬 스트림 사용시 문제 발생! CPU를 90%나 사용하며 출력되지 않는다.
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);

        long endTime = System.nanoTime();
        System.out.println("endTime = " + endTime);
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000 + " ms"); // 실행 시간 출력
    }
}