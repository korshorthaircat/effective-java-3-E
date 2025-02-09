package ch08.item50;

import java.util.Date;

/**
 * Period - 안전하지 못한 클래스
 * 생성자에서 방어적 복사를 clone()으로 수행하는 경우
 */
public final class Period3 {
    private final Date start;
    private final Date end;

    public Period3(Date start, Date end) {
        // 매개변수의 방어적 복사본을 만들 때 clone()을 사용했다.
        this.start = (Date) start.clone();
        this.end   = (Date) end.clone();

        if (this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException(
                    this.start + " after " + this.end);
    }

    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }

    public String toString() {
        return start + " - " + end;
    }
}