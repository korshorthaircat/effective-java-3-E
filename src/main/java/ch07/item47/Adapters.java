package ch07.item47;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// Stream <-> Iterable 변환 어댑터
public class Adapters {
    /**
     * Stream<E>를 Iterable<E>로 중개해주는 어댑터 메서드
     */
    public static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator; // return stream.iterator();
    }

    /**
     * Iterable<E>를 Stream<E>로 중개해주는 어댑터 메서드
     */
    public static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static void main(String[] args) {
        // (1)
        Stream<String> stream = Stream.of("A", "B", "C");
        Iterable<String> resultIterable = Adapters.iterableOf(stream);

        for (String s : resultIterable) {
            System.out.println(s);
        }

        // (2)
        List<String> list = Arrays.asList("X", "Y", "Z");
        Stream<String> resultStream = Adapters.streamOf(list);

        resultStream.forEach(System.out::println);
    }
}
