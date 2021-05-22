package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.assets.CompositeIdKey;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@IdClass(CompositeIdKey.class)
@Table(name = "FRIEND_REQUESTS")
public class FriendRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name="FROM_USER_ID", nullable=false)
    private User fromUser;

    @Id
    @ManyToOne
    @JoinColumn(name="TO_USER_ID", nullable=false)
    private User toUser;


    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) { this.fromUser = fromUser; }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) { this.toUser = toUser; }
}