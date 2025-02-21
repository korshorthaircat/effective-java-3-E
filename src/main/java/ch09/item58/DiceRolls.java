package ch09.item58;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

public class DiceRolls {
    enum Face { ONE, TWO, THREE, FOUR, FIVE, SIX } // 주사위의 6면

    public static void main(String[] args) {
        Collection<Face> faces = EnumSet.allOf(Face.class);

        for (Iterator<Face> i = faces.iterator(); i.hasNext(); )
            for (Iterator<Face> j = faces.iterator(); j.hasNext(); )
                System.out.println(i.next() + " " + j.next());
        // 출력 결과
//        ONE ONE
//        TWO TWO
//        THREE THREE
//        FOUR FOUR
//        FIVE FIVE
//        SIX SIX

        System.out.println("***************************");

        for (Face f1 : faces)
            for (Face f2 : faces)
                System.out.println(f1 + " " + f2);
    }
}
