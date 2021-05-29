package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LobbyService lobbyService;
    @InjectMocks
    private UserService userService;

    private Lobby lobby;
    private Lobby lobby2;
    private RegisteredUser hans;
    private RegisteredUser wanda;
    private RegisteredUser nicole;
    private RegisteredUser susi;
    private RegisteredUser timon;
    private RegisteredUser fred;
    Set<User> users;
    Set<User> users2;
    List<Lobby> myLobbyList;
    HashSet<Lobby> myLobbyListpublic;
    HashSet<Lobby> myLobbyListfriends;

    public LobbyServiceTest() {
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        hans = new RegisteredUser();
        hans.setUsername("Hans");
        hans.setPassword("password1");
        hans.setStatus(UserStatus.OFFLINE);
        hans.setId(UUID.randomUUID());

        wanda= new RegisteredUser();
        wanda.setUsername("Wanda");
        wanda.setPassword("password2");
        wanda.setStatus(UserStatus.ONLINE);
        wanda.setId(UUID.randomUUID());

        nicole= new RegisteredUser();
        nicole.setUsername("Nicole");
        nicole.setPassword("password3");
        nicole.setStatus(UserStatus.ONLINE);
        nicole.setId(UUID.randomUUID());

        susi = new RegisteredUser();
        susi.setUsername("Susi");
        susi.setPassword("password4");
        susi.setStatus(UserStatus.ONLINE);
        susi.setId(UUID.randomUUID());
        susi.setId(UUID.randomUUID());

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("TimonAndPumba");
        timon.setStatus(UserStatus.ONLINE);
        timon.setId(UUID.randomUUID());

        fred = new RegisteredUser();
        fred.setUsername("Fred");
        fred.setPassword("Freddy");
        fred.setStatus(UserStatus.ONLINE);
        fred.setId(UUID.randomUUID());

        users = new HashSet<>();
        users.add(hans);
        users.add(wanda);
        users.add(nicole);

        users2 = new HashSet<>();
        users2.add(hans);
        users2.add(wanda);

        Group group1 = new Group(GroupType.COLLECTIVE);
        group1.setUsers(new ArrayList<>(users));
        Group group2 = new Group(GroupType.COLLECTIVE);
        group2.setUsers(new ArrayList<>(users2));


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
        lobby.setGroup(group1);

        lobby2 = new Lobby();
        lobby2.setCreatorUsername(timon.getUsername());
        lobby2.setUsersInLobby(users2);
        lobby2.setLobbyType("private");
        lobby2.setMode(GameMode.SCHIEBER);
        lobby2.setStartingCardSuit(Suit.ROSE);
        lobby2.setStartingCardRank(Rank.TEN);
        lobby2.setPointsToWin(2500);
        lobby2.setWeisAllowed(false);
        lobby2.setCrossWeisAllowed(false);
        lobby2.setWeisAsk("never");
        lobby2.setIngameModes(ingameModeMultiplicators);
        lobby2.setGroup(group2);

        myLobbyList = new ArrayList<>();
        myLobbyListfriends =  new HashSet<>();
        myLobbyListpublic =  new HashSet<>();

        myLobbyList.add(lobby);
        myLobbyList.add(lobby2);
        myLobbyListpublic.add(lobby);
        myLobbyListfriends.add(lobby);

        Mockito.when(lobbyRepository.findUserById(Mockito.any())).thenReturn(susi);
        Mockito.when(lobbyRepository.getAllExcludePrivate()).thenReturn(myLobbyList);
        Mockito.when(lobbyRepository.getPublicLobbies()).thenReturn(myLobbyListpublic);
        Mockito.when(lobbyRepository.getFriendsLobbiesOfUserWithId(timon.getId())).thenReturn(myLobbyListfriends);

        userService.createRegisteredUser(susi);
        Mockito.when(lobbyRepository.saveAndFlush(Mockito.any())).thenReturn(lobby);
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(lobby);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void getaccessibleLobbies_validInputs_success() {
        // when
        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertEquals(returnedLobbies.get(0).getId(), lobby.getId());
        assertEquals(returnedLobbies.size(), 1);
        assertEquals(returnedLobbies.get(0).getLobbyType(), lobby.getLobbyType());
        assertEquals(returnedLobbies.get(0).getCreatorUsername(), lobby.getCreatorUsername());
    }

    @Test
    public void getaccessibleLobbies_onlyprivateLobbies_noreturns() {
        lobby.setLobbyType("private");
        myLobbyListpublic.remove(lobby);
        myLobbyListfriends.remove(lobby);
        Mockito.when(lobbyRepository.getPublicLobbies()).thenReturn(myLobbyListpublic);
        Mockito.when(lobbyRepository.getFriendsLobbiesOfUserWithId(timon.getId())).thenReturn(myLobbyListfriends);
        // when -> any object is being save in the userRepository -> return the dummy testUser
        List<Lobby> returnedLobbies = lobbyService.getAccessibleLobbies(timon.getId());

        // then
        assertEquals(returnedLobbies.size(), 0);
    }

    @Test
    public void createnewLobby_succes() {

        // when
        Lobby newLobby = lobbyService.createLobby(lobby);

        // then
        assertEquals(newLobby.getId(), lobby.getId());
        assertEquals(newLobby.getUsersInLobby(), lobby.getUsersInLobby());
        assertEquals(newLobby.getCreatorUsername(), lobby.getCreatorUsername());
        assertEquals(newLobby.getLobbyType(), lobby.getLobbyType());
    }

    @Test
    public void addUserSusiToLobby_success() {

        // setup
        LobbyPutUserWithIdDTO usernotinLobbyyet = new LobbyPutUserWithIdDTO();
        usernotinLobbyyet.setAdd(Boolean.TRUE);
        usernotinLobbyyet.setUserId(susi.getId());
        usernotinLobbyyet.setRemove(Boolean.FALSE);

        // when
        Mockito.when(lobbyRepository.findById(lobby.getId())).thenReturn(Optional.of(lobby));
        Lobby newLobby = lobbyService.addUserToLobby(usernotinLobbyyet, lobby.getId());

        // then
        assertEquals(lobby.getId(), newLobby.getId());
        assertEquals(lobby.getUsersInLobby(), newLobby.getUsersInLobby());
        assertEquals(lobby.getCreatorUsername(), newLobby.getCreatorUsername());
        assertEquals(lobby.getLobbyType(), newLobby.getLobbyType());
    }

    @Test
    public void addUserSusiToLobby_lobbynotfound() {

        // when
        LobbyPutUserWithIdDTO usernotinLobbyyet = new LobbyPutUserWithIdDTO();
        usernotinLobbyyet.setAdd(Boolean.TRUE);
        usernotinLobbyyet.setUserId(susi.getId());
        usernotinLobbyyet.setRemove(Boolean.FALSE);
        Mockito.when(lobbyRepository.findById(UUID.randomUUID())).thenReturn(Optional.of(lobby));

        // then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> lobbyService.addUserToLobby(usernotinLobbyyet, lobby.getId()));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Could not find a lobby with this id.", reason);
    }

    @Test
    public void clearLobby_success() {

        // when
        Lobby emptyLobby = lobbyService.clearLobby(lobby2);

        // then
        assertEquals(emptyLobby.getUsersInLobby().size(), 0);
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

