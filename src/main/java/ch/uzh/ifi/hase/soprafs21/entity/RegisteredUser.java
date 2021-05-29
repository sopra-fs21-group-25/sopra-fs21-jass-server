package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RegisteredUser")
public class RegisteredUser extends User {
    @Column
    protected String password;

    public RegisteredUser() {
        this.userType = UserType.REGISTERED;
    }


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Avatar avatar;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Avatar getAvatar() { return avatar; }

    public void setAvatar(Avatar avatar) { this.avatar = avatar; }
}
