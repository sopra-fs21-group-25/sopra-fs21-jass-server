package ch.uzh.ifi.hase.soprafs21.rest.dto;


import java.util.UUID;

public class LobbyPutUserWithIdDTO {

    private UUID userId;
    private Boolean remove;
    private Boolean add;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }
}
