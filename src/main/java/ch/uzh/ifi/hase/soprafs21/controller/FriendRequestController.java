package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.*; 

import java.util.concurrent.*;
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

/*
    No longer in use. Will be removed soonish
    @PostMapping("/friend_requests/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FriendRequestGetDTO sendFriendRequest(@RequestBody UserPutDTO user, @PathVariable UUID id){
        User toUser = DTOMapper.INSTANCE.convertUserPutDTOtoRegisteredUser(user);
        User fromUser = userService.getUserById(id);
        //throws exeption if user does not exist 
        userService.getUserById(toUser.getId());
        FriendRequest newRequest = friendRequestService.sendFriendRequest(fromUser, toUser);
        return DTOMapper.INSTANCE.convertEntityToFriendRequestGetDTO(newRequest);
    }

    @GetMapping("/friend_requests/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequestGetDTO> getPendingRequests(@PathVariable UUID id){
        User user = userService.getUserById(id);
        List<FriendRequest> pendingRequests = user.getPendingFriendRequests();
        List<FriendRequestGetDTO> friendRequestGetDTOs = new ArrayList<>();

        for (FriendRequest request : pendingRequests) {
            friendRequestGetDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendRequestGetDTO(request));
        }
        return friendRequestGetDTOs;
    }

    @GetMapping("/friend_requests/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SseEmitter getPendingRequestsEvent(@PathVariable UUID id){
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    User user = userService.getUserById(id);
                    List<FriendRequest> pendingRequests = user.getPendingFriendRequests();
                    List<FriendRequestGetDTO> friendRequestGetDTOs = new ArrayList<>();

                    for (FriendRequest request : pendingRequests) {
                        friendRequestGetDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendRequestGetDTO(request));
                    }
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                      .data(friendRequestGetDTOs)
                      .id(id.toString())
                      .name("message");
                    emitter.send(event);
                    Thread.sleep(100000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }


    @DeleteMapping("/friend_requests/decline/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void declineRequest(@PathVariable UUID id){
        friendRequestService.declineRequest(id);
    }

    @PostMapping("/friend_requests/accept/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void acceptRequest(@PathVariable UUID id){
        friendRequestService.acceptRequest(id);
    }*/


    @PostMapping("/friend_requests/{fromUserId}/{toUserId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FriendRequestWithUsernameGetDTO createFriendRequest(@PathVariable("fromUserId") UUID fromUserId, @PathVariable("toUserId") UUID toUserId) {
        FriendRequest newRequest = DTOMapper.INSTANCE.convertFriendRequestPostDTOToFriendRequest(new FriendRequestPostDTO(fromUserId, toUserId), userService);
        friendRequestService.createAndStoreNewFriendRequest(newRequest);

        return DTOMapper.INSTANCE.convertEntityToFriendRequestWithUsernameGetDTO(newRequest);
    }

    @GetMapping("/friend_requests/with_username/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequestWithUsernameGetDTO> getPendingFriendRequestsWithUsername(@PathVariable("userId") UUID userId) {
        User requestingUser = userService.getUserById(userId);
        List<FriendRequest> pendingRequests = requestingUser.getPendingFriendRequests();
        List<FriendRequestWithUsernameGetDTO> getDTOs = new ArrayList<>();

        for(FriendRequest req : pendingRequests) {
            getDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendRequestWithUsernameGetDTO(req));
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
