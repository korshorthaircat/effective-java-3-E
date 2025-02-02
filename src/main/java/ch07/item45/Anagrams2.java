package ch07.item45;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * 스트림을 과도하게 사용함!
 * 스트림을 과용하면 프로그램이 읽거나 유지보수하기 어려워진다.
 *
 * 사전 하나를 훑어 원소 수가 많은 아나그램 그룹들을 출력한다.
 */
public class Anagrams2 {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("listen", "silent", "enlist", "inlets", "tinsel", "stone", "notes", "tones", "hello", "world");
        int minGroupSize = 2;

        words.stream().collect(
                        groupingBy(word -> word.chars().sorted()
                                .collect(StringBuilder::new,
                                        (sb, c) -> sb.append((char) c),
                                        StringBuilder::append).toString()))
                // 문자들 정렬, 정렬된 문자를 문자열로 변환한 뒤 같은 아나그램 단어끼리 그룹화
                .values().stream()
                .filter(group -> group.size() >= minGroupSize)
                .map(group -> group.size() + ": " + group)
                .forEach(System.out::println);
    }
}
