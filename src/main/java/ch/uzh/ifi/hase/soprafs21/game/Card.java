package ch.uzh.ifi.hase.soprafs21.game;


import ch.uzh.ifi.hase.soprafs21.game.comparators.ObenabeComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.TrumpfComparator;
import ch.uzh.ifi.hase.soprafs21.game.comparators.UndenufeComparator;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.*;

@Embeddable
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

    @Override
    public int hashCode() {
        int leastSigDecDigit = getRank().ordinal();
        int mostSigDecDigit = getSuit().ordinal() * 10;

        // calc hashcode e.g. for the card BELL TEN as:
        // BELL ordinal is 2, times 10 is 20, then + TEN ordinal which is 4 => yields 20+4 == 24
        return mostSigDecDigit + leastSigDecDigit;
    }

    public static Card[] sortCardArray(Card ... cards) {
        ObenabeComparator comparator = new ObenabeComparator();

        List<Card> acorns = new ArrayList<>();
        List<Card> roses = new ArrayList<>();
        List<Card> bells = new ArrayList<>();
        List<Card> shields = new ArrayList<>();

        for(Card card : cards) {
            if(card.getSuit() == Suit.ACORN) {
                acorns.add(card);
            }
            if(card.getSuit() == Suit.ROSE) {
                roses.add(card);
            }
            if(card.getSuit() == Suit.BELL) {
                bells.add(card);
            }
            if(card.getSuit() == Suit.SHIELD) {
                shields.add(card);
            }
        }

        acorns.sort(comparator);
        roses.sort(comparator);
        bells.sort(comparator);
        shields.sort(comparator);

        List<Card> cardsSuitSorted = new ArrayList<>();

        cardsSuitSorted.addAll(acorns);
        cardsSuitSorted.addAll(roses);
        cardsSuitSorted.addAll(bells);
        cardsSuitSorted.addAll(shields);

        return cardsSuitSorted.toArray(new Card[0]);
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
    public static Card determineHighestCard(IngameMode mode, Roundstart start, Integer trick, Integer[] pointsCollector, Card trickStartingCard, Card ... cards) {
        Card result = trickStartingCard;
        Comparator<Card> comparator;

        switch(mode) {
            case ACORN, ROSE, BELL, SHIELD -> {
                comparator = new TrumpfComparator();
                for(Card card : cards) {
                    if(card.getIsTrumpf() && card.getRank() == Rank.UNDER) {
                        pointsCollector[0] += 20;
                    }
                    else if(card.getIsTrumpf() && card.getRank() == Rank.NINE) {
                        pointsCollector[0] += 14;
                    }
                    else {
                        pointsCollector[0] += card.calcObenabePoints(mode);
                    }
                }
            }
            case OBENABE -> {
                comparator = new ObenabeComparator();
                for(Card card : cards) {
                    pointsCollector[0] += card.calcObenabePoints(mode);
                }
            }
            case UNDENUFE -> {
                comparator = new UndenufeComparator();
                for(Card card : cards) {
                    pointsCollector[0] += card.calcUndenufePoints();
                }
            }
            case SLALOM -> {
                /*
                Depending on (1) did the round start Obe or Unde and
                (2) which is the current trick this round, we have to
                determine which comparator to use
                 */
                if(start == Roundstart.OBE) {
                    for(Card card : cards) {
                        pointsCollector[0] += card.calcObenabePoints(mode);
                    }

                    if(trick % 2 == 0) {
                        comparator = new ObenabeComparator();
                    } else {
                        comparator = new UndenufeComparator();
                    }
                } else {
                    for(Card card : cards) {
                        pointsCollector[0] += card.calcUndenufePoints();
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

                for(Card card : cards) {
                    pointsCollector[0] += card.calcObenabePoints(mode);
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

                for(Card card : cards) {
                    pointsCollector[0] += card.calcUndenufePoints();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        }

        /*
        finally use the chosen comparator to determine the highest card
         */
        if(trickStartingCard.getIsTrumpf()) {
            for(Card card : cards) {
                if(comparator.compare(result, card) < 0) {
                    result = card;
                }
            }
        } else {
            for(Card card : cards) {
                if(card.getIsTrumpf()) {
                    if(comparator.compare(result, card) < 0) {
                        result = card;
                    }
                }
                else {
                    if(card.getSuit() == trickStartingCard.getSuit() && comparator.compare(result, card) < 0) {
                        result = card;
                    }
                }
            }
        }


        return result;
    }

    private int calcObenabePoints(IngameMode mode) {
        switch (rank) {
            case SIX, SEVEN, NINE -> {
                return 0;
            }
            case EIGHT -> {
                // ordVal in [0,..,3] means Trump mode, i.e. no points for the EIGHT
                int ordVal = mode.ordinal();
                return ordVal==0||ordVal==1||ordVal==2||ordVal==3 ? 0 : 8;
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
