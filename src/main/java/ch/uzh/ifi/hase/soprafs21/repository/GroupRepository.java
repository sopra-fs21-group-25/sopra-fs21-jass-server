package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("groupRepository")
public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query(value = "SELECT g FROM Group g WHERE g.groupType = :groupType AND (SELECT COUNT (u) FROM g.users u WHERE u.id = :senderId) = 1 AND (SELECT COUNT (u) FROM g.users u WHERE u.id = :recipientId) = 1")
    Group findByGroupTypeAndUsersWithIds(@Param("groupType") GroupType groupType, @Param("senderId") UUID senderId, @Param("recipientId") UUID recipientId);

    @Query(value = "SELECT g FROM Group g WHERE EXISTS (SELECT ga FROM SchieberGameSession ga WHERE ga.group = g AND ga.id = :environmentId) OR EXISTS (SELECT l FROM Lobby l WHERE l.group = g AND l.id = :environmentId)")
    Group retrieveGroupByEnvironmentIdAsLobbyIdOrGameId(@Param("environmentId") UUID environmentId);

    @Query(value = "SELECT m FROM Message m WHERE m.group.groupType = ch.uzh.ifi.hase.soprafs21.constant.GroupType.BIDIRECTIONAL AND (SELECT COUNT (u) FROM m.group.users u WHERE u.id = :aId) = 1 AND (SELECT COUNT (u) FROM m.group.users u WHERE u.id = :bId) = 1 ORDER BY m.timestamp ASC")
    List<Message> findOrderedByTimestampUserToUserMessagesSentBetweenUserAAndUserB(@Param("aId") UUID aId, @Param("bId") UUID bId);

    @Query(value = "SELECT m FROM Message m WHERE m.group.groupType = ch.uzh.ifi.hase.soprafs21.constant.GroupType.COLLECTIVE AND m.group.lobby.id = :lobbyId ORDER BY m.timestamp ASC")
    List<Message> findOrderedByTimestampLobbyMessages(@Param("lobbyId") UUID lobbyId);

    @Query(value = "SELECT m FROM Message m WHERE m.group.groupType = ch.uzh.ifi.hase.soprafs21.constant.GroupType.COLLECTIVE AND m.group.game.id = :gameId ORDER BY m.timestamp ASC")
    List<Message> findOrderedByTimestampGameMessages(@Param("gameId") UUID gameId);

}
