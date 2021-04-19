package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("registeredUserRepository")
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, UUID> {

    RegisteredUser findByUsername(String username);

}
