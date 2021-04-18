package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.game.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String creatorUsername;

    @ElementCollection
    @CollectionTable(name = "lobby_having_users", joinColumns = @JoinColumn(name = "lobby_id"))
    @Column(name = "user_in_lobby")
    private Set<String> usersInLobby = new HashSet<>();

    @Column(nullable = false)
    private GameMode mode;

    @Column(nullable = false)
    private String lobbyType;

    @Column(nullable = false)
    private Rank startingCardRank;

    @Column(nullable = false)
    private Suit startingCardSuit;

    @Column(nullable = false)
    private Integer pointsToWin;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "lobby_ingame_modes", joinColumns = @JoinColumn(name = "lobby_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "ingameMode", column = @Column(name = "ingameModeName")),
            @AttributeOverride(name = "multiplicator", column = @Column(name = "multiplicator"))
    })
    private Set<IngameModeMultiplicatorObject> ingameModes = new HashSet<>();

    @Column(nullable = false)
    private Boolean weisAllowed;

    @Column(nullable = false)
    private Boolean crossWeisAllowed;

    @Column
    private String weisAsk;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Set<String> getUsersInLobby() {
        return usersInLobby;
    }

    public void setUsersInLobby(Set<String> usersInLobby) {
        this.usersInLobby = usersInLobby;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public String getLobbyType() {
        return lobbyType;
    }

    public void setLobbyType(String lobbyType) {
        this.lobbyType = lobbyType;
    }

    public Rank getStartingCardRank() {
        return startingCardRank;
    }

    public void setStartingCardRank(Rank startingCardRank) {
        this.startingCardRank = startingCardRank;
    }

    public Suit getStartingCardSuit() {
        return startingCardSuit;
    }

    public void setStartingCardSuit(Suit startingCardSuit) {
        this.startingCardSuit = startingCardSuit;
    }

    public Integer getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(Integer pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public Set<IngameModeMultiplicatorObject> getIngameModes() {
        return ingameModes;
    }

    public void setIngameModes(Set<IngameModeMultiplicatorObject> ingameModes) {
        this.ingameModes = ingameModes;
    }

    public Boolean getWeisAllowed() {
        return weisAllowed;
    }

    public void setWeisAllowed(Boolean weisAllowed) {
        this.weisAllowed = weisAllowed;
    }

    public Boolean getCrossWeisAllowed() {
        return crossWeisAllowed;
    }

    public void setCrossWeisAllowed(Boolean crossWeisAllowed) {
        this.crossWeisAllowed = crossWeisAllowed;
    }

    public String getWeisAsk() {
        return weisAsk;
    }

    public void setWeisAsk(String weisAsk) {
        this.weisAsk = weisAsk;
    }
}
