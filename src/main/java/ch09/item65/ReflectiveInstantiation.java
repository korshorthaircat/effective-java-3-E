package ch09.item65;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

/**
 * 제공된 클래스 이름을 기반으로 Set<String>타입 객체 동적 생성
 */
public class ReflectiveInstantiation {
    // 리플렉션으로 인스턴스를 생성하되, 인터페이스로 참조해 활용해야 한다.
    public static void main(String[] args) {
        String[] names = {"java.util.HashSet", "apple", "cherry", "banana", "cherry"};
//        String[] names = {"java.util.TreeSet", "apple", "cherry", "banana", "cherry"}; // 정렬 순서 유지
//        String[] names = {"java.util.LinkedHashSet", "apple", "cherry", "banana", "cherry"}; // 입력 순서 유지
//        String[] names = {"java.util.MySet", "apple", "banana", "cherry", "cherry"}; // Class not found.

        // 클래스 이름을 Class 객체로 변환
        Class<? extends Set<String>> clazz = null;
        try {
            clazz = (Class<? extends Set<String>>) Class.forName(names[0]); // 비검사 형변환!
        } catch (ClassNotFoundException e) {
            fatalError("Class not found.");
        }

        // 생성자를 얻는다.
        Constructor<? extends Set<String>> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fatalError("No parameterless constructor");
        }

        // 집합의 인스턴스를 만든다.
        Set<String> set = null;
        try {
            set = constructor.newInstance();
        } catch (IllegalAccessException e) {
            fatalError("Constructor not accessible");
        } catch (InstantiationException e) {
            fatalError("Class not instantiable.");
        } catch (InvocationTargetException e) {
            fatalError("Constructor threw " + e.getCause());
        } catch (ClassCastException e) {
            fatalError("Class doesn't implement Set");
        }
        // 리플렉션 예외 각각을 잡는 대신 모든 리플렉션 예외 상위 클래스인 ReflectiveOperationException을 잡아 코드 길이를 줄일 수 있음

        set.addAll(Arrays.asList(names).subList(1, names.length)); // 객체가 일단 생성된 이후는, 여타의 Set 인스턴스 사용할떄랑 똑같다.
        System.out.println(set);
    }

    private static void fatalError(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
}
