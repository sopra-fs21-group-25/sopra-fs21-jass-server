package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String creatorUsername;

    @ElementCollection
    private List<String> usersInLobby = new ArrayList<>();



    public String getCreatorUsername() {
        return this.creatorUsername;
    }

    public List<String> getUsersInLobby() {
        return this.usersInLobby;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
