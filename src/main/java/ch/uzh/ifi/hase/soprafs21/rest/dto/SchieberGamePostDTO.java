package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;

import java.util.UUID;

public class SchieberGamePostDTO {

    private IngameModeMultiplicatorObject[] ingameModes;
    private Integer pointsToWin;
    private Card startingCard;
    private Boolean weisAllowed;
    private Boolean crossWeisAllowed;
    private String weisAsk;
    private UUID player0id;
    private UUID player1id;
    private UUID player2id;
    private UUID player3id;
    private UUID prevLobbyId;

    public IngameModeMultiplicatorObject[] getIngameModes() {
        return ingameModes;
    }

    public void setIngameModes(IngameModeMultiplicatorObject[] ingameModes) {
        this.ingameModes = ingameModes;
    }

    public Integer getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(Integer pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public Card getStartingCard() {
        return startingCard;
    }

    public void setStartingCard(Card startingCard) {
        this.startingCard = startingCard;
    }

    public Boolean getWeisAllowed() {
        return weisAllowed;
    }

    public void setWeisAllowed(Boolean weisAllowed) {
        this.weisAllowed = weisAllowed;
    }

    public Boolean getCrossWeisAllowed() {
        return crossWeisAllowed;
    }

    public void setCrossWeisAllowed(Boolean crossWeisAllowed) {
        this.crossWeisAllowed = crossWeisAllowed;
    }

    public String getWeisAsk() {
        return weisAsk;
    }

    public void setWeisAsk(String weisAsk) {
        this.weisAsk = weisAsk;
    }

    public UUID getPlayer0id() {
        return player0id;
    }

    public void setPlayer0id(UUID player0id) {
        this.player0id = player0id;
    }

    public UUID getPlayer1id() {
        return player1id;
    }

    public void setPlayer1id(UUID player1id) {
        this.player1id = player1id;
    }

    public UUID getPlayer2id() {
        return player2id;
    }

    public void setPlayer2id(UUID player2id) {
        this.player2id = player2id;
    }

    public UUID getPlayer3id() {
        return player3id;
    }

    public void setPlayer3id(UUID player3id) {
        this.player3id = player3id;
    }

    public UUID getPrevLobbyId() {
        return prevLobbyId;
    }

    public void setPrevLobbyId(UUID prevLobbyId) {
        this.prevLobbyId = prevLobbyId;
    }
}
