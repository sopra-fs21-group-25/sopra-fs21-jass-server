package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("groupRepository")
public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query(value = "SELECT g FROM Group g WHERE g.groupType = :groupType AND (SELECT COUNT (u) FROM g.users u WHERE u.id = :senderId) = 1 AND (SELECT COUNT (u) FROM g.users u WHERE u.id = :recipientId) = 1")
    Group findByGroupTypeAndUsersWithIds(@Param("groupType") GroupType groupType, @Param("senderId") UUID senderId, @Param("recipientId") UUID recipientId);

    @Query(value = "SELECT g FROM Group g WHERE g.lobby.id = :environmentId OR g.game.id = :environmentId")
    Group findByLobbyIdOrGameId(@Param("environmentId") UUID environmentId);
}