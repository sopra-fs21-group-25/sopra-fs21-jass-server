package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    RegisteredUser boss;
    RegisteredUser fridolin;
    RegisteredUser silvia;
    RegisteredUser annegret;
    RegisteredUser timon;

    Set<User> users;

    Group group;
    Lobby lobby;

    List<Lobby> myLobbyList;
    HashSet<Lobby> myLobbyListpublic;
    HashSet<Lobby> myLobbyListfriends;
    private Group group1;
    private List<Group> groupList;

    @BeforeEach
    public void setup() {

        // given
        boss = new RegisteredUser();
        boss.setUsername("Boss");
        boss.setPassword("password1");
        boss.setStatus(UserStatus.OFFLINE);
        entityManager.persistAndFlush(boss);

        fridolin = new RegisteredUser();
        fridolin.setUsername("Fridolin");
        fridolin.setPassword("password2");
        fridolin.setStatus(UserStatus.ONLINE);
        entityManager.persistAndFlush(fridolin);

        silvia = new RegisteredUser();
        silvia.setUsername("Silvia");
        silvia.setPassword("password3");
        silvia.setStatus(UserStatus.ONLINE);
        entityManager.persistAndFlush(silvia);

        annegret = new RegisteredUser();
        annegret.setUsername("Annegret");
        annegret.setPassword("password4");
        annegret.setStatus(UserStatus.ONLINE);
        entityManager.persistAndFlush(annegret);

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("TimonAndPumba");
        timon.setStatus(UserStatus.ONLINE);

        users = new HashSet<>();
        users.add(boss);
        users.add(fridolin);
        users.add(silvia);
        users.add(annegret);

        group1 = new Group(GroupType.COLLECTIVE);
        group1.setUsers(new ArrayList<>(users));
        entityManager.persistAndFlush(group1);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        lobby = new Lobby();
        group = new Group(GroupType.COLLECTIVE);
        lobby.setGroup(group);
        lobby.setCreatorUsername(boss.getUsername());
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
        lobby.setUsersInLobby(users);
        lobby.setGroup(group1);
    }

    @Test
    public void findByUserId_success() {
        // given
        // Set containing the registeredUser
        entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(lobby);

        // when
        User found = lobbyRepository.findUserById((annegret.getId()));

        // then
        assertEquals(found.getId(), annegret.getId());
        assertEquals(found.getUsername(), annegret.getUsername());
        assertTrue(found instanceof User);
    }

    @Test
    public void findByUserId_fail() {
        // given
        // Set containing the registeredUser
        entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(lobby);

        // when
        User found = lobbyRepository.findUserById((timon.getId()));

        // then
        assertNull(found);
    }

    @Test
    public void getallexcludePrivate_success() {
        // given
        entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(lobby);

        // when
        List<Lobby> found = lobbyRepository.getAllExcludePrivate();

        // then
        assertEquals(found.get(0).getId(), lobby.getId());
        assertEquals(found.get(0).getLobbyType(), lobby.getLobbyType());
        assertTrue(found.get(0) instanceof Lobby);
    }

    @Test
    public void getallexcludePrivate_fail() {
        // given
        entityManager.persistAndFlush(group);
        lobby.setLobbyType("private");
        entityManager.persistAndFlush(lobby);

        // when
        List<Lobby> found = lobbyRepository.getAllExcludePrivate();

        // then
        assertEquals(0, found.size());
    }

    @Test
    public void getPublicLobbies_success() {
        // given
        entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(lobby);

        // when
        HashSet<Lobby> found = lobbyRepository.getPublicLobbies();

        // then
        assertEquals(found.stream().findFirst().get().getId(), lobby.getId());
        assertEquals(found.stream().findFirst().get().getLobbyType(), lobby.getLobbyType());
        assertTrue(found.stream().findFirst().get() instanceof Lobby);
    }

    @Test
    public void getPublicLobbies_fail() {
        // given
        entityManager.persistAndFlush(group);
        lobby.setLobbyType("friends");
        entityManager.persistAndFlush(lobby);

        // when
        HashSet<Lobby> found = lobbyRepository.getPublicLobbies();

        // then
        assertEquals(0, found.size());
    }

    @Test
    public void getFriendsLobbiesOfUserWithId_success() {
        // given
        entityManager.persistAndFlush(group);
        lobby.setLobbyType("friends");
        entityManager.persistAndFlush(lobby);

        List<User> timonsFriends = new ArrayList<>();
        timonsFriends.add(annegret);
        timonsFriends.add(fridolin);
        timon.setFriends(timonsFriends);
        entityManager.persistAndFlush(timon);

        List<User> fridolinsFriends = new ArrayList<>();
        fridolinsFriends.add(timon);
        fridolin.setFriends(fridolinsFriends);
        fridolin.setLobby(lobby);
        entityManager.persistAndFlush(fridolin);

        List<User> annegretsFriends = new ArrayList<>();
        annegretsFriends.add(timon);
        annegret.setFriends(annegretsFriends);
        annegret.setLobby(lobby);
        entityManager.persistAndFlush(annegret);

        // when
        HashSet<Lobby> foundLobbies = lobbyRepository.getFriendsLobbiesOfUserWithId(timon.getId());

        // then
        assertEquals(1, foundLobbies.size());
        assertEquals(foundLobbies.stream().findFirst().get(), fridolin.getLobby());
        assertEquals(foundLobbies.stream().findFirst().get(), annegret.getLobby());
    }

    @Test
    public void getFriendsLobbiesOfUserWithId_fails() {
        // given
        entityManager.persistAndFlush(group);
        lobby.setLobbyType("friends");
        entityManager.persistAndFlush(lobby);

        List<User> timonsFriends = new ArrayList<>();
        timonsFriends.add(fridolin);
        timon.setFriends(timonsFriends);
        entityManager.persistAndFlush(timon);

        List<User> fridolinsFriends = new ArrayList<>();
        fridolinsFriends.add(timon);
        fridolin.setFriends(fridolinsFriends);
        entityManager.persistAndFlush(fridolin);

        // when
        HashSet<Lobby> foundLobbies = lobbyRepository.getFriendsLobbiesOfUserWithId(fridolin.getId());

        // then
        assertEquals(0, foundLobbies.size());
    }

    @AfterEach
    public void cleanUpEach() {
        lobbyRepository.delete(lobby);
        entityManager.remove(group1);
        entityManager.remove(annegret);
        entityManager.remove(fridolin);
        entityManager.remove(timon);
        entityManager.remove(boss);
    }
}
