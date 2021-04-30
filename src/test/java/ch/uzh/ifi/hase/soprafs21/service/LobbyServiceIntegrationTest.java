package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
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
    private UserRepository userRepository;

    @Autowired
    private LobbyService lobbyService;

    private Lobby lobby;
    private RegisteredUser susi = new RegisteredUser();
    private RegisteredUser timon = new RegisteredUser();

    Set<User> users;

    @BeforeEach
    public void createLobby(){
        lobbyRepository.deleteAll();
        userRepository.deleteAll();

        susi = new RegisteredUser();
        susi.setUsername("Susi");
        susi.setPassword("password4");
        susi.setStatus(UserStatus.ONLINE);

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("TimonAndPumba");
        timon.setStatus(UserStatus.ONLINE);

        users = new HashSet<>();
        users.add(susi);

        userRepository.saveAndFlush(timon);
        userRepository.saveAndFlush(susi);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        lobby = new Lobby();
        lobby.setCreatorUsername(susi.getUsername());
        lobby.setUsersInLobby(users);
        lobby.setLobbyType("public");
        lobby.setMode(GameMode.SCHIEBER);
        lobby.setStartingCardSuit(Suit.ROSE);
        lobby.setStartingCardRank(Rank.TEN);
        lobby.setPointsToWin(2500);
        lobby.setWeisAllowed(false);
        lobby.setCrossWeisAllowed(false);
        lobby.setWeisAsk("never");
        lobby.setIngameModes(ingameModeMultiplicators);
        susi.setLobby(lobby);
        lobbyRepository.saveAndFlush(lobby);

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
    }

    @Test
    public void getAccessibleLobbies_onlyPrivateLobbies_noreturns()  {
        // when
        lobby.setLobbyType("private");
        lobbyRepository.saveAndFlush(lobby);

        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertEquals(returnedLobbies.size(), 0);
    }

    @Test
    public void getLobbywithId_success()  {
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
    public void addUsertoLobby_success()  {
        // add Timon to the existing lobby
        LobbyPutUserWithIdDTO usernotinLobbyyet = new LobbyPutUserWithIdDTO();
        usernotinLobbyyet.setAdd(Boolean.TRUE);
        usernotinLobbyyet.setUserId(UUID.randomUUID());
        usernotinLobbyyet.setRemove(Boolean.FALSE);
        // when
        Lobby returnedLobby = lobbyService.addUserToLobby(usernotinLobbyyet, lobby.getId());

        // then
        assertEquals(returnedLobby.getId(), lobby.getId());
        assertEquals(returnedLobby.getUsersInLobby().size(), 2);
        assertEquals(returnedLobby.getCreatorUsername(), lobby.getCreatorUsername());
        assertEquals(returnedLobby.getMode(), lobby.getMode());
    }

    @Test
    public void addUsertoLobby_fail_lobbydoesnotExist()  {
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
    public void removeUserfromLobby_success()  {
        //given
        UUID myId = UUID.randomUUID();
        LobbyPutUserWithIdDTO usernotinLobbyyet = new LobbyPutUserWithIdDTO();
        usernotinLobbyyet.setAdd(Boolean.TRUE);
        usernotinLobbyyet.setUserId(myId);
        usernotinLobbyyet.setRemove(Boolean.FALSE);

        //setup check
        Lobby returnedLobby = lobbyService.addUserToLobby(usernotinLobbyyet, lobby.getId());
        assertEquals(2, returnedLobby.getUsersInLobby().size());

        //when
        Lobby returnedLobbywithonly1user = lobbyService.removeUserFromLobby(usernotinLobbyyet, lobby.getId());

        //then
        assertEquals(1, returnedLobbywithonly1user.getUsersInLobby().size());
        assertEquals(lobby.getCreatorUsername(), returnedLobbywithonly1user.getCreatorUsername());
    }

    @Test
    public void clearLobby_success()  {
        //when
        Lobby returnedLobby = lobbyService.clearLobby(lobby);

        //then
        assertEquals(0, returnedLobby.getUsersInLobby().size());
    }
}
