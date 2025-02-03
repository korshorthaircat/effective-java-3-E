package ch07.item46;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

// Frequency table examples showing improper and proper use of stream (Page 210-11)
public class Freq {
    public static void main(String[] args) throws FileNotFoundException {
        String[] strArr = {"cat", "cat", "cat", "Dog", "Dog", "lion", "tiger"};
        Stream<String> words = Stream.of(strArr);

//        // 스트림 패러다임을 이해하지 못한 채 API만 사용하는 경우 - 따라하지 말 것!
//        Map<String, Long> frequencyMap = new HashMap<>();
//        words.forEach(word -> {
//            frequencyMap.merge(word.toLowerCase(), 1L, Long::sum);
//        });


        // 스트림을 제대로 활용하여 빈도표 초기화하기
        Map<String, Long> frequencyMap;
        frequencyMap = words
                .collect(groupingBy(String::toLowerCase, counting()));

        System.out.println(frequencyMap);

        // 빈도표에서 가장 흔한 단어 2개를 뽑아내는 파이프라인
        List<String> topTen = frequencyMap.keySet().stream()
                .sorted(comparing(frequencyMap::get).reversed())
                .limit(2)
                .collect(toList());

        System.out.println(topTen);
    }
}
