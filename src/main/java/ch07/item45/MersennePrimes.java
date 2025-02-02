package ch07.item45;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

/**
 * 메르센 수: 2^p -1 형태의 수
 * p가 1이면 해당 메르센 수도 소수일 수 있는데, 이 때의 수를 메르센 소수라 한다.
 */
public class MersennePrimes {
    /**
     * 2부터 시작해 모든 소수를 무한 스트림으로 제공하는 메서드
     */
    static Stream<BigInteger> primes() { // 스트림 반환하는 메서드 이름은 원소의 정체를 알려주는 복수 명사로 쓰기 추천(가독성)
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
        // Stream.iterate 정적팩터리는 매개변수를 두개 받는다. seed:스트림의 첫번째 원소, function:스트림에서 다음 원소를 생성해주는 함수
    }

    public static void main(String[] args) {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE)) // 무한소수 스트림에서 p를 받아 2^p -1 계산하여 메르센수로 변환
                .filter(mersenne -> mersenne.isProbablePrime(50)) // isProbablePrime()은 확률적으로 소수를 판단하며, 인자 50은 신뢰도(소수일 확률)를 높이기 위한 값
                .limit(20)
                // 메르센 소수의 앞에 지수 p를 출력하고 싶지만, 이 값은 초기 스트림에서만 나타나므로 종단 연산(출력)에서 접근할 수 없다.
                // 하지만 중간 연산에서 수행했던 매핑을 거꾸로 수행하여 p를 계산해낼 수 있다. (지수는 숫자를 이진수로 표현 후 몇비트인지 세면 나오기 떄문)
                .forEach(mp -> System.out.println(mp.bitLength() + ": " + mp));

        // 출력 결과:
//        2: 3
//        3: 7
//        5: 31
//        7: 127
//        13: 8191
//        17: 131071
//        19: 524287
//        31: 2147483647
//        61: 2305843009213693951
//        89: 618970019642690137449562111
//        107: 162259276829213363391578010288127
//        127: 170141183460469231731687303715884105727
        //...
    }
}
