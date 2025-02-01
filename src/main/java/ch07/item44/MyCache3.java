package ch07.item44;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap의 removeEldestEntry 재정의하여
 * 10개 초과 시 가장 오래된 항목 삭제하도록 캐시 구현(LRU방식)
 *
 * 직접 선언한 함수형 인터페이스 사용
 */
public class MyCache3<K, V> extends LinkedHashMap<K, V> {
    private static final int MAX_SIZE = 10;
    private final EldestEntryRemovalFunction<K, V> removalFunction;

    public MyCache3(EldestEntryRemovalFunction<K, V> removalFunction) {
        super(MAX_SIZE, 0.75F, true);
        this.removalFunction = removalFunction;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return removalFunction.remove(this, eldest);
    }

    public static void main(String[] args) {
        // 람다 표현식을 사용하여 삭제 정책을 정의
        MyCache3<Integer, String> cache = new MyCache3<>((map, eldest) -> map.size() > MAX_SIZE);

        // 15개의 아이템 추가
        for (int i = 1; i <= 15; i++) {
            cache.put(i, "값" + i);
            System.out.println(": " + i);
        }

        // 현재 상태 출력
        System.out.println("캐시 사이즈: " + cache.size());
        System.out.println("가장 오래된 키: " + cache.entrySet().iterator().next().getKey()); // 삽입 순서 유지하므로(accessOrder=true) 첫번째 엔트리가 제일 오래된 항목임
    }
}
