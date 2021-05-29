package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_DISCRIMINATOR_TYPE")
@Table(name = "users")
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(unique = true)
    protected String username;

    @Email
    @Column(unique = true, length=60, nullable=true)
    protected String email;

    @Column
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

    @OneToMany(mappedBy="toUser", cascade = CascadeType.REMOVE)
    private List<FriendRequest> pendingFriendRequests;

    @OneToMany(mappedBy="fromUser", cascade = CascadeType.REMOVE)
    private List<FriendRequest> sentFriendRequests;

    @Column(nullable = true, unique = true)
    protected String token;

    @ManyToMany
    @JoinTable(name="user_In_Group", 
    joinColumns=@JoinColumn(name="user_id"),
    inverseJoinColumns=@JoinColumn(name="group_id"))
    private List<Group> groups;

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @OneToMany(mappedBy="sender", cascade = CascadeType.REMOVE)
    private List<Message> senderMessages;

    public List<Message> getSenderMessages() {
        return this.senderMessages;
    }

    public void setSenderMessages(List<Message> senderMessages) {
        this.senderMessages = senderMessages;
    }

    @Column
    protected UserType userType;

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

    public UUID getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserType getUserType() { return userType; }

    public Lobby getLobby() { return lobby; }

    public void setLobby(Lobby lobby) { this.lobby = lobby; }

    public SchieberGameSession getGameSitting0() {
        return gameSitting0;
    }

    public void setGameSitting0(SchieberGameSession gameSitting0) {
        this.gameSitting0 = gameSitting0;
    }

    public SchieberGameSession getGameSitting1() {
        return gameSitting1;
    }

    public void setGameSitting1(SchieberGameSession gameSitting1) {
        this.gameSitting1 = gameSitting1;
    }

    public SchieberGameSession getGameSitting2() {
        return gameSitting2;
    }

    public void setGameSitting2(SchieberGameSession gameSitting2) {
        this.gameSitting2 = gameSitting2;
    }

    public SchieberGameSession getGameSitting3() {
        return gameSitting3;
    }

    public void setGameSitting3(SchieberGameSession gameSitting3) {
        this.gameSitting3 = gameSitting3;
    }
}
