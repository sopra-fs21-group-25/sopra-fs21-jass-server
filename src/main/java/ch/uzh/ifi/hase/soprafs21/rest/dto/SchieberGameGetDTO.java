package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.IngameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;

import java.util.UUID;

public class SchieberGameGetDTO {

    private UUID id;
    private UUID player0id;
    private UUID player1id;
    private UUID player2id;
    private UUID player3id;
    private String[] playerUsernames;
    private Integer[] playerCardsAmount;
    private Integer pointsToWin;
    private IngameModeMultiplicatorObject[] ingameModes;
    private Boolean weisAllowed;
    private Boolean crossWeisAllowed;
    private String weisAsk;
    private Integer pointsTeam0_2;
    private Integer pointsTeam1_3;
    private Integer trickToPlay;
    private Boolean[] playerStartsTrick;
    private Card[] cardsPlayed;
    private Boolean hasTrickStarted;
    private UUID idOfRoundStartingPlayer;
    private IngameMode currentIngameMode;

    /*
    This array contains the cards of a specific user;
    null if userId not specified in the GET destination
     */
    private Card[] cardsOfPlayer;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(Integer pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public IngameModeMultiplicatorObject[] getIngameModes() {
        return ingameModes;
    }

    public void setIngameModes(IngameModeMultiplicatorObject[] ingameModes) {
        this.ingameModes = ingameModes;
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

    public Integer getPointsTeam0_2() {
        return pointsTeam0_2;
    }

    public void setPointsTeam0_2(Integer pointsTeam0_2) {
        this.pointsTeam0_2 = pointsTeam0_2;
    }

    public Integer getPointsTeam1_3() {
        return pointsTeam1_3;
    }

    public void setPointsTeam1_3(Integer pointsTeam1_3) {
        this.pointsTeam1_3 = pointsTeam1_3;
    }

    public Integer getTrickToPlay() {
        return trickToPlay;
    }

    public void setTrickToPlay(Integer trickToPlay) {
        this.trickToPlay = trickToPlay;
    }

    public Boolean[] getPlayerStartsTrick() {
        return playerStartsTrick;
    }

    public void setPlayerStartsTrick(Boolean[] playerStartsTrick) {
        this.playerStartsTrick = playerStartsTrick;
    }

    public Card[] getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(Card[] cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public Boolean getHasTrickStarted() {
        return hasTrickStarted;
    }

    public void setHasTrickStarted(Boolean hasTrickStarted) {
        this.hasTrickStarted = hasTrickStarted;
    }

    public Card[] getCardsOfPlayer() {
        return cardsOfPlayer;
    }

    public void setCardsOfPlayer(Card[] cardsOfPlayer) {
        this.cardsOfPlayer = cardsOfPlayer;
    }

    public UUID getIdOfRoundStartingPlayer() {
        return idOfRoundStartingPlayer;
    }

    public void setIdOfRoundStartingPlayer(UUID idOfRoundStartingPlayer) {
        this.idOfRoundStartingPlayer = idOfRoundStartingPlayer;
    }

    public IngameMode getCurrentIngameMode() {
        return currentIngameMode;
    }

    public void setCurrentIngameMode(IngameMode currentIngameMode) {
        this.currentIngameMode = currentIngameMode;
    }

    public String[] getPlayerUsernames() {
        return playerUsernames;
    }

    public void setPlayerUsernames(String[] playerUsernames) {
        this.playerUsernames = playerUsernames;
    }

    public Integer[] getPlayerCardsAmount() {
        return playerCardsAmount;
    }

    public void setPlayerCardsAmount(Integer[] playerCardsAmount) {
        this.playerCardsAmount = playerCardsAmount;
    }
}
