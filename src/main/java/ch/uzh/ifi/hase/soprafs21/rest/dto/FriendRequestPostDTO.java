package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public  class FriendRequestPostDTO {

    private UUID id;
    private User fromUser;
    private User toUser;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}