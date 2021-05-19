package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;

public class FriendRequestWithUsernameGetDTO {
    private UUID toId;
    private UUID fromId;
    private String fromUsername;


    public UUID getFromId() {
        return fromId;
    }

    public void setFromId(UUID fromId) {
        this.fromId = fromId;
    }

    public UUID getToId() {
        return toId;
    }

    public void setToId(UUID toId) {
        this.toId = toId;
    }

    public String getFromUsername() { return fromUsername; }

    public void setFromUsername(String fromUsername) { this.fromUsername = fromUsername; }
}
