package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.List;


@Service
@Transactional
public class FriendRequestService {

    private final Logger log = LoggerFactory.getLogger(FriendRequestService.class);

    private final FriendRequestRepository friendRequestRepository;
    private final FriendService friendService;

    @Autowired
    public FriendRequestService(@Qualifier("friendRequestRepository") FriendRequestRepository friendRequestRepository, FriendService friendService) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendService = friendService;
    }

    public FriendRequest createAndStoreNewFriendRequest(FriendRequest newRequest) throws ResponseStatusException {
        if(friendService.getFriends(newRequest.getFromUser()).contains(newRequest.getToUser())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot send friend request to user who is already a friend");
        }

        newRequest = friendRequestRepository.save(newRequest);
        friendRequestRepository.flush();

        log.debug("Created new friend request {}", newRequest);
        return newRequest;
    }

    public void acceptFriendRequest(User fromUser, User toUser) {
        // Note: this is not the request that's actually being accepted, it's the inverse request in case the accepting user
        // has sent a request himself. In this case we must delete both requests
        FriendRequest optionalInverseRequest = friendRequestRepository.getFriendRequestByFromUserAndToUser(toUser, fromUser);
        if(optionalInverseRequest != null) {
            friendRequestRepository.deleteFriendRequestByFromUserAndToUser(toUser, fromUser);
        }

        friendService.addFriends(fromUser, toUser);
        friendRequestRepository.deleteFriendRequestByFromUserAndToUser(fromUser, toUser);
    }

    public void declineFriendRequest(User fromUser, User toUser) {
        friendRequestRepository.deleteFriendRequestByFromUserAndToUser(fromUser, toUser);
    }
}
