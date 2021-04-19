package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/friend_requests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void sendFriendRequest(@RequestBody UserPostDTO user, @PathVariable UUID id){
        User toUser = DTOMapper.INSTANCE.convertUserPostDTOtoRegisteredUser(user);
        User fromUser = userService.getUserById(id);
        //throws exeption if user does not exist 
        userService.getUserById(toUser.getId());
        friendRequestService.sendFriendRequest(fromUser, toUser);
    }
}
