package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public  class FriendRequestGetDTO {

    private UUID id;
    private UUID toId;
    private UUID fromId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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