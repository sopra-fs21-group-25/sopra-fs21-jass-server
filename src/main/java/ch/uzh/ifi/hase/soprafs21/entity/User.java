package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
@Table(name = "users")
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(nullable = true, unique = true)
    protected String username;

    @Column(nullable = true)
    protected UserStatus status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user0")
    private SchieberGameSession gameSitting0;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user1")
    private SchieberGameSession gameSitting1;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user2")
    private SchieberGameSession gameSitting2;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user3")
    private SchieberGameSession gameSitting3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "LOBBY_USERS",
            joinColumns = @JoinColumn(
                    name = "USER_ID",
                    insertable = false,
                    updatable = false,
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "LOBBY_ID",
                    insertable = false,
                    updatable = false,
                    referencedColumnName = "id"
            )
    )
    private Lobby lobby;

    @ManyToMany
    @JoinTable(name="friends",
    joinColumns=@JoinColumn(name="userA_id"),
    inverseJoinColumns=@JoinColumn(name="userB_id")
    )
    private List<User> friends;

    @ManyToMany
    @JoinTable(name="friends",  
    joinColumns=@JoinColumn(name="userB_id"),
    inverseJoinColumns=@JoinColumn(name="userA_id")
    )
    private List<User> friendOf;

    @OneToMany(mappedBy="toUser")
    private List<FriendRequest> pendingFriendRequests;

    @OneToMany(mappedBy="fromUser")
    private List<FriendRequest> sentFriendRequests;

    public UUID getId() {
        return id;
    }

    @Column(nullable = true, unique = true)
    protected String token;

    @Transient
    public String getDiscriminatorValue(){
        //This doesn't work, please uncomment it when you fix it
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);
        return val == null ? null : val.value();
    }

    @Transient
    protected String userType = getDiscriminatorValue();

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(other == null) {
            return false;
        }

        if(!(other instanceof User)) {
            return false;
        }

        User user = (User) other;

        return this.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<User> getFriends(){
        return friends;
    }

    public List<User> getFriendOf(){
        return friendOf;
    }

    public void setFriends(List<User> friends){
        this.friends = friends; 
    }

    public void setfriendOf(List<User> friendOf){
        this.friendOf = friendOf;
    }

    public List<FriendRequest> getPendingFriendRequests(){
        return this.pendingFriendRequests;
    }

    public List<FriendRequest> getSentFriendRequests(){
        return this.sentFriendRequests;
    }

    public void setPendingFriendRequests(List <FriendRequest> pendingFriendRequests){
        this.pendingFriendRequests = pendingFriendRequests;
    }

    public void setSentFriendRequests(List <FriendRequest> sentFriendRequests){
        this.sentFriendRequests = sentFriendRequests;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() { return userType; }

    public Lobby getLobby() { return lobby; }

    public void setLobby(Lobby lobby) { this.lobby = lobby; }
}
