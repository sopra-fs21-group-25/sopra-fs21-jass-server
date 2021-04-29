package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.game.Card;

import java.util.UUID;

public class CardsGetDTO {

    private Card[]  cards;


    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
}
