package ch.uzh.ifi.hase.soprafs21.game.comparators;

import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.Rank;

import java.util.Comparator;

public class TrumpfComparator implements Comparator<Card> {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * The implementor must ensure that {@code sgn(compare(x, y)) ==
     * -sgn(compare(y, x))} for all {@code x} and {@code y}.  (This
     * implies that {@code compare(x, y)} must throw an exception if and only
     * if {@code compare(y, x)} throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
     * {@code compare(x, z)>0}.<p>
     * <p>
     * Finally, the implementor must ensure that {@code compare(x, y)==0}
     * implies that {@code sgn(compare(x, z))==sgn(compare(y, z))} for all
     * {@code z}.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."<p>
     * <p>
     * In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(Card o1, Card o2) {
        if(o1.isTrumpf() && o2.isTrumpf()) {
            /*
            Both are Trumpf, hence we have to determine first if
            either is UNDER (i.e. the highest card) or else none is
            UNDER, then if either is NINE (i.e. the second highest
            card) or else regular card precedence
             */
            if(o1.getRank() == Rank.UNDER) return 1;
            if(o2.getRank() == Rank.UNDER) return -1;
            if(o1.getRank() == Rank.NINE) return 1;
            if(o2.getRank() == Rank.NINE) return -1;
            return o1.getRank().compareTo(o2.getRank());
        } else if(o1.isTrumpf()) {
            /*
            Only o1 is Trumpf, thus the higher card
             */
            return 1;
        } else if(o2.isTrumpf()) {
            /*
            Only o2 is Trumpf, thus the higher card
             */
            return -1;
        } else {
            /*
            None is Trumpf, thus regular precedence applies
             */
            return o1.getRank().compareTo(o2.getRank());
        }
    }
}
