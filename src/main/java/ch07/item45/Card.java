package ch07.item45;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Card {
    public enum Suit { SPADE, HEART, DIAMOND, CLUB }
    public enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }

    private final Suit suit;
    private final Rank rank;

    @Override public String toString() {
        return rank + " of " + suit + "S";
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;

    }
    private static final List<Card> NEW_DECK = newDeck();

    // (1) 중첩 for문 사용해 카드덱 초기화하는 방식
//    private static List<Card> newDeck() {
//        List<Card> result = new ArrayList<>();
//        for (Suit suit : Suit.values())
//            for (Rank rank : Rank.values())
//                result.add(new Card(suit, rank));
//        return result;
//    }

    // (2) 스트림으로 카드덱 초기화하는 방식
    private static List<Card> newDeck() {
        return Stream.of(Suit.values()) // Stream<Suit>
                .flatMap(suit -> // 각 문양에 대한 내부 스트림 생성
                        Stream.of(Rank.values())
                                .map(rank -> new Card(suit, rank)))
                // flatMap은 스트림의 원소 각각을 하나의 스트림으로 매핑하고, 그 스트림들을 다시 하나의 스트림으로 합친다. 평탄화(flattening)
                // 내부에 있는 중첩된 구조를 제거하고 하나의 단일 스트림으로 변환 가능
                .collect(toList());
    }

    public static void main(String[] args) {
        System.out.println(NEW_DECK);
    }
}