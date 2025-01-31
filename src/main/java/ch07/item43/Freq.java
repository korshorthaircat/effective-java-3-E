package ch07.item43;

import java.util.Map;
import java.util.TreeMap;

public class Freq {
    public static void main(String[] args) {
        String[] strArr = {"cat", "cat", "cat", "Dog", "Dog", "lion", "tiger"};

        Map<String, Integer> frequencyMap = new TreeMap<>();
        
        for (String s : strArr)
            frequencyMap.merge(s, 1, (count, incr) -> count + incr); // 람다
        // java8에 Map에 추가된 merge 메서드는 키, 값, 함수를 인수로 받으며
        // 주어진 키가 맵에 아직 없다면 주어진 (키, 값) 쌍을 저장하고
        // 키가 이미 있다면 세 번째 인수로 받은 함수를 현재 값과 주어진 값에 적용한 다음, 그 결과로 현재 값을 덮어쓴다. 즉, (키, 함수의 결과)쌍을 저장한다.


        System.out.println(frequencyMap);
        frequencyMap.clear();

        for (String s : strArr)
            frequencyMap.merge(s, 1, Integer::sum); // 메서드 참조
        // 자바 8에서 Integer(와 모든 기본타입의 박싱 타입)은 정적 메서드 sum을 제공하므로,
        // 위의 람다 대신 이 메서드의 참조를 전달시 똑같은 결과를 얻을 수 있다.

        System.out.println(frequencyMap);
    }
}
