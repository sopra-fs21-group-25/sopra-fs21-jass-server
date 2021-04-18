package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GuestUser")
public class GuestUser extends User {
}
