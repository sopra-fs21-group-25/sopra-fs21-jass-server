package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.FriendService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    FriendController(FriendService friendService, UserService userService){
        this.friendService = friendService;
        this.userService = userService;
    }

    @GetMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getFriends(@PathVariable UUID id){
        User user = userService.getUserById(id);
        
        List<User> friends = friendService.getFriends(user);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        for (User friend : friends) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(friend));
        }
        return userGetDTOs;
    }
    @DeleteMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeFriends(@RequestBody UserPostDTO user, @PathVariable UUID id){
        try{
            User currentUser = userService.getUserById(id);
            User removedUser = DTOMapper.INSTANCE.convertUserPostDTOtoRegisteredUser(user);
            userService.getUserById(removedUser.getId());
            friendService.removeFriend(currentUser, removedUser);
        }
        catch (Exception e){
            throw e;
        }
    }
}
