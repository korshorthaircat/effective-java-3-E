package ch07.item47;


import java.util.*;

/**
 * 멱집합
 * 멱집합은 어떤 집합의 모든 부분집합을 포함하는 집합이다.
 * 원소가n개인 집합의 멱집합은 2^n개의 원소를 갖는다.
 */public class PowerSet {

    /**
     * 전달받은 집합의 멱집합을 커스텀 컬렉션으로 반환한다.
     */    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s); // 리스트 변환을 통해 인덱스 사용 가능하게 함
        if (src.size() > 30)
            throw new IllegalArgumentException("집합에 원소가 너무 많습니다. " + s);
        // 2^30개의 부분집합 생성시 메모리 사용량 고려

        return new AbstractList<Set<E>>() {
            // AbstractList 사용함으로써, 모든 부부집합을 미리 생성하지 않고 요청이 있을 때 계산하여 변환하는 Lazy Evaluation 기법을 적용
            // 실제로 데이터가 저장되어 있는 것이 아니라, 필요할 때마다(get 호출 시) 부분집합을 계산해서 반환됨
            @Override
            public int size() {
                return 1 << src.size(); // 멱집합의 크기는 2를 원래 집합의 원소 수만큼 거듭제곱한 것과 같다.
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set)o);
            }

            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index != 0; i++, index >>= 1)
                    // index의 각 비트는 집합 내 원소의 포함 여부를 나타냄 (1이면 포함, 0이면 제외)
                    // for문에서 index를 오른쪽으로 한칸씩 이동하면서(2로 나누면서) 한 비트씩 검사
                    if ((index & 1) == 1) // index의 이진수 첫째자리 값이 1인지 확인
                        result.add(src.get(i)); // 일치하면 해당 원소를 결과 집합에 추가
                return result;
            }
        };
    }

    public static void main(String[] args) {
        Set<String> s = Set.of("a", "b", "c");
        System.out.println(PowerSet.of(s));
        // 출력 결과:
//        [[], [a], [b], [a, b], [c], [a, c], [b, c], [a, b, c]]

        // 참고
// get(int index)는 System.out.println()이 리스트를 출력할 때 자동으로 호출된다.
// AbstractList는 부분집합을 미리 계산해서 저장하지 않고 get(int index)가 호출될 때마다 비트 연산을 사용하여 부분집합을 생성한다.
// toString()이 Iterator를 사용하여 리스트를 순회하면서 get(0), get(1), ..., get(size()-1)이 자동으로 여러 번 호출된 것이다.
    }
}
