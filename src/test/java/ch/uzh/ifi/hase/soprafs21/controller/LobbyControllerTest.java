package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyPosition;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @Test
    public void getAllLobbiesTest() throws Exception {
        Lobby l1 = new Lobby();
        Lobby l2 = new Lobby();

        l1.setId(new UUID(1,1));
        l1.setLobbyType("public");
        l1.setCreatorUsername("Arceus"); // The only legit creator
        l1.setMode(GameMode.SCHIEBER);
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(2500);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");

        l2.setId(new UUID(0,0));
        l2.setLobbyType("private");
        l2.setCreatorUsername("Celebi"); // Still the most mysterious one
        l2.setMode(GameMode.COIFFEUR);
        l2.setStartingCardSuit(Suit.ACORN);
        l2.setStartingCardRank(Rank.ACE);
        l2.setPointsToWin(5000);
        l2.setWeisAllowed(true);
        l2.setCrossWeisAllowed(false);
        l2.setWeisAsk("never");

        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(l1);
        lobbies.add(l2);

        given(lobbyService.getLobbies()).willReturn(lobbies);

        MockHttpServletRequestBuilder getRequest = get("/lobbies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(l1.getId().toString())))
                .andExpect(jsonPath("$[0].creatorUsername", is(l1.getCreatorUsername())))
                .andExpect(jsonPath("$[0].mode", is(l1.getMode().toString())))
                .andExpect(jsonPath("$[0].lobbyType", is(l1.getLobbyType())))
                .andExpect(jsonPath("$[0].startingCard.suit", is(l1.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$[0].startingCard.rank", is(l1.getStartingCardRank().toString())))
                .andExpect(jsonPath("$[0].pointsToWin", is(l1.getPointsToWin())))
                .andExpect(jsonPath("$[0].weis", is(l1.getWeisAllowed())))
                .andExpect(jsonPath("$[0].crossWeis", is(l1.getCrossWeisAllowed())))
                .andExpect(jsonPath("$[0].weisAsk", is(l1.getWeisAsk())))
                .andExpect(jsonPath("$[1].id", is(l2.getId().toString())))
                .andExpect(jsonPath("$[1].creatorUsername", is(l2.getCreatorUsername())))
                .andExpect(jsonPath("$[1].mode", is(l2.getMode().toString())))
                .andExpect(jsonPath("$[1].lobbyType", is(l2.getLobbyType())))
                .andExpect(jsonPath("$[1].startingCard.suit", is(l2.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$[1].startingCard.rank", is(l2.getStartingCardRank().toString())))
                .andExpect(jsonPath("$[1].pointsToWin", is(l2.getPointsToWin())))
                .andExpect(jsonPath("$[1].weis", is(l2.getWeisAllowed())))
                .andExpect(jsonPath("$[1].crossWeis", is(l2.getCrossWeisAllowed())))
                .andExpect(jsonPath("$[1].weisAsk", is(l2.getWeisAsk())));
    }

    @Test
    public void getAccessibleLobbies_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);

        User user4 = new RegisteredUser();
        user4.setUsername("Sam");
        user4.setId(UUID.randomUUID());


        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(2500);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Damiano");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(user);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);

        Lobby l2 = new Lobby();
        l2.setId(UUID.randomUUID());
        l2.setLobbyType("public");
        l2.setCreatorUsername("Gronk");
        l2.setMode(GameMode.SCHIEBER);
        l2.setStartingCardSuit(Suit.ACORN);
        l2.setStartingCardRank(Rank.NINE);
        l2.setPointsToWin(5020);
        l2.setWeisAllowed(true);
        l2.setCrossWeisAllowed(true);
        l2.setWeisAsk("never");
        l2.setUsersInLobby(userSet);
        l2.setUserTop(user);
        l2.setUserRight(user1);
        l2.setUserBottom(user2);
        l2.setUserLeft(user3);
        l2.setGroup(group);

        List<Lobby> accessibleLobbies = new ArrayList<>();
        accessibleLobbies.add(l1);
        accessibleLobbies.add(l2);


        given(lobbyService.getAccessibleLobbies(user4.getId())).willReturn(accessibleLobbies);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/accessible/"+ user4.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(l1.getId().toString())))
                .andExpect(jsonPath("$[0].creatorUsername", is(l1.getCreatorUsername())))
                .andExpect(jsonPath("$[0].mode", is(l1.getMode().toString())))
                .andExpect(jsonPath("$[0].lobbyType", is(l1.getLobbyType())))
                .andExpect(jsonPath("$[0].startingCard.suit", is(l1.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$[0].startingCard.rank", is(l1.getStartingCardRank().toString())))
                .andExpect(jsonPath("$[0].pointsToWin", is(l1.getPointsToWin())))
                .andExpect(jsonPath("$[0].weis", is(l1.getWeisAllowed())))
                .andExpect(jsonPath("$[0].crossWeis", is(l1.getCrossWeisAllowed())))
                .andExpect(jsonPath("$[0].weisAsk", is(l1.getWeisAsk())))
                .andExpect(jsonPath("$[0].userTop", is(notNullValue())))
                .andExpect(jsonPath("$[0].userLeft", is(notNullValue())))
                .andExpect(jsonPath("$[0].userBottom", is(notNullValue())))
                .andExpect(jsonPath("$[0].userRight", is(notNullValue())))
                .andExpect(jsonPath("$[1].id", is(l2.getId().toString())))
                .andExpect(jsonPath("$[1].creatorUsername", is(l2.getCreatorUsername())))
                .andExpect(jsonPath("$[1].mode", is(l2.getMode().toString())))
                .andExpect(jsonPath("$[1].lobbyType", is(l2.getLobbyType())))
                .andExpect(jsonPath("$[1].startingCard.suit", is(l2.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$[1].startingCard.rank", is(l2.getStartingCardRank().toString())))
                .andExpect(jsonPath("$[1].pointsToWin", is(l2.getPointsToWin())))
                .andExpect(jsonPath("$[1].weis", is(l2.getWeisAllowed())))
                .andExpect(jsonPath("$[1].crossWeis", is(l2.getCrossWeisAllowed())))
                .andExpect(jsonPath("$[1].weisAsk", is(l2.getWeisAsk())))
                .andExpect(jsonPath("$[1].userTop", is(notNullValue())))
                .andExpect(jsonPath("$[1].userLeft", is(notNullValue())))
                .andExpect(jsonPath("$[1].userBottom", is(notNullValue())))
                .andExpect(jsonPath("$[1].userRight", is(notNullValue())));
    }

    @Test
    public void getLobbyWithId_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);


        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(2500);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Damiano");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(user);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);

        given(lobbyService.getLobbyWithId(l1.getId())).willReturn(l1);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/"+ l1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(l1.getId().toString())))
                .andExpect(jsonPath("$.creatorUsername", is(l1.getCreatorUsername())))
                .andExpect(jsonPath("$.mode", is(l1.getMode().toString())))
                .andExpect(jsonPath("$.lobbyType", is(l1.getLobbyType())))
                .andExpect(jsonPath("$.startingCard.suit", is(l1.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$.startingCard.rank", is(l1.getStartingCardRank().toString())))
                .andExpect(jsonPath("$.pointsToWin", is(l1.getPointsToWin())))
                .andExpect(jsonPath("$.weis", is(l1.getWeisAllowed())))
                .andExpect(jsonPath("$.crossWeis", is(l1.getCrossWeisAllowed())))
                .andExpect(jsonPath("$.weisAsk", is(l1.getWeisAsk())))
                .andExpect(jsonPath("$.userTop", is(notNullValue())))
                .andExpect(jsonPath("$.userLeft", is(notNullValue())))
                .andExpect(jsonPath("$.userBottom", is(notNullValue())))
                .andExpect(jsonPath("$.userRight", is(notNullValue())));
    }

    @Test
    public void addUserToLobbyTest() throws Exception {
        // given user as Lobby creator
        User registeredUser = new RegisteredUser();
        registeredUser.setId(new UUID(0,0));
        registeredUser.setUsername("creator");

        UserGetDTO registUserDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(registeredUser);

        // Set containing the registeredUser
        Set<User> users = new HashSet<>();
        users.add(registeredUser);

        // given public Lobby
        Lobby lobby = new Lobby();
        lobby.setId(new UUID(1,1));
        lobby.setCreatorUsername(registeredUser.getUsername());
        lobby.setUsersInLobby(users);

        LobbyPutUserWithIdDTO lobbyPutUserWithIdDTO = new LobbyPutUserWithIdDTO();
        lobbyPutUserWithIdDTO.setUserId(registeredUser.getId());
        lobbyPutUserWithIdDTO.setAdd(Boolean.TRUE);
        lobbyPutUserWithIdDTO.setRemove(Boolean.FALSE);

        given(lobbyService.addUserToLobby(Mockito.any(),Mockito.any())).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/" + lobby.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPutUserWithIdDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId().toString())))
                .andExpect(jsonPath("$.usersInLobby[0].id", is(registUserDTO.getId().toString())))
                .andExpect(jsonPath("$.usersInLobby[0].username", is(registUserDTO.getUsername())))
                .andExpect(jsonPath("$.usersInLobby[0].userType", is(registUserDTO.getUserType())));

    }

    @Test
    public void removeUserFromLobby_Test() throws Exception {
        // given user as Lobby creator
        User registeredUser = new RegisteredUser();
        registeredUser.setId(new UUID(0,0));
        registeredUser.setUsername("creator");

        User willbeRemoved= new RegisteredUser();
        willbeRemoved.setId(UUID.randomUUID());
        willbeRemoved.setUsername("annoyingUser");

        UserGetDTO registUserDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(registeredUser);

        // Set containing the registeredUser
        Set<User> users = new HashSet<>();
        users.add(registeredUser);
        //users.add(willbeRemoved);

        // given public Lobby
        Lobby lobby = new Lobby();
        lobby.setId(new UUID(1,1));
        lobby.setCreatorUsername(registeredUser.getUsername());
        lobby.setUsersInLobby(users);

        LobbyPutUserWithIdDTO lobbyPutUserWithIdDTO = new LobbyPutUserWithIdDTO();
        lobbyPutUserWithIdDTO.setUserId(willbeRemoved.getId());
        lobbyPutUserWithIdDTO.setRemove(Boolean.TRUE);
        lobbyPutUserWithIdDTO.setAdd(Boolean.FALSE);

        given(lobbyService.removeUserFromLobby(Mockito.any(),Mockito.any())).willReturn(lobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/" + lobby.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPutUserWithIdDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId().toString())))
                .andExpect(jsonPath("$.usersInLobby[0].id", is(registUserDTO.getId().toString())))
                .andExpect(jsonPath("$.usersInLobby[0].username", is(registUserDTO.getUsername())))
                .andExpect(jsonPath("$.usersInLobby[0].userType", is(registUserDTO.getUserType())));

    }


    @Test
    public void sitUseratPositionInLobby_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);

        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(25090);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Perlin");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(user);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);


        given(lobbyService.placeUserAtPosition(l1.getId(), user1.getId(), LobbyPosition.BOTTOM)).willReturn(l1);


        MockHttpServletRequestBuilder putRequest = put("/lobbies/" + l1.getId().toString()+ "/sit/"+user1.getId()+"/"+LobbyPosition.BOTTOM)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(l1.getId().toString())))
                .andExpect(jsonPath("$.creatorUsername", is(l1.getCreatorUsername())))
                .andExpect(jsonPath("$.mode", is(l1.getMode().toString())))
                .andExpect(jsonPath("$.lobbyType", is(l1.getLobbyType())))
                .andExpect(jsonPath("$.startingCard.suit", is(l1.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$.startingCard.rank", is(l1.getStartingCardRank().toString())))
                .andExpect(jsonPath("$.pointsToWin", is(l1.getPointsToWin())))
                .andExpect(jsonPath("$.weis", is(l1.getWeisAllowed())))
                .andExpect(jsonPath("$.crossWeis", is(l1.getCrossWeisAllowed())))
                .andExpect(jsonPath("$.weisAsk", is(l1.getWeisAsk())))
                .andExpect(jsonPath("$.userTop", is(notNullValue())))
                .andExpect(jsonPath("$.userLeft", is(notNullValue())))
                .andExpect(jsonPath("$.userBottom", is(notNullValue())))
                .andExpect(jsonPath("$.userRight", is(notNullValue())));
    }

    @Test
    public void unSitUseratPositionInLobby_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);

        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(25090);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Perlin");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(null);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);


        given(lobbyService.unsitUserInLobby(l1.getId(), user.getId())).willReturn(l1);


        MockHttpServletRequestBuilder putRequest = put("/lobbies/" + l1.getId()+ "/unsit/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(l1.getId().toString())))
                .andExpect(jsonPath("$.creatorUsername", is(l1.getCreatorUsername())))
                .andExpect(jsonPath("$.mode", is(l1.getMode().toString())))
                .andExpect(jsonPath("$.lobbyType", is(l1.getLobbyType())))
                .andExpect(jsonPath("$.startingCard.suit", is(l1.getStartingCardSuit().toString())))
                .andExpect(jsonPath("$.startingCard.rank", is(l1.getStartingCardRank().toString())))
                .andExpect(jsonPath("$.pointsToWin", is(l1.getPointsToWin())))
                .andExpect(jsonPath("$.weis", is(l1.getWeisAllowed())))
                .andExpect(jsonPath("$.crossWeis", is(l1.getCrossWeisAllowed())))
                .andExpect(jsonPath("$.weisAsk", is(l1.getWeisAsk())))
                .andExpect(jsonPath("$.userTop", is(nullValue())))
                .andExpect(jsonPath("$.userLeft", is(notNullValue())))
                .andExpect(jsonPath("$.userBottom", is(notNullValue())))
                .andExpect(jsonPath("$.userRight", is(notNullValue())));
    }


    @Test
    public void createLobbyTest() throws Exception {

        // given user as Lobby creator
        User registeredUser = new RegisteredUser();
        registeredUser.setId(new UUID(2L, 2L));

        // Set containing the registeredUser
        Set<User> users = new HashSet<>();
        users.add(registeredUser);

        // dummy ingameModes set
        IngameModeMultiplicatorObject[] ingameModesArray =
                new IngameModeMultiplicatorObject[] {
                        new IngameModeMultiplicatorObject(IngameMode.ACORN, 1),
                        new IngameModeMultiplicatorObject(IngameMode.ROSE, 1),
                        new IngameModeMultiplicatorObject(IngameMode.BELL, 1),
                        new IngameModeMultiplicatorObject(IngameMode.SHIELD, 1),
                        new IngameModeMultiplicatorObject(IngameMode.UNDENUFE, 1),
                        new IngameModeMultiplicatorObject(IngameMode.OBENABE, 1)
                };
        List<IngameModeMultiplicatorObject> ingameModes = new ArrayList<>(6);
        for(IngameModeMultiplicatorObject m : ingameModesArray) { ingameModes.add(m); }

        // given public Lobby
        Lobby lobby = new Lobby();
        lobby.setId(new UUID(1,1));
        lobby.setLobbyType("public");
        lobby.setCreatorUsername(registeredUser.getUsername());
        lobby.setUsersInLobby(new HashSet<>());
        lobby.setMode(GameMode.SCHIEBER);
        lobby.setIngameModes(ingameModes);
        lobby.setStartingCardSuit(Suit.ROSE);
        lobby.setStartingCardRank(Rank.TEN);
        lobby.setPointsToWin(2500);
        lobby.setWeisAllowed(false);
        lobby.setCrossWeisAllowed(false);
        lobby.setWeisAsk("never");

        // initialize user
        registeredUser.setId(new UUID(0,0));
        registeredUser.setUsername("username");
        registeredUser.setLobby(lobby);

        // setting up the corresponding PostDTO
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setMode(GameMode.SCHIEBER);
        lobbyPostDTO.setLobbyType("public");
        lobbyPostDTO.setIngameModes(ingameModesArray);
        lobbyPostDTO.setStartingCard(new Card(Suit.ROSE, Rank.TEN));
        lobbyPostDTO.setPointsToWin(2500);
        lobbyPostDTO.setWeis(false);
        lobbyPostDTO.setCrossWeis(false);
        lobbyPostDTO.setWeisAsk("never");
        lobbyPostDTO.setCreatorUsername("username");
        lobbyPostDTO.setUsersInLobby(new User[0]);

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(lobby.getId().toString())))
                .andExpect(jsonPath("$.mode", is(asJsonString(lobby.getMode()).substring(1, asJsonString(lobby.getMode()).length()-1))))
                .andExpect(jsonPath("$.lobbyType", is(lobby.getLobbyType())))
                .andExpect(jsonPath("$.pointsToWin", is(lobby.getPointsToWin())))
                .andExpect(jsonPath("$.weis", is(lobby.getWeisAllowed())))
                .andExpect(jsonPath("$.crossWeis", is(lobby.getCrossWeisAllowed())))
                .andExpect(jsonPath("$.weisAsk", is(lobby.getWeisAsk())))
                .andExpect(jsonPath("$.creatorUsername", is(lobby.getCreatorUsername())));
    }

    @Test
    public void deleteLobbyNoCascadeDeleteChatGroup_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        List<User> userList = new ArrayList<>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);
        group.setUsers(userList);

        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(25090);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Perlin");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(null);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);


        given(lobbyService.getLobbyWithId(l1.getId())).willReturn(l1);
        doNothing().when(lobbyService).deleteNoCascadeChatGroupLobby(l1);


        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/" + l1.getId()+ "/delete/no-cascade")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());

    }


    @Test
    public void deleteLobbyCascadeDeleteChatGroup_Test() throws Exception {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        List<User> userList = new ArrayList<>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);
        group.setUsers(userList);

        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setMode(GameMode.SCHIEBER);
        l1.setLobbyType("public");
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(25090);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setCreatorUsername("Franko");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(user);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);


        given(lobbyService.getLobbyWithId(l1.getId())).willReturn(l1);
        doNothing().when(lobbyService).deleteCascadeChatGroupLobby(l1);


        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/" + l1.getId()+ "/delete/cascade")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
    }

    @Test
    public void getUsersInLobbyWithID_Test() throws Exception {


        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        Set<User> userSet = new HashSet<User>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        List<User> userList = new ArrayList<>();
        userSet.add(user);
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);
        Group group = new Group(GroupType.COLLECTIVE);
        group.setUsers(userList);

        Lobby l1 = new Lobby();
        l1.setId(new UUID(1,1));
        l1.setLobbyType("public");
        l1.setCreatorUsername(user.getUsername());
        l1.setMode(GameMode.SCHIEBER);
        l1.setStartingCardSuit(Suit.ROSE);
        l1.setStartingCardRank(Rank.TEN);
        l1.setPointsToWin(2500);
        l1.setWeisAllowed(false);
        l1.setCrossWeisAllowed(false);
        l1.setWeisAsk("never");
        l1.setUsersInLobby(userSet);
        l1.setUserTop(user);
        l1.setUserRight(user1);
        l1.setUserBottom(user2);
        l1.setUserLeft(user3);
        l1.setGroup(group);


        given(lobbyService.getLobbyWithId(l1.getId())).willReturn(l1);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/"+l1.getId()+"/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.usersInLobby", is(notNullValue())));
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
