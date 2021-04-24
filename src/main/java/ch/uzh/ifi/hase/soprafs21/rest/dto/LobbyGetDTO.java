package ch.uzh.ifi.hase.soprafs21.rest.dto;


import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;

import java.util.UUID;

public class LobbyGetDTO {

    private UUID id;
    private GameMode mode;
    private String lobbyType;
    private IngameModeMultiplicatorObject[] ingameModes;
    private Card startingCard;
    private Integer pointsToWin;
    private Boolean weis;
    private Boolean crossWeis;
    private String weisAsk;
    private String creatorUsername;
    private String[] usersInLobby;

    public UUID getId() { return id; }
    public String getLobbyType() { return lobbyType; }
    public GameMode getMode() { return mode; }
    public IngameModeMultiplicatorObject[] getIngameModes() { return ingameModes; }
    public Card getStartingCard() { return startingCard; }
    public Integer getPointsToWin() { return pointsToWin; }
    public Boolean getWeis() { return weis; }
    public Boolean getCrossWeis() { return crossWeis; }
    public String getWeisAsk() { return weisAsk; }
    public String getCreatorUsername() { return creatorUsername; }
    public String[] getUsersInLobby() { return usersInLobby; }

    public void setId(UUID id) { this.id = id; }
    public void setMode(GameMode mode) { this.mode = mode; }
    public void setLobbyType(String lobbyType) { this.lobbyType = lobbyType; }
    public void setIngameModes(IngameModeMultiplicatorObject[] ingameModes) { this.ingameModes = ingameModes; }
    public void setStartingCard(Card card) { this.startingCard = card; }
    public void setPointsToWin(Integer pointsToWin) { this.pointsToWin = pointsToWin; }
    public void setWeis(Boolean weisAllowed) { this.weis = weisAllowed; }
    public void setCrossWeis(Boolean crossWeisAllowed) { this.crossWeis = crossWeisAllowed; }
    public void setWeisAsk(String weisAsk) { this.weisAsk = weisAsk; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }
    public void setUsersInLobby(String[] usersInLobby) { this.usersInLobby = usersInLobby; }
}
