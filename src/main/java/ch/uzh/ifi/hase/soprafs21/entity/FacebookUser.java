package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FacebookUser")
public class FacebookUser extends User{
}
