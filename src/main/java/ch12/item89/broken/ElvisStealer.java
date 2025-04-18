package ch12.item89.broken;

import java.io.Serializable;

/**
 * Elvis 인스턴스를 훔쳐올 도둑 클래스
 */
public class ElvisStealer implements Serializable {
    static Elvis impersonator;
    private Elvis payload;



    private Object readResolve() {
        impersonator = payload; // 역직렬화된 Elvis 객체를 훔침. resolve되기 전의 Elvis 인스턴스의 참조를 저장한다.
        return new String[] { "A Fool Such As I" };
    }

    private static final long serialVersionUID = 0;
}
