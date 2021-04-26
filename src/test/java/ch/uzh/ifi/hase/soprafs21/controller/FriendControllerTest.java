package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.service.FriendService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import java.util.*;

@WebMvcTest(FriendController.class)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @MockBean
    private UserService userService;

    @Test
    public void getFriendsTest_ok() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        List<User> friendsOf1 = new ArrayList<>(); 
        friendsOf1.add(testUser2); 

        List<User> friendsOf2 = new ArrayList<>(); 
        friendsOf2.add(testUser1); 

        testUser1.setfriendOf(friendsOf1);

        testUser2.setFriends(friendsOf2);

        given(userService.getUserById(testUser1.getId())).willReturn(testUser1);

        given(friendService.getFriends(Mockito.any())).willReturn(friendsOf1);

        MockHttpServletRequestBuilder getRequest = get("/friends/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id", is(testUser2.getId().toString())))
                .andExpect(jsonPath("$[0].username", is(testUser2.getUsername())));
    }

    @Test
    public void getFriendsTest_nouser() throws Exception{
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        given(userService.getUserById(testUser1.getId()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a user with this id."));
        
        MockHttpServletRequestBuilder getRequest = get("/friends/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);
                
        mockMvc.perform(getRequest)
                .andExpect(status().is(404))
                .andExpect(status().reason(containsString("Could not find a user with this id.")));
    }
    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
