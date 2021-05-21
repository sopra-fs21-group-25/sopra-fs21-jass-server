package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import java.util.UUID;

@SuppressWarnings("unused")
public class GameInitializationMSG {

    private UUID id;
    private UUID player0id;
    private UUID player1id;
    private UUID player2id;
    private UUID player3id;

    public GameInitializationMSG(UUID id, UUID player0id, UUID player1id, UUID player2id, UUID player3id) {
        this.id = id;
        this.player0id = player0id;
        this.player1id = player1id;
        this.player2id = player2id;
        this.player3id = player3id;
    }

    public GameInitializationMSG() {}

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
}
