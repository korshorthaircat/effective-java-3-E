package ch08.item50;

import java.util.Date;

class MaliciousDate extends Date {
    public MaliciousDate(long time) {
        super(time);
    }
    @Override
    public Object clone() {
        System.out.println("악의적인 clone() 메서드 실행됨!");
        return this; // 원래 객체를 그대로 반환하여 방어적 복사 무력화
    }
}

public class CloneAttack {
    public static void main(String[] args) {
        // 악의적인 MaliciousDate 인스턴스를 생성
        MaliciousDate start = new MaliciousDate(System.currentTimeMillis());
        MaliciousDate end = new MaliciousDate(start.getTime() + 1000000); // 1000초 후

        // Period3 인스턴스 생성 (clone()을 이용한 방어적 복사가 적용됨)
        Period3 period = new Period3(start, end);
        System.out.println("생성된 기간: " + period);

        // start를 변경하면, period 내부의 start도 변경되는지 확인
        System.out.println("start 변경 전: " + period);
        start.setTime(0); // 원본 변경
        System.out.println("start 변경 후: " + period); // 내부 데이터도 변경됨!


        System.out.println(new Date(0));
    }
}
