package ch09.item65;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SetPerformanceAnalyzer {
    private static final int ELEMENTS = 100_000; // 테스트할 개수
    private static final int SEARCH_DELETE_TEST_SIZE = 10_000; // 검색, 삭제 테스트 개수

    public static void main(String[] args) {
        // 테스트할 Set 클래스 목록
        List<String> setClasses = Arrays.asList(
                "java.util.HashSet",
                "java.util.LinkedHashSet",
                "java.util.TreeSet"
        );

        // 성능 데이터 Map (클래스명 -> [삽입 시간, 검색 시간, 삭제 시간])
        Map<String, Long[]> performanceResults = new HashMap<>();

        for (String className : setClasses) {
            performanceResults.put(className, analyzeSetPerformance(className));
        }

        // 성능 비교 출력
        System.out.println();
        printRanking(performanceResults, 0, "삽입 속도");
        printRanking(performanceResults, 1, "검색 속도");
        printRanking(performanceResults, 2, "삭제 속도");
//        HashSet이 삽입, 검색, 삭제 속도 모두 가장 빠름 (일반적으로 HashSet이 LinkedHashSet보다 빠르지만 해시 충돌, 데이터 특성, JIT 최적화, JDK 버전 등에 따라 LinkedHashSet이 더 빠를 수도 있음)
//        LinkedHashSet은 HashSet보다 약간 느리지만 입력 순서를 유지
//        TreeSet은 정렬 유지 때문에 속도가 느림
    }

    private static Long[] analyzeSetPerformance(String className) {
        // Set 생성
        Set<String> set = createSetInstance(className);
        if (set == null) return new Long[]{Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};

        System.out.println(">>> " + className + " 성능 테스트 시작...");

        // 랜덤 데이터 생성
        List<String> testData = new ArrayList<>(ELEMENTS);
        for (int i = 0; i < ELEMENTS; i++) {
            testData.add(UUID.randomUUID().toString()); // 무작위 문자열 생성
        }

        // 삽입 속도 측정
        long startTime = System.nanoTime();
        for (String data : testData) {
            set.add(data);
        }
        long insertTime = System.nanoTime() - startTime;
        System.out.println("  - 삽입 속도: " + insertTime / 1_000_000.0 + " ms");

        // 검색 속도 측정
        startTime = System.nanoTime();
        for (int i = 0; i < SEARCH_DELETE_TEST_SIZE; i++) { // 일부 데이터만 검색
            set.contains(testData.get(i));
        }
        long searchTime = System.nanoTime() - startTime;
        System.out.println("  - 검색 속도: " + searchTime / 1_000_000.0 + " ms");

        // 삭제 속도 측정
        startTime = System.nanoTime();
        for (int i = 0; i < SEARCH_DELETE_TEST_SIZE; i++) { // 일부 데이터만 삭제
            set.remove(testData.get(i));
        }
        long deleteTime = System.nanoTime() - startTime;
        System.out.println("  - 삭제 속도: " + deleteTime / 1_000_000.0 + " ms");

        return new Long[]{insertTime, searchTime, deleteTime};
    }

    // 리플렉션을 이용해 Set 인스턴스 생성
    private static Set<String> createSetInstance(String className) {
        try {
            Class<? extends Set<String>> clazz = (Class<? extends Set<String>>) Class.forName(className);
            Constructor<? extends Set<String>> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        } catch (NoSuchMethodException e) {
            System.err.println("No parameterless constructor: " + className);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("Cannot instantiate: " + className);
        }
        return null;
    }

    // 성능 지표에 따른 순위 출력
    private static void printRanking(Map<String, Long[]> results, int index, String metricName) {
        List<Map.Entry<String, Long[]>> sortedResults = new ArrayList<>(results.entrySet());
        sortedResults.sort(Comparator.comparingLong(o -> o.getValue()[index]));

        System.out.println("[" + metricName + " 순위] (낮을수록 빠름)");
        for (int i = 0; i < sortedResults.size(); i++) {
            Map.Entry<String, Long[]> entry = sortedResults.get(i);
            System.out.println((i + 1) + ". " + entry.getKey() + " - " + (entry.getValue()[index] / 1_000_000.0) + " ms");
        }
        System.out.println();
    }
}
