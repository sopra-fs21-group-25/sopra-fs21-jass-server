package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GoogleUser")
public class GoogleUser extends User{

    public GoogleUser() {
        this.userType = UserType.GOOGLE;
    }
}
