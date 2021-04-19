package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "FriendRequests")
public class FriendRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected UUID id;

    @ManyToOne
    @JoinColumn(name="fromUser_id", nullable=false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name="toUser_id", nullable=false)
    private User toUser;

    private UUID fromId; 
    private UUID toId; 

    public UUID getFromId() {
        return fromId;
    }

    public UUID getToId() {
        return toId;
    }
    public FriendRequest(User fromUser, User toUser){
        this.setFromUser(fromUser); 
        this.setToUser(toUser);
    }

    public FriendRequest(){
    }

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
        this.fromId = fromUser.getId();
    }
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
        this.toId = toUser.getId();
    }
}