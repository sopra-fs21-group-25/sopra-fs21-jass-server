package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") UUID id);

    @Query(value = "SELECT l FROM Lobby l WHERE NOT l.lobbyType = 'private'")
    List<Lobby> getAllExcludePrivate();

    @Query(value = "SELECT frnd.lobby FROM User frnd WHERE EXISTS (SELECT mself FROM frnd.friends mself WHERE mself.id = :user_id) AND frnd.lobby.lobbyType = 'friends'")
    HashSet<Lobby> getFriendsLobbiesOfUserWithId(@Param("user_id") UUID userId);

    @Query(value = "SELECT lobby FROM Lobby lobby WHERE lobby.lobbyType = 'public'")
    HashSet<Lobby> getPublicLobbies();


/*    // Initially used for retreiving accessible lobbies of a specific user with a single query; split into friendsLobbiesRetrieval and publicLobbiesRetrieval
    @Query(value = "SELECT l, frnd.lobby FROM Lobby l, User frnd WHERE EXISTS (SELECT mself FROM frnd.friends mself WHERE mself.id = :user_id) OR l.lobbyType = 'public'")
    List<Lobby> getAccessibleLobbies(@Param("user_id") UUID userId);*/
}
