package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.game.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "LOBBIES")
public class Lobby implements Serializable, Comparable<Lobby> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String creatorUsername;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="LOBBY_USERS",
            joinColumns = @JoinColumn(
                    name="LOBBY_ID",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name="USER_ID",
                    referencedColumnName = "id"
            )
    )
    private Set<User> usersInLobby = new HashSet<>();

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
    @CollectionTable(
            name = "lobby_ingame_modes",
            joinColumns = @JoinColumn(
                    name = "lobby_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "INGAME_MODES_FK_CONSTRAINT",
                            foreignKeyDefinition = "FOREIGN KEY (lobby_id) references public.lobbies (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE"
                    )
            )
    )
    @AttributeOverrides({
            @AttributeOverride(name = "ingameMode", column = @Column(name = "ingameModeName")),
            @AttributeOverride(name = "multiplicator", column = @Column(name = "multiplicator"))
    })
    @OrderColumn
    private List<IngameModeMultiplicatorObject> ingameModes = new ArrayList<>();

    @Column(nullable = false)
    private Boolean weisAllowed;

    @Column(nullable = false)
    private Boolean crossWeisAllowed;

    @Column
    private String weisAsk;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Set<User> getUsersInLobby() {
        return usersInLobby;
    }

    public void setUsersInLobby(Set<User> usersInLobby) {
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

    public List<IngameModeMultiplicatorObject> getIngameModes() {
        return ingameModes;
    }

    public void setIngameModes(List<IngameModeMultiplicatorObject> ingameModes) {
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Lobby o) {
        return this.getId().compareTo(o.getId());
    }
}
