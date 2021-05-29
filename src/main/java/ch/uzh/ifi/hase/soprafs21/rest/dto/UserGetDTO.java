package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public class UserGetDTO {

    private UUID id;
    private String username;
    private String email;
    private UserStatus status;
    private String userType;
    private String token;
    private UUID lobbyId;

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) { this.status = status; }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UUID getLobbyId() { return lobbyId; }

    public void setLobbyId(UUID lobbyId) { this.lobbyId = lobbyId; }
}
