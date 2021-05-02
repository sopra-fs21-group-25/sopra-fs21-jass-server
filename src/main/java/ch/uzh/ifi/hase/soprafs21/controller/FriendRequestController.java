package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestPostDTO;
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

    private SseEmitter emitte; 

    private final FriendRequestService friendRequestService;
    private final UserService userService;
    FriendRequestController(FriendRequestService friendRequestService, UserService userService){
        this.friendRequestService = friendRequestService;
        this.userService = userService; 
    }

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
    }
}
