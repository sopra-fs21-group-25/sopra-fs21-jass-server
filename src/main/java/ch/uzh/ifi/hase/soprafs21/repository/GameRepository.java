package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<SchieberGameSession, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User findByUserId(@Param("id") UUID id);

}
