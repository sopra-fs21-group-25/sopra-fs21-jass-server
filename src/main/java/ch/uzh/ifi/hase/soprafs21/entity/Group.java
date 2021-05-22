package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.game.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.Date; 


@Entity
@Table(name = "Groups")
public class Group implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)   
    private UUID id;
    
    @OneToMany(mappedBy="group")
    private List<Message> messages;

    @ManyToMany(mappedBy="groups")
    private List<User> users; 

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

}
