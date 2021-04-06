package ch.uzh.ifi.hase.soprafs21.game;


import ch.uzh.ifi.hase.soprafs21.game.comparators.ObenabeComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.TrumpfComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.UndenufeComparator;

import java.util.Comparator;

public class Card {
    public Suit suit;
    public Rank rank;
    private boolean isTrumpf = false;

    public Card(Suit s, Rank r) {
        this.suit = s;
        this.rank = r;
    }

    public void setTrumpf() {
        this.isTrumpf = true;
    }

    public boolean isTrumpf() {
        return this.isTrumpf;
    }

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
     *              trick index 1, not 0 (!)
     * @param cards the cards to compare with each other
     * @return the highest card in the current trick according to the right precedence
     */
    public static Card determineHighestCard(IngameMode mode, Roundstart start, Integer trick, Card ... cards) {
        Card result = cards[0];
        Comparator<Card> comparator;

        switch(mode) {
            case ACORN, ROSE, BELL, SHIELD -> {
                comparator = new TrumpfComparator();
            }
            case OBENABE -> {
                comparator = new ObenabeComparator();
            }
            case UNDENUFE -> {
                comparator = new UndenufeComparator();
            }
            case SLALOM -> {
                /*
                Depending on (1) did the round start Obe or Unde and
                (2) which is the current trick this round, we have to
                determine which comparator to use
                 */
                if(start == Roundstart.OBE) {
                    if(trick % 2 != 0) {
                        comparator = new ObenabeComparator();
                    } else {
                        comparator = new UndenufeComparator();
                    }
                } else {
                    if(trick % 2 == 0) {
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
                if(trick > 5) {
                    comparator = new UndenufeComparator();
                } else {
                    comparator = new ObenabeComparator();
                }
            }
            case MERRY -> {
                /*
                Analogous to GUSTI with reversed comparators
                 */
                if(trick <= 5) {
                    comparator = new UndenufeComparator();
                } else {
                    comparator = new ObenabeComparator();
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
}
