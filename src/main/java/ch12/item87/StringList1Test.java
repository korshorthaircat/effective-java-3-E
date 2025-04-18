package ch12.item87;

import java.io.*;

public class StringList1Test {
    public static void main(String[] args) {
        // 1. 객체 생성 및 데이터 추가
        StringList1 list = new StringList1();
        for (int i = 0; i < 100_000; i++) {
            list.add("data-" + i);
        }
//        list.add("Alpha");
//        list.add("Beta");
//        list.add("Gamma");

        // 2. 직렬화
        long startWrite = System.currentTimeMillis();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("stringlist1.ser"))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endWrite = System.currentTimeMillis();

        System.out.println("직렬화 완료: " + (endWrite - startWrite) + "ms");

        // 3. 역직렬화
        long startRead = System.currentTimeMillis();
        StringList1 readList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("stringlist1.ser"))) {
            readList = (StringList1) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        long endRead = System.currentTimeMillis();

        System.out.println("역직렬화 완료: " + (endRead - startRead) + "ms");

        readList.print();
    }
}
