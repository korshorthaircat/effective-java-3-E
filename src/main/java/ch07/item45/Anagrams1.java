package ch07.item45;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 사전 하나를 훑어 원소 수가 많은 아나그램 그룹들을 출력한다.
 */
public class Anagrams1 {
    public static void main(String[] args) throws IOException {
//        File dictionary = new File(args[0]);
//        int minGroupSize = Integer.parseInt(args[1]);
        List<String> words = Arrays.asList("listen", "silent", "enlist", "inlets", "tinsel", "stone", "notes", "tones", "hello", "world");
        int minGroupSize = 2;
//        listen, silent, enlist, inlets, tinsel  => 같은 그룹
//        stone, notes, tones  => 같은 그룹
//        hello => 혼자만 있는 그룹. group.size() == 1
//        world => 혼자만 있는 그룹

        Map<String, Set<String>> groups = new HashMap<>();
//        try (Scanner s = new Scanner(dictionary)) {
//            while (s.hasNext()) {
//                String word = s.next();
//                groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word);
//            }
//        }
        for (String word : words) {
            groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word); // key가 이미 존재하면 기존 값을 반환한다. key가 존재하지 않으면 `mappingFunction`을 실행하여 새로운 값을 계산하고 이를 맵에 추가한 후 반환한다.
        }

        for (Set<String> group : groups.values())
            if (group.size() >= minGroupSize)
                System.out.println(group.size() + ": " + group);
    }

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
