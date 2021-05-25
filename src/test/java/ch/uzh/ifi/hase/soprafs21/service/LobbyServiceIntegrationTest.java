package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Types;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LobbyServiceIntegrationTest {
    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LobbyService lobbyService;

    private Lobby lobby;
    private Group group;
    private RegisteredUser susi = new RegisteredUser();
    private RegisteredUser timon = new RegisteredUser();

    Set<User> users;

    @BeforeEach
    public void createLobby(){
        lobbyRepository.deleteAll();
        lobbyRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
        groupRepository.deleteAll();
        groupRepository.flush();
        susi = new RegisteredUser();
        susi.setUsername("Susi");
        susi.setPassword("password4");
        susi.setStatus(UserStatus.ONLINE);
        susi.setId(UUID.randomUUID());

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("TimonAndPumba");
        timon.setStatus(UserStatus.ONLINE);
        timon.setId(UUID.randomUUID());

        timon = userRepository.save(timon);
        susi = userRepository.save(susi);
        userRepository.flush();

        users = new HashSet<>();
        users.add(susi);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);

        ingameModeMultiplicators.add(oneObject);

        group = groupRepository.saveAndFlush(new Group(GroupType.COLLECTIVE));

        lobby = new Lobby();
        lobby.setCreatorUsername(susi.getUsername());
        lobby.setUsersInLobby(users);
        lobby.setLobbyType("public");
        lobby.setMode(GameMode.SCHIEBER);
        lobby.setStartingCardSuit(Suit.ROSE);
        lobby.setStartingCardRank(Rank.TEN);
        lobby.setPointsToWin(2500);
        lobby.setWeisAllowed(false);
        lobby.setCrossWeisAllowed(Boolean.FALSE);
        lobby.setWeisAsk("never");
        lobby.setIngameModes(ingameModeMultiplicators);
        lobby.setGroup(group);
        lobby = lobbyRepository.saveAndFlush(lobby);

    }



    @Test
    public void getAccessibleLobbies_success()  {
        // given
        assertNotNull(lobbyRepository.findById(lobby.getId()));

        // when
        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertNotNull(returnedLobbies.size());
        assertEquals(returnedLobbies.get(0).getCreatorUsername(), "Susi");
        assertEquals(returnedLobbies.get(0).getLobbyType(), "public");
        userRepository.deleteAll();
    }

    @Test
    public void getAccessibleLobbies_onlyPrivateLobbies_noReturns()  {
        // when
        lobby.setLobbyType("private");
        lobbyRepository.saveAndFlush(lobby);

        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertEquals(returnedLobbies.size(), 0);
    }

    @Test
    public void getLobbyWithId_success()  {
        // given
        assertNotNull(lobbyRepository.findById(lobby.getId()));

        // when
       Lobby returnedLobby = lobbyService.getLobbyWithId(lobby.getId());

        // then
        assertEquals(returnedLobby.getId(), lobby.getId());
        assertEquals(returnedLobby.getUsersInLobby().size(), lobby.getUsersInLobby().size());
        assertEquals(returnedLobby.getCreatorUsername(), lobby.getCreatorUsername());
        assertEquals(returnedLobby.getMode(), lobby.getMode());
    }

    @Test
    public void addUserToLobby_success()  {
        // add Timon to the existing lobby
        LobbyPutUserWithIdDTO userNotInLobbyYet = new LobbyPutUserWithIdDTO();
        userNotInLobbyYet.setAdd(Boolean.TRUE);
        userNotInLobbyYet.setUserId(UUID.randomUUID());
        userNotInLobbyYet.setRemove(Boolean.FALSE);
        // when
        Lobby returnedLobby = lobbyService.addUserToLobby(userNotInLobbyYet, lobby.getId());

        // then
        assertEquals(returnedLobby.getId(), lobby.getId());
        assertEquals(returnedLobby.getUsersInLobby().size(), 2);
        assertEquals(returnedLobby.getCreatorUsername(), lobby.getCreatorUsername());
        assertEquals(returnedLobby.getMode(), lobby.getMode());
    }

    @Test
    public void addUserToLobby_fail_lobbyDoesNotExist()  {
        // add Timon to the existing lobby
        LobbyPutUserWithIdDTO usernotinLobbyyet = new LobbyPutUserWithIdDTO();
        usernotinLobbyyet.setAdd(Boolean.TRUE);
        usernotinLobbyyet.setUserId(UUID.randomUUID());
        usernotinLobbyyet.setRemove(Boolean.FALSE);

        // when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->  lobbyService.addUserToLobby(usernotinLobbyyet, UUID.randomUUID()));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        // then
        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Could not find a lobby with this id.", reason);
    }

    @Test
    public void removeUserFromLobby_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userNotInLobbyYet = new LobbyPutUserWithIdDTO();
        userNotInLobbyYet.setAdd(Boolean.TRUE);
        userNotInLobbyYet.setUserId(timonUUID);
        userNotInLobbyYet.setRemove(Boolean.FALSE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userNotInLobbyYet, lobby.getId());
        assertEquals(2, returnedLobby.getUsersInLobby().size());

        //when
        Lobby returnedLobbyWithOnlyOneUser = lobbyService.removeUserFromLobby(userNotInLobbyYet, lobby.getId());

        //then
        assertEquals(1, returnedLobbyWithOnlyOneUser.getUsersInLobby().size());
        assertEquals(lobby.getCreatorUsername(), returnedLobbyWithOnlyOneUser.getCreatorUsername());
    }

    @Test
    public void clearLobby_success()  {
        //when
        Lobby returnedLobby = lobbyService.clearLobby(lobby);

        //then
        assertEquals(0, returnedLobby.getUsersInLobby().size());
    }

    @After
    public void cleanDatabase(){
        lobbyRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();

        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(groupRepository.findAll().isEmpty());
    }

}
