package ch07.item44;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * LinkedHashMap의 removeEldestEntry 재정의하여
 * 10개 초과 시 가장 오래된 항목 삭제하도록 캐시 구현(LRU방식)
 *
 * 표준 함수형 인터페이스 사용 - BiPredicate
 */
public class MyCache4<K, V> extends LinkedHashMap<K, V> {
    private static final int MAX_SIZE = 10;
    private final BiPredicate<Map<K, V>, Map.Entry<K, V>> removalPredicate;

    public MyCache4(BiPredicate<Map<K, V>, Map.Entry<K, V>> removalPredicate) {
        super(MAX_SIZE, 0.75F, true);
        this.removalPredicate = removalPredicate;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return removalPredicate.test(this, eldest);
    }

    public static void main(String[] args) {
        MyCache4<Integer, String> cache = new MyCache4<>((map, eldest) -> map.size() > MAX_SIZE);

        // 15개의 항목 추가
        for (int i = 1; i <= 15; i++) {
            cache.put(i, "값" + i); // put 메서드 내부에서 removeEldestEntry를 호출한다.
            System.out.println(": " + i);
        }

        // 현재 맵 상태 출력
        System.out.println("캐시 사이즈: " + cache.size());
        System.out.println("가장 오래된 키: " + cache.entrySet().iterator().next().getKey()); // 삽입 순서 유지하므로(accessOrder=true) 첫번째 엔트리가 제일 오래된 항목임
    }
}
