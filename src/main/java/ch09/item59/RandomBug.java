package ch09.item59;

import java.util.Random;

// 잘못된 랜덤 숫자 생성의 예
public class RandomBug {
    static Random random = new Random();

    /**
     * [흔하지만 문제가 심각한 코드]
     * n 이하의 랜덤 양의 정수 반환
     * @param n
     */
    static int random(int n) {
        return Math.abs(random.nextInt()) % n;
        // random.nextInt()로 int 범위(-2^31 ~ 2^31-1)에서 임의의 값 반환
        // -> Math.abs()로 절대값 얻음
        // -> n으로 나눈 나머지 반환하여 0부터 n-1 사이의 값 생성
    }
    // 문제점
    // (1) Math.abs(rnd.nextInt())가 Integer.MIN_VALUE (-2^31)를 받으면 여전히 음수가 된다. 음수를 나머지 연산한 결과 음수 값이 나오게된다.
    // (2) rnd.nextInt()는 int 범위의 모든 값을 균등하게 생성하지만, Math.abs(rnd.nextInt()) % n의 결과는 특정 값이 더 자주 등장할 가능성이 있다.
    // n이 그리 크지 않은 2의 제곱수라면, 얼마 지나지 않아 같은 수열이 반복된다.
    // n이 2의 제곱수가 아니라면, 몇몇 숫자가 평균적으로 더 자주 반환된다. (n 값이 크면 이 현상이 더 두드러진다.)

    // Math.abs(rnd.nextInt()) % n가 특정 값을 더 자주 반환하는 이유
    //  rnd.nextInt()는 -2^31부터 2^31-1까지의 값을 생성하지만, Math.abs()를 적용하면 양수 값이 음수보다 하나 더 많다.
    // → Integer.MIN_VALUE의 경우 Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE가 되어 음수가 남아 문제가 발생한다.

    // n이 2의 거듭제곱수가 아니면, modulo bias(모듈로 편향)가 생긴다.
    // → rnd.nextInt()가 균등 분포를 가진다고 해도, 나머지 연산을 하면 특정 숫자가 더 자주 선택되는 현상이 발생한다.

    public static void main(String[] args) {
        // 랜덤 값의 분포가 고르지 않은 것 확인하기
        int n = 2 * (Integer.MAX_VALUE / 3); // 전체 int 범위에서 약 2/3에 해당하는 값
        int low = 0;
        for (int i = 0; i < 1000000; i++) // 100만번 검사
            if (random(n) < n/2) // 난수 생성한 결과가 전체 범위의 딱 중간값보다 작은지 검사. 생성된 난수가 고르게 분포되어 있다면, 중간값보다 작은 수가 나올 확률은 50%여야 함.
                low++;
        System.out.println(low);
        // 출력 결과: 666753
        // 메서드가 이상적으로 동작하면 약 50만개가 출력되어야 하나, 실제로 돌려보면 666666에 가까운 값을 얻는다. 약 66.67%가 중간값보다 작음!
        // 작은 값들이 더 자주 나오는 편향이 있다는 의미

        int n2 = 3 * (Integer.MAX_VALUE / 5); // 전체 int 범위에서 약 3/5에 해당하는 값
        int low2 = 0;
        for (int i = 0; i < 1000000; i++) // 100만번 검사
            if (random(n2) < n2/2) // 난수 생성한 결과가 전체 범위의 딱 중간값보다 작은지 검사. 생성된 난수가 고르게 분포되어 있다면, 중간값보다 작은 수가 나올 확률은 50%여야 함.
                low2++;
        System.out.println(low2);
        // 출력 결과: 600115
        // 메서드가 이상적으로 동작하면 약 50만개가 출력되어야 하나, 실제로 돌려보면 600000에 가까운 값을 얻는다. 약 60%가 중간값보다 작음!
        // 작은 값들이 더 자주 나오는 편향이 있다는 의미

        // Random의 nextInt(n)을 사용하는 것이 최선의 방법이다! 라이브러리를 익히고 사용하자.
        int low3 = 0;
        for (int i = 0; i < 1000000; i++)
            if (random.nextInt(n) < n/2)
                low3++;
        System.out.println(low3);
        // 출력 결과: 500039
        // 이상적으로 동작하여 약 50만개 출력됨.
    }
}
