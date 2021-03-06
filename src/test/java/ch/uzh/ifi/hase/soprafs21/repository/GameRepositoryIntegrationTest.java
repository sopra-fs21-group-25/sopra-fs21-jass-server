package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.CardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;



    private RegisteredUser doris;
    private RegisteredUser erwin;
    private RegisteredUser justus;
    private RegisteredUser esmeralda;
    private Card[] listofCard;
    private Card card0;
    private Card card1;
    private Card card2;
    private Card card3;
    private List<Card> listCard;
    private List<Group> listGroup;

    private SchieberGameSession schieberGameSession;
    private SchieberGameSession postDTO;
    private SchieberGamePostDTO schieberGamePostDTO;
    private SchieberGamePutDTO schieberGamePutDTO;
    private SchieberGameGetDTO schieber;
    private CardsGetDTO playerCards;
    private Group newGroup;

    @BeforeEach
    public void setupGameSession() {
        card0 = new Card();
        card0.setRank(Rank.KING);
        card0.setSuit(Suit.ACORN);
        card0.setIsTrumpf(true);


        card1 = new Card();
        card1.setRank(Rank.KING);
        card1.setSuit(Suit.BELL);
        card1.setIsTrumpf(false);

        card2 = new Card();
        card2.setRank(Rank.NINE);
        card2.setSuit(Suit.BELL);
        card2.setIsTrumpf(false);

        card3 = new Card();
        card3.setRank(Rank.UNDER);
        card3.setSuit(Suit.BELL);
        card3.setIsTrumpf(false);


        newGroup = new Group(GroupType.COLLECTIVE);
        listGroup = new ArrayList<>();
        listGroup.add(newGroup);


        doris = new RegisteredUser();
        doris.setUsername("Doris");
        doris.setStatus(UserStatus.ONLINE);
        doris.setPassword("password");
        doris.setToken("4");
        doris.setGroups(listGroup);


        erwin = new RegisteredUser();
        erwin.setUsername("Erwin");
        erwin.setStatus(UserStatus.ONLINE);
        erwin.setPassword("password");
        erwin.setToken("3");
        erwin.setGroups(listGroup);





// set up users
        justus = new RegisteredUser();
        justus.setUsername("Justus");
        justus.setStatus(UserStatus.ONLINE);
        justus.setPassword("password");
        justus.setToken("2");
        justus.setGroups(listGroup);

        esmeralda = new RegisteredUser();
        esmeralda.setUsername("Esmeralda");
        esmeralda.setStatus(UserStatus.ONLINE);
        esmeralda.setPassword("password");
        esmeralda.setToken("1");
        esmeralda.setGroups(listGroup);


        listofCard = new Card[4];
        listofCard[0] = card1;
        listofCard[1] = card2;
        listofCard[2] = card3;
        listofCard[3] = card0;

        listCard = new ArrayList<>();
        listCard.add(card1);
        listCard.add(card2);
        listCard.add(card3);
        listCard.add(card0);


        // List of Ingame Multiplicator Objects
        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);

        //List of Ingame Multiplicator Objects
        IngameModeMultiplicatorObject[] myObjectforDTO = new IngameModeMultiplicatorObject[1];
        myObjectforDTO[0] = new IngameModeMultiplicatorObject(IngameMode.ACORN, 1000);

        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
//        UUID gameID = UUID.randomUUID();
//        schieberGameSession.setId(gameID);
        schieberGameSession.setIngameModes(ingameModeMultiplicators);
        schieberGameSession.setCardPlayedByPlayer0(card1);
        schieberGameSession.setCardPlayedByPlayer1(card2);
        schieberGameSession.setCardPlayedByPlayer2(card3);
        schieberGameSession.setCardPlayedByPlayer3(card0);
        schieberGameSession.setCardsHeldByPlayer0(listCard);
        schieberGameSession.setCardsHeldByPlayer1(listCard);
        schieberGameSession.setCardsHeldByPlayer2(listCard);
        schieberGameSession.setCardsHeldByPlayer3(listCard);
        schieberGameSession.setCurrentIngameMode(IngameMode.ACORN);
        schieberGameSession.setPointsToWin(2000);
        schieberGameSession.setUser0(doris);
        schieberGameSession.setUser1(erwin);
        schieberGameSession.setUser2(esmeralda);
        schieberGameSession.setUser3(justus);
        schieberGameSession.setWeisAsk("true");
        schieberGameSession.setTrickToPlay(1);
        schieberGameSession.setCrossWeisAllowed(true);
        schieberGameSession.setWeisAllowed(Boolean.TRUE);
        schieberGameSession.setStartingCardRank(Rank.NINE);
        schieberGameSession.setStartingCardSuit(Suit.ROSE);
        schieberGameSession.setInitiallyStartingPlayer();
        schieberGameSession.setIdOfRoundStartingPlayer(doris.getId());
        schieberGameSession.setPointsTeam1_3(200);
        schieberGameSession.setPointsTeam0_2(200);
        schieberGameSession.setPlayer0startsTrick(true);
        schieberGameSession.setHasTrickStarted(false);
        schieberGameSession.setGroup(newGroup);

    }

    @Test
    void findByUserId() {
        // given
        entityManager.persistAndFlush(newGroup);
        entityManager.persistAndFlush(doris);
        entityManager.persistAndFlush(esmeralda);
        entityManager.persistAndFlush(erwin);
        entityManager.persistAndFlush(justus);

        entityManager.persist(schieberGameSession);
        entityManager.flush();

        // when
        User found = gameRepository.findByUserId(doris.getId());

        // then
        assertEquals(found.getId(), doris.getId());
        assertEquals(found.getUsername(), doris.getUsername());
        assertEquals(found.getStatus(), doris.getStatus());
        assertTrue(found instanceof RegisteredUser);


    }

    @AfterEach
    public void cleanUpEach() {
        gameRepository.delete(schieberGameSession);
        entityManager.remove(doris);
        entityManager.remove(esmeralda);
        entityManager.remove(erwin);
        entityManager.remove(justus);
        entityManager.remove(newGroup);

    }
}