package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        UserGetDTO returnedUser = null;
        // convert API user to internal representation
        if (userPostDTO.getUserType().equals("registered")) {
            RegisteredUser userInput = DTOMapper.INSTANCE.convertUserPostDTOtoRegisteredUser(userPostDTO);
            User newUser = userService.createRegisteredUser(userInput);

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
            returnedUser.setUserType("registered");
        }

        if (userPostDTO.getUserType().equals("facebook")) {
            User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoFacebookUser(userPostDTO);
            User newUser = userService.createFacebookUser(userInput);

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
            returnedUser.setUserType("facebook");
        }

        if (userPostDTO.getUserType().equals("guest")) {
            GuestUser newGuest = userService.createGuestUser();

            returnedUser = DTOMapper.INSTANCE.convertEntityToUserGetDTO(newGuest);
            returnedUser.setUserType("guest");
        }

        if (returnedUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create guest user, due to wrong userType");
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
}
