package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserGetDTO {

    private UUID id;
    private String username;
    private UserStatus status;
    private String userType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
