package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

public class LobbyUsersGetDTO {

    private User[] usersInLobby;

    public User[] getUsersInLobby() {
        return usersInLobby;
    }

    public void setUsersInLobby(User[] usersInLobby) {
        this.usersInLobby = usersInLobby;
    }
}
