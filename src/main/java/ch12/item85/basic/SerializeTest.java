package ch12.item85.basic;

import java.io.*;

public class SerializeTest {
    public static void main(String[] args) throws Exception {
        // 객체 생성
        Person p1 = new Person("고고", 30);
        Person2 p2 = new Person2("양양", 25);

        // -------- 기본 직렬화/역직렬화
        ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream("person.ser"));
        out1.writeObject(p1);
        out1.close();

        ObjectInputStream in1 = new ObjectInputStream(new FileInputStream("person.ser"));
        Person restored1 = (Person) in1.readObject();
        in1.close();
        // readObject 메서드는 Serializable 구현시 클래스패스안의 거의 모든 타입 객체를 만들어낼 수 있는 사실상 마법같은 생성자다...
        // 바이트스트림을 역직렬화하는 과정에서 이 메서드는 그 타입들 안의 모든 코드를 수행할 수 있다. -> 그 타입의 코드 전체가 공격 범위에 들어가는 것이다.

        System.out.println("복원된 기본 직렬화 객체: " + restored1);

        // -------- 커스텀 직렬화/역직렬화
        ObjectInputStream in2 = new ObjectInputStream(new FileInputStream("person2.ser"));
        Person2 restored2 = (Person2) in2.readObject();
        in2.close();

        ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream("person2.ser"));
        out2.writeObject(p2);
        out2.close();

        System.out.println("복원된 커스텀 직렬화 객체: " + restored2);
    }
}
