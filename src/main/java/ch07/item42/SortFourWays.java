package ch07.item42;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class SortFourWays {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("aaaaa", "bbbb", "ccc", "dd", "e");

        // (1) 과거의 자바: 익명 클래스의 인스턴스를 함수 객체로 사용 - 낡은 기법!!!
        Collections.sort(words, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        System.out.println(words);
        Collections.shuffle(words);

        // (2) 자바8 이후: 람다식을 함수 객체로 사용 - 익명 클래스를 대체했다!!!
        Collections.sort(words,
                (s1, s2) -> Integer.compare(s1.length(), s2.length()));
        System.out.println(words);
        Collections.shuffle(words);

        // (3) 람다 자리에 비교자 생성 메서드 사용하여 코드를 더 간결하게 만들기(w/메서드 참조)
        Collections.sort(words, comparingInt(String::length));
        System.out.println(words);
        Collections.shuffle(words);

        // (4) List 인터페이스의 디폴트 메서드 sort를 이용해 코드를 더 간결하게 만들기
        words.sort(comparingInt(String::length));
        System.out.println(words);
    }
}
