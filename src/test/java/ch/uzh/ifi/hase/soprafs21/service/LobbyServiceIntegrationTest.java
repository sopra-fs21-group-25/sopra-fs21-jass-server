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
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    private List<Group> groupList;
    Set<User> users;

    @BeforeEach
    public void createLobby(){
        if(lobby != null) { lobbyRepository.delete(lobby); }
        if(susi != null) { userRepository.delete(susi); }
        if(timon != null) { userRepository.delete(timon); }
        if(group != null) { groupRepository.delete(group); }

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

        group = new Group(GroupType.COLLECTIVE);
        group.setUsers(new ArrayList<>(users));


        groupList = new ArrayList<>();
        groupList.add(group);
        susi.setGroups(groupList);
        timon.setGroups(groupList);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);

        ingameModeMultiplicators.add(oneObject);

        group = groupRepository.saveAndFlush(new Group(GroupType.COLLECTIVE));
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        timon.setGroups(groups);
        susi.setGroups(groups);

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
        lobby.setUserTop(susi);
        lobby.setGroup(group);
        lobby = lobbyRepository.saveAndFlush(lobby);


    }


    @Test
    public void getLobbies_Test()  {
        // given
        assertNotNull(lobbyRepository.findById(lobby.getId()));


        // when
        List<Lobby> returnedLobbies = lobbyService.getLobbies();

        // then
        assertNotEquals(returnedLobbies.size(), 0);
        assertEquals(returnedLobbies.get(0).getCreatorUsername(), "Susi");
        assertEquals(returnedLobbies.get(0).getLobbyType(), "public");
    }


    @Test
    public void getPublicAndFriendsLobbies_Test()  {
        // given
        assertNotNull(lobbyRepository.findById(lobby.getId()));

        // when
        List<Lobby> returnedLobbies = lobbyService.getPublicAndFriendsLobbies();

        // then
        assertEquals(returnedLobbies.size(), 1);
        assertEquals(returnedLobbies.get(0).getCreatorUsername(), "Susi");
        assertEquals(returnedLobbies.get(0).getLobbyType(), "public");
        assertEquals(returnedLobbies.get(0).getId(), lobby.getId());
        assertEquals(returnedLobbies.get(0).getMode(), lobby.getMode());
        assertEquals(returnedLobbies.get(0).getGroup(), lobby.getGroup());
        assertEquals(returnedLobbies.get(0).getPointsToWin(), lobby.getPointsToWin());
    }


    @Test
    public void getAccessibleLobbies_success()  {
        // given
        assertNotNull(lobbyRepository.findById(lobby.getId()));

        // when
        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertNotEquals(returnedLobbies.size(), 0);
        assertEquals(returnedLobbies.get(0).getCreatorUsername(), "Susi");
        assertEquals(returnedLobbies.get(0).getLobbyType(), "public");
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
        userNotInLobbyYet.setUserId(timon.getId());
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
    public void  removeUserFromLobby_LobbyInexistent_failed()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> lobbyService.removeUserFromLobby(userInLobby, UUID.randomUUID()));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Could not find a lobby with this id.", reason);
    }

    @Test
    public void  removeUserFromLobby_UserNotInLobby_failed()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.TRUE);
        userInLobby.setUserId(UUID.randomUUID());
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.getLobbyWithId(lobby.getId());
        assertEquals(1, returnedLobby.getUsersInLobby().size());


        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> lobbyService.removeUserFromLobby(userInLobby, lobby.getId()));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("User not in lobby", reason);
    }

    @Test
    public void  removeUserFromLobby_removeLeftUser_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserLeft(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.removeUserFromLobby(userInLobby, returnedLobby.getId());
        assertEquals(1, lobbyWithLessPlayers.getUsersInLobby().size());
        assertEquals(lobbyWithLessPlayers.getUserLeft(), null);
    }
    @Test
    public void  removeUserFromLobby_removeRigthUser_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserRight(susi);
        returnedLobby.setUserTop(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.removeUserFromLobby(userInLobby, returnedLobby.getId());
        assertEquals(1, lobbyWithLessPlayers.getUsersInLobby().size());
        assertEquals(lobbyWithLessPlayers.getUserTop(), null);
    }

    @Test
    public void  removeUserFromLobby_removeTopUser_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserBottom(timon);
        lobbyRepository.saveAndFlush(lobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.removeUserFromLobby(userInLobby, lobby.getId());
        assertEquals(1, lobbyWithLessPlayers.getUsersInLobby().size());
        assertEquals(lobbyWithLessPlayers.getUserBottom(), null);
        assertEquals(lobbyWithLessPlayers.getUserTop(), susi);
    }

    @Test
    public void  removeUserFromLobby_removeBottomUser_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserBottom(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.removeUserFromLobby(userInLobby, returnedLobby.getId());
        assertEquals(1, lobbyWithLessPlayers.getUsersInLobby().size());
        assertEquals(lobbyWithLessPlayers.getUserBottom(), null);
        assertEquals(lobbyWithLessPlayers.getUserTop(), susi);
    }

    @Test
    public void  unsitUserinLobby_noLobby_failed()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserBottom(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());



        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> lobbyService.unsitUserInLobby(UUID.randomUUID(), timonUUID));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Could not find a lobby with this id.", reason);
    }

    @Test
    public void  unsitUserinLobby_Bottom_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserBottom(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.unsitUserInLobby(returnedLobby.getId(), timonUUID );
        assertEquals(lobbyWithLessPlayers.getUserBottom(), null);
        assertEquals(lobbyWithLessPlayers.getUserTop(), susi);
    }

    @Test
    public void  unsitUserinLobby_Right_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserRight(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.unsitUserInLobby(returnedLobby.getId(), timonUUID );
        assertEquals(lobbyWithLessPlayers.getUserRight(), null);
        assertEquals(lobbyWithLessPlayers.getUserTop(), susi);
        assertEquals(2, lobbyWithLessPlayers.getUsersInLobby().size());
    }

    @Test
    public void  unsitUserinLobby_Left_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserLeft(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.unsitUserInLobby(returnedLobby.getId(), timonUUID );
        assertEquals(lobbyWithLessPlayers.getUserLeft(), null);
        assertEquals(lobbyWithLessPlayers.getUserTop(), susi);
        assertEquals(2, lobbyWithLessPlayers.getUsersInLobby().size());
    }

    @Test
    public void  unsitUserinLobby_Top_success()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserRight(susi);
        returnedLobby.setUserTop(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        Lobby lobbyWithLessPlayers =  lobbyService.unsitUserInLobby(returnedLobby.getId(), timonUUID );
        assertEquals(lobbyWithLessPlayers.getUserTop(), null);
        assertEquals(lobbyWithLessPlayers.getUserRight(), susi);
        assertEquals(2, lobbyWithLessPlayers.getUsersInLobby().size());
    }
    @Test
    public void  deleteNoCascadeChatGroupLobby_Test()  {
        //given
        UUID timonUUID = userRepository.findByUsername("Timon").getId();
        LobbyPutUserWithIdDTO userInLobby = new LobbyPutUserWithIdDTO();
        userInLobby.setAdd(Boolean.FALSE);
        userInLobby.setUserId(timonUUID);
        userInLobby.setRemove(Boolean.TRUE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(userInLobby, lobby.getId());
        returnedLobby.setUserRight(susi);
        returnedLobby.setUserTop(timon);
        lobbyRepository.saveAndFlush(returnedLobby);
        assertEquals(2, returnedLobby.getUsersInLobby().size());


        //when
        lobbyService.deleteNoCascadeChatGroupLobby(returnedLobby);
        assertEquals(lobbyRepository.findById(returnedLobby.getId()), Optional.empty());
    }




    @Test
    public void clearLobby_success()  {
        //when
        Lobby returnedLobby = lobbyService.clearLobby(lobby);

        //then
        assertEquals(0, returnedLobby.getUsersInLobby().size());
    }

    @AfterEach
    public void cleanDatabase(){
        lobbyRepository.delete(lobby);
        lobbyRepository.flush();
        userRepository.delete(timon);
        userRepository.delete(susi);
        userRepository.flush();
        groupRepository.delete(group);
        groupRepository.flush();
    }

}
