package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.IngameMode;
import ch.uzh.ifi.hase.soprafs21.game.Roundstart;

import java.util.UUID;

public class SchieberGamePutDTO {

    private Card playedCard;
    private UUID userId;
    private IngameMode ingameMode;
    private Roundstart lowOrHigh;

    public Card getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(Card playedCard) {
        this.playedCard = playedCard;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public IngameMode getIngameMode() {
        return ingameMode;
    }

    public void setIngameMode(IngameMode ingameMode) {
        this.ingameMode = ingameMode;
    }

    public Roundstart getLowOrHigh() {
        return lowOrHigh;
    }

    public void setLowOrHigh(Roundstart lowOrHigh) {
        this.lowOrHigh = lowOrHigh;
    }
}
