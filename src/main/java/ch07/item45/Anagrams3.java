package ch07.item45;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * 스트림을 적당하게 사용함!
 * 코드가 짧고 명확하며 읽기 쉽다.
 *
 * 사전 하나를 훑어 원소 수가 많은 아나그램 그룹들을 출력한다.
 */
public class Anagrams3 {
    public static void main(String[] args) throws IOException {
        List<String> words = Arrays.asList("listen", "silent", "enlist", "inlets", "tinsel", "stone", "notes", "tones", "hello", "world");
        int minGroupSize = 2;

        words.stream()
                .collect(groupingBy(word -> alphabetize(word))) // Map<String, List<...>>
                .values().stream()// Stream<List<String>>
                .filter(group -> group.size() >= minGroupSize)
                .forEach(group -> System.out.println(group.size() + ": " + group)); // 매개변수의 이름을 잘 지어야 스트림 파이프라인의 가독성이 유지된다.
    }

    private static String alphabetize(String s) { // 연산에 적절한 이름을 지어주고, 세부 구현을 주 프로그램 로직 밖으로 빼내 전체적 가독성을 높이기!
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
    // alphabetize 메서드를 스트림을 이용해 구현한다면,
    //      명확성이 떨어지고 잘못 구현한 가능성3도 커진다.
    //      심지어 느려질 수도 있다. 자바가 기본 타입인 char용 스트림을 지원하지 않기 떄문이다.
}