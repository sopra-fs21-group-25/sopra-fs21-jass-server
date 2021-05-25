package ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS;

import java.util.UUID;

public class LobbyJoinDTO {

    private UUID lobbyId;
    private String lobbyCreator;


    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyCreator() {
        return lobbyCreator;
    }

    public void setLobbyCreator(String lobbyCreator) {
        this.lobbyCreator = lobbyCreator;
    }
}
