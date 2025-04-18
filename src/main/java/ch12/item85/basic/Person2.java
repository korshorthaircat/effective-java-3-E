package ch12.item85.basic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person2 implements Serializable {
    private String name;
    private transient int age; // transient: 기본 직렬화에선 저장되지 않음

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();          // name은 기본 방식으로 저장
        out.writeInt(age);                 // age는 직접 저장
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();            // name 복원
        age = in.readInt();                // age 복원
    }

    public String toString() {
        return "[Person2] name: " + name + ", age: " + age;
    }
}
