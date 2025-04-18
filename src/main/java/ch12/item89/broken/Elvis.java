package ch12.item89.broken;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 잘못된 싱글턴 (직렬화로 인해 취약한 구조)
 * transient가 아닌 참조 필드를 가지고 있음
 */
public class Elvis implements Serializable {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    private String[] favoriteSongs = {"Hound Dog", "Heartbreak Hotel"}; // transient기ㅏ 아닌 참조 필드를 가지고 있다.

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

    /**
     * readResolve()는 Java 직렬화 시스템에서 역직렬화 후 객체를 대체할 수 있게 해주는 특수 메서드
     * 이 메서드는 객체가 역직렬화된 직후에 호출돼서, 역직렬화된 객체 대신 반환할 다른 객체를 지정할 수 있게 해준다.
     * @return
     */
    private Object readResolve() {
        return INSTANCE; // 진짜 Elvis를 반환하고 가짜 Elvis는 가비지 컬렉터에 맡긴다.
    }
    // 이 메서드는 역직렬화한 객체는 무시하고, 클래스 초기화 때 만들어진 Elvis 인스턴스를 반환한다.
    // 따라서 Elvis 인스턴스의 직렬화 형태는 아무런 실 데이터를 가질 이유가 없으니, 모든 인스턴스 필드를 transient로 선언해야 한다.
    // (사실 readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다.
    // 그렇지 않으면 readResolve 메서드가 수행되기 전 역직렬화된 객체의 참조를 공격할 여지가 남는다.)

    private static final long serialVersionUID = 0;
}
