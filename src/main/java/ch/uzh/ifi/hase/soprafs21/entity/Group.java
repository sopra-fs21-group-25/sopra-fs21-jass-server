package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.game.*;
import javassist.SerialVersionUID;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.Date; 


@Entity
@Table(name = "Groups_")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)   
    private UUID id;
    
    @OneToMany(mappedBy="group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messages;

    @ManyToMany(mappedBy="groups")
    private List<User> users;

    @OneToOne(mappedBy = "group", fetch = FetchType.LAZY)
    private Lobby lobby;

    @OneToOne(mappedBy = "group", fetch = FetchType.LAZY)
    private SchieberGameSession game;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;


    public Group(GroupType groupType, User... users) {
        this.groupType = groupType;
        for(User u : users) { u.getGroups().add(this); }
        this.messages = new ArrayList<>();
    }

    public Group() {}

    public UUID evaluateEnvironmentId(UUID senderId) {
        if(this.groupType == GroupType.BIDIRECTIONAL) {
            for(User u : this.users) {
                if(!u.getId().equals(senderId)) {
                    return u.getId();
                }
            }
            return null;
        } else {
            if(this.game != null) {
                return this.game.getId();
            } else {
                return this.lobby.getId();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof Group)) {
            return false;
        }

        return this.id.equals(((Group) o).getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users; }

    public GroupType getGroupType() { return groupType; }

    public void setGroupType(GroupType groupType) { this.groupType = groupType; }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public SchieberGameSession getGame() {
        return game;
    }

    public void setGame(SchieberGameSession game) {
        this.game = game;
    }
}
