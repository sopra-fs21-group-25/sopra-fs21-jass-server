package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

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
        String val = this.getClass().getName();
        return val;
    }

    @Transient
    protected String userType = getDiscriminatorValue();

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

    public List<User> getfriendOf(){
        return friendOf;
    }

    public void setFriends(List<User> friends){
        this.friends = new ArrayList<>(friends); 
    }

    public void setfriendOf(List<User> friendOf){
        this.friendOf = new ArrayList<>(friendOf);
    }

    public List<FriendRequest> getPendingFriendRequests(){
        return this.pendingFriendRequests;
    }

    public List<FriendRequest> getSentFriendRequests(){
        return this.sentFriendRequests;
    }

    public void setPendingFriendRequests(List <FriendRequest> pendingFriendRequests){
        this.pendingFriendRequests = new ArrayList<>(pendingFriendRequests);
    }

    public void setSentFriendRequests(List <FriendRequest> sentFriendRequests){
        this.sentFriendRequests = new ArrayList<>(sentFriendRequests);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() { return userType; }
}
