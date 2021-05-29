package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GameServiceIntegrationTest {
    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    private SchieberGameSession schieberGameSession;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;

    private RegisteredUser peter;
    private RegisteredUser ursine;
    private RegisteredUser marie;
    private RegisteredUser kai;

    private List<Card> listCard0;
    private List<Card> listCard1;
    private List<Card> listCard2;
    private List<Card> listCard3;

    private Card[] listofCard;
    private Group group;

    @BeforeEach
    public void setup() {
        card1 = new Card();
        card1.setRank(Rank.ACE);
        card1.setSuit(Suit.BELL);
        card1.setIsTrumpf(true);

        card2 = new Card();
        card2.setRank(Rank.KING);
        card2.setSuit(Suit.BELL);
        card2.setIsTrumpf(true);

        card3 = new Card();
        card3.setRank(Rank.UNDER);
        card3.setSuit(Suit.BELL);
        card3.setIsTrumpf(true);

        card4 = new Card();
        card4.setRank(Rank.SEVEN);
        card4.setSuit(Suit.BELL);
        card4.setIsTrumpf(true);


        peter = new RegisteredUser();
        peter.setUsername("Peter");
        peter.setPassword("Huhu");
        peter.setStatus(UserStatus.ONLINE);
        userRepository.save(peter);
        userRepository.flush();

        marie = new RegisteredUser();
        marie.setUsername("Marie");
        marie.setPassword("notNull");
        marie.setStatus(UserStatus.ONLINE);
        userRepository.save(marie);
        userRepository.flush();

        ursine = new RegisteredUser();
        ursine.setUsername("Ursine");
        ursine.setPassword("notNull");
        ursine.setStatus(UserStatus.ONLINE);
        userRepository.save(ursine);
        userRepository.flush();

        kai = new RegisteredUser();
        kai.setUsername("Kai");
        kai.setPassword("notNull");
        kai.setStatus(UserStatus.ONLINE);
        userRepository.save(kai);
        userRepository.flush();


        listofCard = new Card[4];
        listofCard[0] = card1;
        listofCard[1] = card2;
        listofCard[2] = card3;
        listofCard[3] = card4;

        listCard0 = new ArrayList<>();
        listCard0.add(card1);
        listCard0.add(card2);
        listCard0.add(card3);
        listCard0.add(card4);

        listCard1 = new ArrayList<>();
        listCard1.add(card1);
        listCard1.add(card2);
        listCard1.add(card3);
        listCard1.add(card4);

        listCard2 = new ArrayList<>();
        listCard2.add(card1);
        listCard2.add(card2);
        listCard2.add(card3);
        listCard2.add(card4);

        listCard3 = new ArrayList<>();
        listCard3.add(card1);
        listCard3.add(card2);
        listCard3.add(card3);
        listCard3.add(card4);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);

        group = new Group(GroupType.COLLECTIVE);
        groupRepository.saveAndFlush(group);


        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
        schieberGameSession.setIngameModes(ingameModeMultiplicators);
        schieberGameSession.setCardPlayedByPlayer0(card1);
        schieberGameSession.setCardPlayedByPlayer1(card2);
        schieberGameSession.setCardPlayedByPlayer2(card3);
        schieberGameSession.setCardPlayedByPlayer3(card4);
        schieberGameSession.setCardsHeldByPlayer0(listCard0);
        schieberGameSession.setCardsHeldByPlayer1(listCard1);
        schieberGameSession.setCardsHeldByPlayer2(listCard2);
        schieberGameSession.setCardsHeldByPlayer3(listCard3);
        schieberGameSession.setCurrentIngameMode(IngameMode.ACORN);
        schieberGameSession.setPointsToWin(2000);
        schieberGameSession.setUser0(peter);
        schieberGameSession.setUser1(marie);
        schieberGameSession.setUser2(kai);
        schieberGameSession.setUser3(ursine);
        schieberGameSession.setWeisAsk("true");
        schieberGameSession.setTrickToPlay(1);
        schieberGameSession.setCrossWeisAllowed(true);
        schieberGameSession.setWeisAllowed(Boolean.TRUE);
        schieberGameSession.setStartingCardRank(Rank.NINE);
        schieberGameSession.setStartingCardSuit(Suit.ROSE);
        schieberGameSession.setInitiallyStartingPlayer();
        schieberGameSession.setIdOfRoundStartingPlayer(peter.getId());
        schieberGameSession.setPointsTeam1_3(200);
        schieberGameSession.setPointsTeam0_2(200);
        schieberGameSession.setPlayer0startsTrick(true);
        schieberGameSession.setHasTrickStarted(false);
        schieberGameSession.setGroup(group);
        schieberGameSession = gameRepository.saveAndFlush(schieberGameSession);

    }


        @Test
    void getGameWithId_Repo_Test() {
        assertNotNull(gameRepository.findAll());
        SchieberGameSession foundSession = gameService.getGameWithId(schieberGameSession.getId());
        assertEquals(foundSession.getId(), schieberGameSession.getId());
        assertEquals(foundSession.getGroup().getGroupType(), schieberGameSession.getGroup().getGroupType());
        assertEquals(foundSession.getUser0(), schieberGameSession.getUser0());
        assertEquals(foundSession.getCardPlayedByPlayer0(), schieberGameSession.getCardPlayedByPlayer0());
    }


    @Test
    void deleteGameSession() {
        gameService.deleteGameSession(schieberGameSession.getId());
        assertTrue(gameRepository.findAll().isEmpty());
    }

    @Test
    void getUserWithId() {
        User myExpectedUser= gameService.getUserWithId(ursine.getId());
        assertEquals(myExpectedUser.getId(), ursine.getId());
        assertEquals(myExpectedUser.getUsername(), ursine.getUsername());
        assertEquals(myExpectedUser.getStatus(), ursine.getStatus());
    }

    @AfterEach
    public void cleanUp(){
        gameRepository.delete(schieberGameSession);
        groupRepository.delete(group);
        userRepository.delete(peter);
        userRepository.delete(kai);
        userRepository.delete(ursine);
        userRepository.delete(marie);
    }
}