package ch.uzh.ifi.hase.soprafs21.game;


import ch.uzh.ifi.hase.soprafs21.game.comparators.ObenabeComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.TrumpfComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.UndenufeComparator;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Comparator;

@Embeddable
@SuppressWarnings("unchecked")
public class Card {

    @Column
    @Enumerated(EnumType.STRING)
    private Suit suit;

    @Column
    @Enumerated(EnumType.STRING)
    private Rank rank;

    @Column
    private Boolean isTrumpf;

    public Card(Suit s, Rank r) {
        this.suit = s;
        this.rank = r;
        this.isTrumpf = false;
    }

    public Card() {}

    public void setIsTrumpf(Boolean isTrumpf) {
        this.isTrumpf = isTrumpf;
    }
    public boolean getIsTrumpf() { return isTrumpf; }


    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }

    public void setSuit(Suit suit) { this.suit = suit; }
    public void setRank(Rank rank) { this.rank = rank; }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Card)) return false;

        Card other = (Card) o;
        return this.suit == other.suit && this.rank == other.rank;
    }

    /**
     *
     * @param mode the ingame mode played in the current round where
     *             we want to determine the card precedence
     * @param start only relevant for slalom
     * @param trick current round's trick number, relevant for slalom,
     *              gusti and merry. NOTE: a round should start with
     *              trick index 0
     * @param cards the cards to compare with each other
     * @return the highest card in the current trick according to the right precedence
     */
    public static Card determineHighestCard(IngameMode mode, Roundstart start, Integer trick, Integer[] pointsCollector, Card ... cards) {
        Card result = cards[0];
        Comparator<Card> comparator;

        switch(mode) {
            case ACORN, ROSE, BELL, SHIELD -> {
                comparator = new TrumpfComparator();
                for(int i=0; i<cards.length; i++) {
                    if(cards[i].getIsTrumpf() && cards[i].getRank() == Rank.UNDER) {
                        pointsCollector[0] += 20;
                    } else if(cards[i].getIsTrumpf() && cards[i].getRank() == Rank.NINE) {
                        pointsCollector[0] += 14;
                    } else {
                        pointsCollector[0] += cards[i].calcObenabePoints();
                    }
                }
            }
            case OBENABE -> {
                comparator = new ObenabeComparator();
                for(int i=0; i<cards.length; i++) {
                    pointsCollector[0] += cards[i].calcObenabePoints();
                }
            }
            case UNDENUFE -> {
                comparator = new UndenufeComparator();
                for(int i=0; i<cards.length; i++) {
                    pointsCollector[0] += cards[i].calcUndenufePoints();
                }
            }
            case SLALOM -> {
                /*
                Depending on (1) did the round start Obe or Unde and
                (2) which is the current trick this round, we have to
                determine which comparator to use
                 */
                if(start == Roundstart.OBE) {
                    for(int i=0; i<cards.length; i++) {
                        pointsCollector[0] += cards[i].calcObenabePoints();
                    }

                    if(trick % 2 == 0) {
                        comparator = new ObenabeComparator();
                    } else {
                        comparator = new UndenufeComparator();
                    }
                } else {
                    for(int i=0; i<cards.length; i++) {
                        pointsCollector[0] += cards[i].calcUndenufePoints();
                    }

                    if(trick % 2 != 0) {
                        comparator = new ObenabeComparator();
                    } else {
                        comparator = new UndenufeComparator();
                    }
                }
            }
            case GUSTI -> {
                /*
                If the current trick belongs to the first 5 tricks,
                the comparator must be an ObenabeComparator, otherwise
                it must be an UndenufeComparator
                 */
                if(trick > 4) {
                    comparator = new UndenufeComparator();
                } else {
                    comparator = new ObenabeComparator();
                }

                for(int i=0; i<cards.length; i++) {
                    pointsCollector[0] += cards[i].calcObenabePoints();
                }
            }
            case MARY -> {
                /*
                Analogous to GUSTI with reversed comparators
                 */
                if(trick <= 4) {
                    comparator = new UndenufeComparator();
                } else {
                    comparator = new ObenabeComparator();
                }

                for(int i=0; i<cards.length; i++) {
                    pointsCollector[0] += cards[i].calcUndenufePoints();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        }

        /*
        finally use the chosen comparator to determine the highest card
         */
        for (int i=1; i<cards.length; i++) {
            if(comparator.compare(result, cards[i]) < 0) {
                result = cards[i];
            }
        }

        return result;
    }

    private int calcObenabePoints() {
        switch (rank) {
            case SIX, SEVEN, NINE -> {
                return 0;
            }
            case EIGHT -> {
                return 8;
            }
            case TEN -> {
                return 10;
            }
            case UNDER -> {
                return 2;
            }
            case OBER -> {
                return 3;
            }
            case KING -> {
                return 4;
            }
            case ACE -> {
                return 11;
            }
        }

        return 0;
    }

    private int calcUndenufePoints() {
        switch (rank) {
            case ACE, SEVEN, NINE -> {
                return 0;
            }
            case EIGHT -> {
                return 8;
            }
            case TEN -> {
                return 10;
            }
            case UNDER -> {
                return 2;
            }
            case OBER -> {
                return 3;
            }
            case KING -> {
                return 4;
            }
            case SIX -> {
                return 11;
            }
        }

        return 0;
    }
}
