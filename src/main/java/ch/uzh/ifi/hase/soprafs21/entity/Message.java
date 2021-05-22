package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.game.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.Date; 

@Entity
@Table(name = "Messages")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="sender_Id", nullable=false)
    private User sender;

    @Column(nullable = false, unique = false)
    private String text;

    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name="group_Id", nullable=false)
    private Group group;

    public Message(){ 
        timestamp = new Date(); 
    }
    
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
