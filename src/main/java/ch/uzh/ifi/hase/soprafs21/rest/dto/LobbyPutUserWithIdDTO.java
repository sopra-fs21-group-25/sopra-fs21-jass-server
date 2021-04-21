package ch.uzh.ifi.hase.soprafs21.rest.dto;


import java.util.UUID;

public class LobbyPutUserWithIdDTO {

    private UUID userId;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
