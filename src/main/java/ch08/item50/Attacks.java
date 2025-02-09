package ch08.item50;

import java.util.Date;

/**
 * Period 내부를 변경하도록 공격
 */
public class Attacks {
    public static void main(String[] args) {
        //----------- 첫 번째 공격
        Date start = new Date();
        Date end = new Date();
        Period1 p = new Period1(start, end);
        end.setYear(78);  // p의 내부를 수정할 수 있다!
        System.out.println("p = " + p);


        // 방어적 복사 수행 후...안전해졌다.
        Date start2 = new Date();
        Date end2 = new Date();
        Period2 p2 = new Period2(start2, end2);
        end2.setYear(78);
        System.out.println("p2 = " + p2);

        //----------- 두 번째 공격
        Date start3 = new Date();
        Date end3 = new Date();
        Period1 p3 = new Period1(start3, end3);
        p3.end().setYear(78);  // p의 내부를 변경했다!
        System.out.println("p3 = " + p3);
    }
}
