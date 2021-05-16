package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RegisteredUser")
public class RegisteredUser extends User {
    @Column
    protected String password;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @OneToOne
    @PrimaryKeyJoinColumn(name = "id")
    ProfileImage profileImage;
}
