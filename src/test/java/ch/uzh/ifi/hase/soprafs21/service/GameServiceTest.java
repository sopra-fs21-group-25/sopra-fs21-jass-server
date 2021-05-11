package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private UserService userService;

    private SchieberGameSession schieberGameSession;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        Card card1 = new Card();
        card1.setRank(Rank.ACE);
        card1.setSuit(Suit.BELL);

        Card card2 = new Card();
        card2.setRank(Rank.KING);
        card2.setSuit(Suit.BELL);

        Card card3 = new Card();
        card3.setRank(Rank.UNDER);
        card3.setSuit(Suit.BELL);

        Card card4 = new Card();
        card4.setRank(Rank.SEVEN);
        card4.setSuit(Suit.BELL);

        RegisteredUser peter = new RegisteredUser();
        peter.setUsername("Peter");
        UUID peterID = UUID.randomUUID();
        peter.setId(peterID);
        userService.createRegisteredUser(peter);

        RegisteredUser marie = new RegisteredUser();
        marie.setUsername("Marie");
        UUID marieID = UUID.randomUUID();
        marie.setId(marieID);
        userService.createRegisteredUser(marie);

        RegisteredUser ursine = new RegisteredUser();
        ursine.setUsername("Ursine");
        UUID ursineID = UUID.randomUUID();
        ursine.setId(ursineID);
        userService.createRegisteredUser(ursine);

        RegisteredUser kai = new RegisteredUser();
        kai.setUsername("Kai");
        UUID kaiID = UUID.randomUUID();
        kai.setId(kaiID);
        userService.createRegisteredUser(kai);

        Card[] listofCard = new Card[4];
        listofCard[0]= card1;
        listofCard[1]= card2;
        listofCard[2]= card3;
        listofCard[3]= card4;

        List<Card> listCard = new ArrayList<>();
        listCard.add(card1);
        listCard.add(card2);
        listCard.add(card3);
        listCard.add(card4);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);
        //IngameModeMultiplicatorObject[] myObjectforDTO = new IngameModeMultiplicatorObject[3];

        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
        UUID gameID = UUID.randomUUID();
        schieberGameSession.setId(gameID);
        schieberGameSession.setIngameModes(ingameModeMultiplicators);
        schieberGameSession.setCardPlayedByPlayer0(card1);
        schieberGameSession.setCardPlayedByPlayer1(card2);
        schieberGameSession.setCardPlayedByPlayer2(card3);
        schieberGameSession.setCardPlayedByPlayer3(card4);
        schieberGameSession.setCardsHeldByPlayer0(listCard);
        schieberGameSession.setCardsHeldByPlayer1(listCard);
        schieberGameSession.setCardsHeldByPlayer2(listCard);
        schieberGameSession.setCardsHeldByPlayer3(listCard);
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
    void getPlayerCards() {
    }

    @Test
    void createNewGame() {
    }

    @Test
    void updateStateOfGameWithId() {
    }

    @Test
    void deleteGameSession() {
    }
}