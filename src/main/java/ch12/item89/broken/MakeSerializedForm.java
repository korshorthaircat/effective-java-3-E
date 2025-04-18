package ch12.item89.broken;

import ch12.Util;

import java.io.FileOutputStream;
import java.lang.reflect.Field;

public class MakeSerializedForm {
    public static void main(String[] args) throws Exception {
        // 도둑에 Elvis를 심어준다
        ElvisStealer stealer = new ElvisStealer();
        Field f = stealer.getClass().getDeclaredField("payload");
        f.setAccessible(true);
        f.set(stealer, Elvis.INSTANCE);

        byte[] bytes = Util.serialize(stealer);

        // 바이트 배열 출력
        System.out.println("private static final byte[] serializedForm = {");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(String.format("(byte) 0x%02X", bytes[i]));
            if (i < bytes.length - 1) System.out.print(", ");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("\n};");

        // 선택: elvis.ser 저장
//        try (FileOutputStream out = new FileOutputStream("elvis.ser")) {
//            out.write(bytes);
//        }
    }
}
