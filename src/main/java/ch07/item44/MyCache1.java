package ch07.item44;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap의 removeEldestEntry 재정의하여
 * 10개 초과 시 가장 오래된 항목 삭제하도록 캐시 구현(LRU방식)
 */
public class MyCache1<K, V> extends LinkedHashMap<K, V> {
    public MyCache1() {
        super(10, 0.75f, true);
        // initialCapacity: 초기 크기 설정
        // loadFactor: 해시맵의 로드 팩터 설정
        // accessOrder
            // true: LRU(Least Recently Used) 방식으로 동작하도록 설정, 최근에 사용한 항목을 가장 나중으로 보내고, 오래된 항목부터 삭제
            // false(기본값): FIFO(First In First Out) 방식으로 동작하도록 설정, 삽입 순서 유지하여 가장 먼저 추가된 항목이 삭제됨
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > 10; // 10개 초과 시 가장 오래된 항목 삭제
    }

    public static void main(String[] args) {
        MyCache1<Integer, String> cache = new MyCache1<>();

        // 15개의 항목 추가
        for (int i = 1; i <= 15; i++) {
            cache.put(i, "값" + i); // put 메서드 내부에서 removeEldestEntry를 호출한다.
            System.out.println(": " + i);
        }

        // 현재 맵 상태 출력
        System.out.println("캐시 사이즈: " + cache.size());
        System.out.println("가장 오래된 키: " + cache.entrySet().iterator().next().getKey()); // 삽입 순서 유지하므로(accessOrder=true) 첫번째 엔트리가 제일 오래된 항목임
//        캐시 사이즈: 10
//        가장 오래된 키: 6 (처음 추가된 1~5는 삭제됐음)
    }
}
