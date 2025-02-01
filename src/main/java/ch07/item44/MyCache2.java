package ch07.item44;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * /**
 *  * LinkedHashMap의 removeEldestEntry 재정의하여
 *  * 100개 초과 시 가장 오래된 항목 삭제하도록 캐시 구현
 *
 *  LRU 방식, FIFO 방식 비교
 */
class MyCache2<K, V> extends LinkedHashMap<K, V> {
    private final int MAX_ENTRIES;

    public MyCache2(int maxEntries, boolean accessOrder) {
        super(maxEntries, 0.75f, accessOrder); // accessOrder에 따라 LRU 또는 FIFO 결정
        this.MAX_ENTRIES = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_ENTRIES; // 캐시 크기를 초과하면 가장 오래된 항목 삭제
    }

    public void access(K key) {
        if (containsKey(key)) {
            get(key); // LRU 모드에서 접근 순서를 갱신하기 위한 호출
        }
    }

    public static void main(String[] args) {
        System.out.println("FIFO");
        MyCache2<Integer, String> fifoCache = new MyCache2<>(3, false);
        testCache(fifoCache);

        System.out.println();
        System.out.println("LRU");
        MyCache2<Integer, String> lruCache = new MyCache2<>(3, true);
        testCache(lruCache);
    }

    private static void testCache(MyCache2<Integer, String> cache) {
        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        System.out.println("초기 상태: " + cache);

        cache.access(1); // 1번 키 접근 (LRU에서는 가장 최근 사용된 것으로 처리됨)

        cache.put(4, "D"); // 새로운 항목 추가 (캐시 크기 초과 -> 제거 발생)
        System.out.println("새 항목 추가 이후: " + cache);
    }
}