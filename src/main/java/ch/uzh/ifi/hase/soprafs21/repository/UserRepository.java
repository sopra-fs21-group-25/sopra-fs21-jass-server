package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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
    
    @Query(value = "select * from Users u where (u.user_type = 'RegisteredUser') AND (u.status = 0) AND (u.id != :id) and NOT EXISTS(select * from Friends where ((userA_id = :id) and (userB_id = u.id)) or ((userA_id = u.id) and (userB_id = :id)))", nativeQuery = true)
    List<User> availableUsersForUserWithId(@Param("id") UUID id);
}
