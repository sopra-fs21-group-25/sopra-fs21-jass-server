package ch.uzh.ifi.hase.soprafs21.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Deck {
    Card[] cards = new Card[36];

    /**
     *
     * @param modes used to set trumpf-values of cards in case
     *              the deck needs to be initialized for a trumpf
     *              mode. Otherwise initialize is called without
     *              arguments or the argument is ignored
     */
    public void initialize(IngameMode ... modes) {
        int i=0;
        for(Suit s : Suit.values()) {
            for(Rank r : Rank.values()) {
                cards[i] = new Card(s,r);
                /*
                if the mode given as argument is a Trumpf mode of a
                specific suit, set those cards's isTrumpf indicator
                field to true
                 */
                if(modes.length > 0 && modes[0].name().equals(s.name())) {
                    cards[i].setTrumpf();
                }
                i++;
            }
        }
    }

    public void shuffle() {
        List<Card> cardList = Arrays.asList(this.cards);
        Collections.shuffle(cardList);
        cardList.toArray(this.cards);
    }

    /**
     * TODO: configure for other gamemodes besides Schieber
     * @param mode determines via the gamemode (e.g. Schieber)
     *             how many players must receive how many cards
     * @return an array containing arrays reflecting the handcards
     * to be individually received per player
     */
    public Card[][] dealCards(GameMode mode) {
        /*
        NOTE: Works only for Schieber so far (!)
         */
        Card[][] playersCards = new Card[4][9];
        for(int i=0; i<36; i++) {
            playersCards[i%4][i%9] = this.cards[i];
        }

        return playersCards;
    }
}
