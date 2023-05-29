package ch02.item01;

public interface HelloService {

    String hello();

    //자바8부터는 인터페이스가 정적 멤버를 허용한다.(public으로 간주)
    //(자바9에서는 메서드의 경우 private까지 허락하지만, 정적 필드와 정적 멤버클래스는 여전히 public이어야 한다.)
    static HelloService of(String lang) {
        if (lang.equals("ko")) {
            return new HelloKoreanService();
        } else {
            return new HelloEnglishService();
        }
    }
}
