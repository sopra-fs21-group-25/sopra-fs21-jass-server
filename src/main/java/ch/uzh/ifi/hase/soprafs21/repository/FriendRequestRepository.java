package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

@Repository("friendRequestRepository")
public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {
   /* @Query(value = "select fr.id, fr.from_user_id from friend_requests fr where to_user_id = :id", nativeQuery = true)
    List<String> pendingRequests(@Param("id") UUID id);*/

    FriendRequest getFriendRequestByFromUserAndToUser(User fromUser, User toUser);

    void deleteFriendRequestByFromUserAndToUser(User fromUser, User toUser);
}


