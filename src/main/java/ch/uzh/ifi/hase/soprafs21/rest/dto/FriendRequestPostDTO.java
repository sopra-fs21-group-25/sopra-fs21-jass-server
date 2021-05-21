package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public  class FriendRequestPostDTO {

    private UUID fromId;
    private UUID toId;

    public FriendRequestPostDTO(UUID fromId, UUID toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

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
}