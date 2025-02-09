package ch08.item50;

import java.util.Date;

/**
 * Period - 불변식을 지키도록 수정 클래스
 */
public final class Period2 {
    private final Date start;
    private final Date end;

    /**
     * @param  start 시작 시각
     * @param  end 종료 시각; 시작 시각보다 뒤여야 한다.
     * @throws IllegalArgumentException 시작 시각이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    // 수정한 생성자
    public Period2(Date start, Date end) {
        // 매개변수의 방어적 복사본을 만들어 사용한다.
        this.start = new Date(start.getTime());
        this.end   = new Date(end.getTime()); // 생성자에서 방어적 복사시 clone() 메서드를 사용하지 않음. Date의 하위 타입이 악의적으로 clone()을 재정의하고 있을 수 있기 때문

        // 방어적 복사 수행 이후에, 방아적 복사본을 가지고 매개변수 유효성 검사를 수행해야 한다.
        // 검사시점/사용시점(TOC/TOU) 공격으로부터 안전하기 위해
        if (this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException(
                    this.start + " after " + this.end);
    }

    // 수정한 접근자 - 필드의 방어적 복사본을 반환한다.
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