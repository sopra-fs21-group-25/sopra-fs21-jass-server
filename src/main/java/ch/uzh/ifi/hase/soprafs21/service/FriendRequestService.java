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

    public FriendRequest sendFriendRequest(User fromUser, User toUser){
        FriendRequest newRequest = new FriendRequest(fromUser, toUser);
        this.friendRequestRepository.save(newRequest); 
        return newRequest; 
    }

    public void declineRequest(UUID id){
        friendRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a friend request with this id."));
        friendRequestRepository.deleteById(id);
    }

    public void acceptRequest(UUID id){
        FriendRequest newRequest = friendRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a friend request with this id."));
        friendService.addFriends(newRequest.getFromUser(), newRequest.getToUser());
        friendRequestRepository.deleteById(id);
    }

    /*public List<String> getPendingRequests(UUID id){
        return friendRequestRepository.pendingRequests(id);
    }*/
}
