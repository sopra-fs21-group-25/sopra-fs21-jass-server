package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByToken(String token);
    
    @Query("select distinct u from User u \n" +
		   "where (u.status = 0) and not (u.id = :id) and \n" +
		       "not exists(select f from u.friends f where f.id = :id) and \n" +
		       "not exists(select f from u.friendOf f where f.id = :id)")
    List<User> availableUsersForUserWithId(@Param("id") UUID id);


    List<User> findAllByStatus(UserStatus status);

    // -------------------------------

    @Query("SELECT DISTINCT u FROM User u WHERE NOT (u.id = :id) AND NOT EXISTS (SELECT f FROM u.friends f WHERE f.id = :id) AND NOT EXISTS (SELECT f FROM u.friendOf f WHERE f.id = :id)")
    List<User> usersNotBefriendedWith(@Param("id") UUID id);

}
