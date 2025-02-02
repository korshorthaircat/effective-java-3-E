package ch07.item45;

public class CharWithStreamTest {
    public static void main(String[] args) {
        "Hello World!".chars() // 반한화는 스트림의 원소가 char가 아니라 int 값이다.
                .forEach(System.out::print);
        // 출력 결과: 72101108108111328711111410810033 (의도한 결과가 아님)

        System.out.println();
        "Hello World!".chars()
                .forEach(x -> System.out.print((char)x)); // 형변환을 명시적으로 해줘야 한다.
        // 출력 결과: Hello World!

        // 이름이 chars인데 int 스트림 반환시 헷갈릴 수 있다.
        // char 값들을 처리할 때는 스트림을 삼가는 편이 낫다!
    }
}
