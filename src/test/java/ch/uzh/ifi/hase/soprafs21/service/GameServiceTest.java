package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.repository.AvatarRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AvatarRepository avatarRepository;


    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private UserService userService;

    private SchieberGameSession schieberGameSession;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;

    private RegisteredUser peter;
    private RegisteredUser ursine;
    private RegisteredUser marie;
    private RegisteredUser kai;

    private RegisteredUser withoutCards;

    private List<Card> listCard0;
    private List<Card> listCard1;
    private List<Card> listCard2;
    private List<Card> listCard3;

    private Group group;
    private Card[] listofCard;
    private SchieberGamePutDTO schieberGamePutDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
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


        Mockito.when(avatarRepository.saveAndFlush(Mockito.any())).thenReturn(new Avatar());
        peter = new RegisteredUser();
        peter.setUsername("Peter");
        UUID peterID = UUID.randomUUID();
        peter.setId(peterID);
        userService.createRegisteredUser(peter);

        marie = new RegisteredUser();
        marie.setUsername("Marie");
        UUID marieID = UUID.randomUUID();
        marie.setId(marieID);
        userService.createRegisteredUser(marie);

        ursine = new RegisteredUser();
        ursine.setUsername("Ursine");
        UUID ursineID = UUID.randomUUID();
        ursine.setId(ursineID);
        userService.createRegisteredUser(ursine);

        kai = new RegisteredUser();
        kai.setUsername("Kai");
        UUID kaiID = UUID.randomUUID();
        kai.setId(kaiID);
        userService.createRegisteredUser(kai);

        withoutCards = new RegisteredUser();
        withoutCards.setUsername("RandomPlayer_nocardsleft");
        UUID withoutCardsId = UUID.randomUUID();
        withoutCards.setId(withoutCardsId);
        userService.createRegisteredUser(withoutCards);

        listofCard = new Card[4];
        listofCard[0]= card1;
        listofCard[1]= card2;
        listofCard[2]= card3;
        listofCard[3]= card4;

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
        group.setId(UUID.randomUUID());

        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
        UUID gameID = UUID.randomUUID();
        schieberGameSession.setId(gameID);
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


        Mockito.when(gameRepository.saveAndFlush(Mockito.any())).thenReturn(schieberGameSession);
        Mockito.when(groupRepository.retrieveGroupByEnvironmentIdAsLobbyIdOrGameId(Mockito.any())).thenReturn(group);

    }

    @Test
    void getGameWithId_success() {
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(schieberGameSession));
       SchieberGameSession foundSession = gameService.getGameWithId(schieberGameSession.getId());

       assertEquals(foundSession.getId(), schieberGameSession.getId());
       assertEquals(foundSession.getUser0(), schieberGameSession.getUser0());
       assertEquals(foundSession.getUser1(), schieberGameSession.getUser1());
       assertEquals(foundSession.getUser2(), schieberGameSession.getUser2());
       assertEquals(foundSession.getUser3(), schieberGameSession.getUser3());
       assertEquals(foundSession.getIngameModes(), schieberGameSession.getIngameModes());
       assertEquals(foundSession.getHasTrickStarted(), schieberGameSession.getHasTrickStarted());
    }

    @Test
    void getGameWithId_wrongID_fails() {
       // Mockito.when(gameRepository.findById(schieberGameSession.getId())).thenReturn(java.util.Optional.ofNullable(schieberGameSession));
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> gameService.getGameWithId(UUID.randomUUID()));
        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Could not find game with that id", reason);
    }

    @Test
    void getPlayerCards_test() {
        Optional<Card[]> returnedCards = gameService.getPlayerCards(peter.getId(), schieberGameSession);
        Optional<Card[]> returnedCards1 = gameService.getPlayerCards(marie.getId(), schieberGameSession);
        Optional<Card[]> returnedCards2 = gameService.getPlayerCards(kai.getId(), schieberGameSession);
        Optional<Card[]> returnedCards3 = gameService.getPlayerCards(ursine.getId(), schieberGameSession);
        Optional<Card[]> returnedCards4 = gameService.getPlayerCards(withoutCards.getId(), schieberGameSession);


        assertFalse(returnedCards.isEmpty());
        assertEquals(returnedCards.isPresent(),true);

        assertFalse(returnedCards1.isEmpty());
        assertEquals(returnedCards1.isPresent(),true);

        assertFalse(returnedCards2.isEmpty());
        assertEquals(returnedCards2.isPresent(),true);

        assertFalse(returnedCards3.isEmpty());
        assertEquals(returnedCards3.isPresent(),true);

        assertTrue(returnedCards4.isEmpty());

    }

    @Test
    void createNewGame() {
        //when
        Mockito.when(gameRepository.save(schieberGameSession)).thenReturn(schieberGameSession);
        Mockito.doNothing().when(gameRepository).flush();
        assertEquals(gameService.createNewGame(schieberGameSession, UUID.randomUUID()), schieberGameSession);

    }

    @Test
    void updateStateOfGameWithId_noPlayedCard_ingameModesnotNull() {
        //setup
        //set up of the put DTO
        schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setIngameMode(IngameMode.ACORN);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setLowOrHigh(Roundstart.OBE);
        schieberGamePutDTO.setPlayedCard(null);
        //when
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(schieberGameSession));

        SchieberGameSession updatedSchieberSession = gameService.updateStateOfGameWithId(schieberGameSession.getId(), schieberGamePutDTO);

        assertEquals(updatedSchieberSession.getId(), schieberGameSession.getId());
        assertEquals(updatedSchieberSession.getCurrentIngameMode(), schieberGamePutDTO.getIngameMode());
        assertEquals(updatedSchieberSession.getHasTrickStarted(), true);
        assertEquals(updatedSchieberSession.getStartedLowOrHigh(), schieberGameSession.getStartedLowOrHigh());
    }


    @Test
    void updateStateOfGameWithId_playedCardsNotNull_noIngameModes() {
        //setup
        //set up of the put DTO
        schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setIngameMode(null);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setLowOrHigh(Roundstart.UNDE);
        schieberGamePutDTO.setPlayedCard(card3);
        //when
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(schieberGameSession));

        SchieberGameSession updatedSchieberSession = gameService.updateStateOfGameWithId(schieberGameSession.getId(), schieberGamePutDTO);

        assertEquals(updatedSchieberSession.getId(), schieberGameSession.getId());
        assertEquals(updatedSchieberSession.getCardsHeldByPlayer0(), listCard0);
        assertEquals(updatedSchieberSession.getHasTrickStarted(), false);
        assertEquals(updatedSchieberSession.getTrickToPlay(), 2);
    }

    @Test
    void updateStateOfGameWithId_noCardsPlayed_noIngameModes() {
        //setup
        //set up of the put DTO
        schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setIngameMode(null);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setLowOrHigh(Roundstart.UNDE);
        schieberGamePutDTO.setPlayedCard(null);
        //when
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(schieberGameSession));

        SchieberGameSession updatedSchieberSession = gameService.updateStateOfGameWithId(schieberGameSession.getId(), schieberGamePutDTO);

        assertEquals(updatedSchieberSession.getId(), schieberGameSession.getId());
        assertEquals(updatedSchieberSession.getCardPlayedByPlayer0(), null);
        assertEquals(updatedSchieberSession.getCardPlayedByPlayer1(), null);
        assertEquals(updatedSchieberSession.getCardPlayedByPlayer2(), null);
        assertEquals(updatedSchieberSession.getCardPlayedByPlayer3(), null);

    }

    @Test
    void updateStateOfGameWithId_GameNotExistent() {
        //setup
        //set up of the put DTO
        schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setIngameMode(null);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setLowOrHigh(Roundstart.UNDE);
        schieberGamePutDTO.setPlayedCard(null);
        //when

        // then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> gameService.updateStateOfGameWithId(schieberGameSession.getId(), schieberGamePutDTO));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("Game with specified id not found", reason);

    }


    @Test
    void deleteGameSession() {
        Mockito.doNothing().when(gameRepository).deleteById(schieberGameSession.getId());
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(schieberGameSession));

        List<Group> groups = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(peter);
        groups.add(group);
        peter.setGroups(groups);
        group.setUsers(users);
        schieberGameSession.setGroup(group);

        gameService.deleteGameSession(schieberGameSession.getId());
        verify(gameRepository,times(1)).deleteById(schieberGameSession.getId());
    }

}