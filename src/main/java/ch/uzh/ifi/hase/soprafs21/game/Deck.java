package ch.uzh.ifi.hase.soprafs21.game;

import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Deck {
    Card[] cards = new Card[36];


    private void initialize() {
        int i=0;
        for(Suit s : Suit.values()) {
            for(Rank r : Rank.values()) {
                cards[i] = new Card(s,r);
                i++;
            }
        }
    }

    private void shuffle() {
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
    private Card[][] dealCards(GameMode mode) {
        /*
        NOTE: Works only for Schieber so far (!)
         */
        if(mode == GameMode.SCHIEBER) {
            Card[][] playersCards = new Card[4][9];
            for(int i=0; i<36; i++) {
                playersCards[i%4][i%9] = this.cards[i];
            }

            playersCards[0] = Card.sortCardArray(playersCards[0]);
            playersCards[1] = Card.sortCardArray(playersCards[1]);
            playersCards[2] = Card.sortCardArray(playersCards[2]);
            playersCards[3] = Card.sortCardArray(playersCards[3]);

            return playersCards;
        }
        return null;
    }

    public static void initializePlayerCards(SchieberGameSession schieberGameSession) {
        Deck deck = new Deck();
        deck.initialize();
        deck.shuffle();

        Card[][] cardsToDeal = deck.dealCards(GameMode.SCHIEBER);

        schieberGameSession.setCardsHeldByPlayer0(Arrays.asList(cardsToDeal[0]));
        schieberGameSession.setCardsHeldByPlayer1(Arrays.asList(cardsToDeal[1]));
        schieberGameSession.setCardsHeldByPlayer2(Arrays.asList(cardsToDeal[2]));
        schieberGameSession.setCardsHeldByPlayer3(Arrays.asList(cardsToDeal[3]));
    }


}
