package ch12.item87;

import java.io.*;

public class StringList2Test {
    public static void main(String[] args) {
        StringList2 list = new StringList2();
        for (int i = 0; i < 100_000; i++) {
            list.add("data-" + i);
        }
//        list.add("Alpha");
//        list.add("Beta");
//        list.add("Gamma");

        // Step 1: StringList를 파일로 직렬화
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("stringlist.ser"))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: 파일을 StringList로 역직렬화
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("stringlist.ser"))) {
            StringList2 deserializedList = (StringList2) in.readObject();
            deserializedList.print();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
