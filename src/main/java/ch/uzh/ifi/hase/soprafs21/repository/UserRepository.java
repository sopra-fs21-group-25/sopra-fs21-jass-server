package ch.uzh.ifi.hase.soprafs21.repository;

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
		   "where (type(u) = 'RegisteredUser') and (u.status = 0) and (u.id != :id) and \n" +
		       "not exists(select f from u.friends f where f.id = :id) and \n" +
		       "not exists(select f from u.friendOf f where f.id = :id)")
    List<User> availableUsersForUserWithId(@Param("id") UUID id);

    @Query(value = "SELECT u FROM User u WHERE u.status = 0")
    List<User> findAllOnlineUsers();

}
