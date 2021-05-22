package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;
    FriendRequestController(FriendRequestService friendRequestService, UserService userService){
        this.friendRequestService = friendRequestService;
        this.userService = userService; 
    }

    @PostMapping("/friend_requests/{fromUserId}/{toUserId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FriendRequestGetDTO createFriendRequest(@PathVariable("fromUserId") UUID fromUserId, @PathVariable("toUserId") UUID toUserId) {
        FriendRequest newRequest = DTOMapper.INSTANCE.convertFriendRequestPostDTOToFriendRequest(new FriendRequestPostDTO(fromUserId, toUserId), userService);
        friendRequestService.createAndStoreNewFriendRequest(newRequest);

        return DTOMapper.INSTANCE.convertEntityToFriendRequestGetDTO(newRequest);
    }

    @GetMapping("/friend_requests/with_username/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequestGetDTO> getPendingFriendRequestsWithUsername(@PathVariable("userId") UUID userId) {
        User requestingUser = userService.getUserById(userId);
        List<FriendRequest> pendingRequests = requestingUser.getPendingFriendRequests();
        List<FriendRequestGetDTO> getDTOs = new ArrayList<>();

        for(FriendRequest req : pendingRequests) {
            getDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendRequestGetDTO(req));
        }

        return getDTOs;
    }

    @PostMapping("/friend_requests/accept/{fromUserId}/{toUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    private void acceptFriendRequest(@PathVariable("fromUserId") UUID fromId, @PathVariable("toUserId") UUID toId) {
        User fromUser = userService.getUserById(fromId);
        User toUser = userService.getUserById(toId);
        friendRequestService.acceptFriendRequest(fromUser, toUser);
    }

    @DeleteMapping("/friend_requests/decline/{fromUserId}/{toUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    private void declineFriendRequest(@PathVariable("fromUserId") UUID fromId, @PathVariable("toUserId") UUID toId) {
        User fromUser = userService.getUserById(fromId);
        User toUser = userService.getUserById(toId);
        friendRequestService.declineFriendRequest(fromUser, toUser);
    }
}
