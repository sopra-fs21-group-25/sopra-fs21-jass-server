package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendService;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
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
import static org.mockito.Mockito.doThrow;


import java.util.*;

@WebMvcTest(FriendRequestController.class)
public class FriendRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @MockBean
    private UserService userService;

    @MockBean
    private FriendRequestService friendRequestService;

    @Test
    public void sendFriendRequest_ok() throws Exception {

        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        UserPutDTO testUser2 = new UserPutDTO(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        User toUser = DTOMapper.INSTANCE.convertUserPutDTOtoRegisteredUser(testUser2);

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser1);
        request.setToUser(toUser);

        given(userService.getUserById(testUser1.getId())).willReturn(testUser1);

        given(userService.getUserById(toUser.getId())).willReturn(toUser);

        given(friendRequestService.createAndStoreNewFriendRequest(Mockito.any())).willReturn(request);

        MockHttpServletRequestBuilder postRequest = post("/friend_requests/" + testUser1.getId() + "/" + toUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testUser2));

        mockMvc.perform(postRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.fromUsername", is(request.getFromUser().getUsername())))
                .andExpect(jsonPath("$.toId", is(request.getToUser().getId().toString())))
                .andExpect(jsonPath("$.fromId", is(request.getFromUser().getId().toString())));
                
    }

    @Test
    public void sendFriendRequest_userFrom_not_exist() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        UserPutDTO testUser2 = new UserPutDTO(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        User toUser = DTOMapper.INSTANCE.convertUserPutDTOtoRegisteredUser(testUser2);

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser1);
        request.setToUser(toUser);

        given(userService.getUserById(testUser1.getId()))
            .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a user with this id."));

        given(userService.getUserById(toUser.getId())).willReturn(toUser);

        given(friendRequestService.createAndStoreNewFriendRequest(Mockito.any())).willReturn(request);
        
        MockHttpServletRequestBuilder postRequest = post("/friend_requests/" + testUser1.getId() + "/" + toUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testUser2));

        mockMvc.perform(postRequest)
            .andExpect(status().is(404))
            .andExpect(status().reason(containsString("Could not find a user with this id.")));
    }

    @Test
    public void sendFriendRequest_userTo_not_exist() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        UserPutDTO testUser2 = new UserPutDTO(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        User toUser = DTOMapper.INSTANCE.convertUserPutDTOtoRegisteredUser(testUser2);

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser1);
        request.setToUser(toUser);

        given(userService.getUserById(toUser.getId()))
            .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a user with this id."));

        given(userService.getUserById(testUser1.getId())).willReturn(testUser1);

        given(friendRequestService.createAndStoreNewFriendRequest(Mockito.any())).willReturn(request);

        MockHttpServletRequestBuilder postRequest = post("/friend_requests/" + testUser1.getId() + "/" + toUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testUser2));

        mockMvc.perform(postRequest)
            .andExpect(status().is(404))
            .andExpect(status().reason(containsString("Could not find a user with this id.")));
    }

    @Test
    public void getPendingRequests_ok() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);

        List<FriendRequest> pendingRequests = new ArrayList<>();
        pendingRequests.add(request);

        testUser1.setPendingFriendRequests(pendingRequests);

        given(userService.getUserById(testUser1.getId())).willReturn(testUser1);

        MockHttpServletRequestBuilder getRequest = get("/friend_requests/with_username/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
            .andExpect(status().is(200))
            .andExpect(jsonPath("$[0].fromUsername", is(request.getFromUser().getUsername())))
            .andExpect(jsonPath("$[0].toId", is(request.getToUser().getId().toString())))
            .andExpect(jsonPath("$[0].fromId", is(request.getFromUser().getId().toString())));
    }

    @Test
    public void getPendingRequests_user_not_found() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);

        List<FriendRequest> pendingRequests = new ArrayList<>();
        pendingRequests.add(request);

        testUser1.setPendingFriendRequests(pendingRequests);

        given(userService.getUserById(testUser1.getId()))
            .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a user with this id."));

        MockHttpServletRequestBuilder getRequest = get("/friend_requests/with_username/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
            .andExpect(status().is(404))
            .andExpect(status().reason(containsString("Could not find a user with this id.")));
    }

    @Test
    public void declineRequest_ok() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);

        MockHttpServletRequestBuilder deleteRequest = delete("/friend_requests/decline/" + testUser2.getId() + "/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().is(204));
    }

    @Test
    public void declineRequest_request_not_exist() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);


        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a friend request with this id."))
            .when(friendRequestService).declineFriendRequest(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder deleteRequest = delete("/friend_requests/decline/" + testUser2.getId() + "/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
            .andExpect(status().is(404))
            .andExpect(status().reason(containsString("Could not find a friend request with this id.")));
    }

    @Test
    public void acceptRequest_ok() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);

        MockHttpServletRequestBuilder postRequest = post("/friend_requests/accept/" + testUser2.getId() + "/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().is(204));
    }

    @Test
    public void acceptRequest_request_not_exist() throws Exception {
        RegisteredUser testUser1 = new RegisteredUser(); 
        testUser1.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        testUser1.setUsername("Test1"); 

        RegisteredUser testUser2 = new RegisteredUser(); 
        testUser2.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        testUser2.setUsername("Test2"); 

        FriendRequest request = new FriendRequest();
        request.setFromUser(testUser2);
        request.setToUser(testUser1);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a friend request with this id."))
            .when(friendRequestService).acceptFriendRequest(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/friend_requests/accept/" + testUser2.getId() + "/" + testUser1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
            .andExpect(status().is(404))
            .andExpect(status().reason(containsString("Could not find a friend request with this id.")));
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
