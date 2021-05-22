package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.constant.UserType;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/online")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllOnlineUsers() {
        List<User> onlineUsers = userService.getOnlineUsers();
        return onlineUsers.stream().map(u -> DTOMapper.INSTANCE.convertEntityToUserGetDTO(u)).collect(Collectors.toList());
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        UserGetDTO returnedUser = null;
        // convert API user to internal representation
        if (userPostDTO.getUserType().equals(UserType.REGISTERED.getType())) {
            RegisteredUser userInput = DTOMapper.INSTANCE.convertUserPostDTOtoRegisteredUser(userPostDTO);
            User newUser = userService.createRegisteredUser(userInput);

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
        }

        if (userPostDTO.getUserType().equals(UserType.GOOGLE.getType())) {
            User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoGoogleUser(userPostDTO);
            User newUser = userService.createGoogleUser(userInput);

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
        }

        if (userPostDTO.getUserType().equals(UserType.GUEST.getType())) {
            GuestUser newGuest = userService.createGuestUser();

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newGuest);
        }

        if (returnedUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create user, due to invalid userType");
        }

        return returnedUser;
    }

    @GetMapping("/availableusers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAvailableUsers(@PathVariable UUID id){
        List<User> users = userService.getAvailableUsers(id);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getSingleUser(@PathVariable UUID id) {
        // fetch particular user
        User user = userService.getUserById(id);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUser(@PathVariable UUID id, @RequestBody UserPutDTO userPutDTO) {
        RegisteredUser userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userService.updateUserProfile(id, userInput);
    }

    @PutMapping("/users/logout/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logoutUserWithToken(@PathVariable String token) { userService.logout(token); }

    // ---------------------------------------------------------

    @GetMapping("/users/not_friends/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getUsersThatAreNotFriendOfUserWithId(@PathVariable("userId") UUID userId) {
        List<User> remainingUsers = userService.getRemainingUsersForUserWithId(userId);
        List<UserGetDTO> getDTOs = new ArrayList<>();

        for(User u : remainingUsers) {
            getDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(u));
        }

        return getDTOs;
    }
}
