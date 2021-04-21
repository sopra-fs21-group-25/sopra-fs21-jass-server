package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") UUID id);

    @Query(value = "SELECT l FROM Lobby l WHERE NOT l.lobbyType = 'private'")
    List<Lobby> getAllExcludePrivate();

}
