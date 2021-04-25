package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.UUID;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public class UserPutDTO {

    private UUID id;
    private String username;
    private UserStatus status;
    private String password;
    // private User[] friends;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) { 
        this.status = status; 
    }

    // public User[] getFriends() { return friends; }

    // public void setFriends(User[] friends) { this.friends = friends; }

}
