package ch12.item90;

// Period class with serialization proxy - Pages 363-364

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

// 방어적 복사를 사용한 불변 클래스 Period
public final class Period implements Serializable {
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end   = new Date(end.getTime());
        if (this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException(
                    start + " after " + end);
    }

    public Date start () { return new Date(start.getTime()); }

    public Date end () { return new Date(end.getTime()); }

    public String toString() { return start + " - " + end; }


    // Period 클래스용 직렬화 프록시
    private static class SerializationProxy implements Serializable {
        private final Date start;
        private final Date end;

        SerializationProxy(Period p) {
            this.start = p.start;
            this.end = p.end;
        }

        private static final long serialVersionUID = 234098243823485285L; // Any number will do (Item 87)
    }

    // writeReplace method for the serialization proxy pattern

    /**
     * writeReplace
     *
     * 자바의 직렬화 시스템이 바깥 클래스의 인스턴스 대신 SerializationProxy의 인스턴스를 반환하게 함
     * 즉, 직렬화가 이뤄지기 전 바깥 클래스의 인스턴스를 직렬화 프록시로 변환해준다.
     * writeReplace 덕분에 직렬화 시스템은 결코 바깥 클래스의 직렬화된 인스턴스를 생성해낼 수 없다.
     *
     * 이 메서드는 범용적이므로 직렬화 프록시를 사용하는 모든 클래스에 동일하게 작성하면 됨
     */
    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    /**
     * readObject
     *
     * 직렬화 프록시 패턴용 readObject 메서드
     */
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy 필요함");
    }
}