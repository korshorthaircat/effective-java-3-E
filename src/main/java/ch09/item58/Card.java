package ch09.item58;

import java.util.*;

public class Card {
    private final Suit suit;
    private final Rank rank;

    // 버그를 찾아보자.
    enum Suit { CLUB, DIAMOND, HEART, SPADE }
    enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }

    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());

    Card(Suit suit, Rank rank ) {
        this.suit = suit;
        this.rank = rank;
    }

    public static void main(String[] args) {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        List<Card> deck3 = new ArrayList<>();

//        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
//            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
//                deck1.add(new Card(i.next(), j.next()));
        // 실행 결과: Exception in thread "main" java.util.NoSuchElementException
        // 원인: i.next()에서, 이 next()는 숫자 하나당 한 번씩만 불려야 했는데 안쪽 반복문에서 호출되는 바람에 카드 하나당 한번씩 호출되고 있다.
        // Card("CLUB", "ACE"), Card("Diamond", "Deuce"), Card("Heart", "Three"), Card("Spade", "Four"), NoSuchElementException...

        // 위의 문제 해결하려면, 반복문 바깥에 원소를 저장하는 변수를 추가하여 사용하면 된다. 보기 좋지는 않다.
        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
            Suit suit = i.next();
            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
                deck2.add(new Card(suit, j.next()));
        }

        // 컬렉션이나 배열의 중첩 반복시에 향상된 for문을 사용하는 것이 더 낫다.
        for (Suit suit : suits)
            for (Rank rank : ranks)
                deck3.add(new Card(suit, rank));
    }
}
