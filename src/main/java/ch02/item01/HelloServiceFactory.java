package ch02.item01;

public class HelloServiceFactory {
    public static void main(String[] args) {
        HelloService eng = HelloService.of("eng");
        System.out.println(eng.hello());
    }
}
