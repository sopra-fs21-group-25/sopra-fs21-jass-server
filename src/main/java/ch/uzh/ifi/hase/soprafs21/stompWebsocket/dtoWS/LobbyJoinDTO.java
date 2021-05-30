package ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS;

import java.util.UUID;

public class LobbyJoinDTO {

    private UUID lobbyId;
    private String lobbyCreator;


    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof LobbyJoinDTO)) {
            return false;
        }

        LobbyJoinDTO dto = (LobbyJoinDTO) o;
        return lobbyId.equals(dto.getLobbyId()) && lobbyCreator.equals(dto.getLobbyCreator());
    }

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
