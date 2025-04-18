package ch12.item85.basic;

import java.io.Serializable;

/**
 * 기본 직렬화
 * implements Serializable시 모든 필드를 자동으로 바이트로 변환해서 저장함
 * 따로 writeObject, readObject 메서드를 작성하지 않아도 됨.
 */
public class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "[Person] name: " + name + ", age: " + age;
    }
}

