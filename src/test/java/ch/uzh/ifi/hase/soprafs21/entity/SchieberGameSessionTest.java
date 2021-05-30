package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SchieberGameSessionTest {

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
    private List<Card> listCard0_test;

    private Group group;
    private Card[] listofCard;
    private SchieberGamePutDTO schieberGamePutDTO;

    @BeforeEach
    public void setupGame(){

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
        UUID peterID = UUID.randomUUID();
        peter.setId(peterID);

        marie = new RegisteredUser();
        marie.setUsername("Marie");
        UUID marieID = UUID.randomUUID();
        marie.setId(marieID);

        ursine = new RegisteredUser();
        ursine.setUsername("Ursine");
        UUID ursineID = UUID.randomUUID();
        ursine.setId(ursineID);

        kai = new RegisteredUser();
        kai.setUsername("Kai");
        UUID kaiID = UUID.randomUUID();
        kai.setId(kaiID);


        withoutCards = new RegisteredUser();
        withoutCards.setUsername("RandomPlayer_nocardsleft");
        UUID withoutCardsId = UUID.randomUUID();
        withoutCards.setId(withoutCardsId);


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
    }



    @Test
    void composePlayerStartsTrickArray_Test() {
        Boolean[] arr =schieberGameSession.composePlayerStartsTrickArray();
        assertEquals(arr[0], schieberGameSession.getPlayer0startsTrick());
        assertEquals(arr[1], schieberGameSession.getPlayer1startsTrick());
        assertEquals(arr[2], schieberGameSession.getPlayer2startsTrick());
        assertEquals(arr[3], schieberGameSession.getPlayer3startsTrick());
    }

    @Test
    void composeCardsPlayedArray_Test() {
        Card[] arr = schieberGameSession.composeCardsPlayedArray();
        assertEquals(arr[0], schieberGameSession.getCardPlayedByPlayer0());
        assertEquals(arr[1], schieberGameSession.getCardPlayedByPlayer1());
        assertEquals(arr[2], schieberGameSession.getCardPlayedByPlayer2());
        assertEquals(arr[3], schieberGameSession.getCardPlayedByPlayer3());
    }

    @Test
    void composeUsernamesArray_Test() {
        String [] arr = schieberGameSession.composeUsernamesArray();
        assertEquals(arr[0], schieberGameSession.getUser0().getUsername());
        assertEquals(arr[1], schieberGameSession.getUser1().getUsername());
        assertEquals(arr[2], schieberGameSession.getUser2().getUsername());
        assertEquals(arr[3], schieberGameSession.getUser3().getUsername());

    }

    @Test
    void composeCardsAmountArray() {
         Integer[] arr = schieberGameSession.composeCardsAmountArray();
        assertEquals(arr[0], schieberGameSession.getCardsHeldByPlayer0().size());
        assertEquals(arr[1], schieberGameSession.getCardsHeldByPlayer1().size());
        assertEquals(arr[2], schieberGameSession.getCardsHeldByPlayer2().size());
        assertEquals(arr[3], schieberGameSession.getCardsHeldByPlayer3().size());
    }

    @Test
    void updateCardsHeldAndPlayedCardOfPlayerWithId() {
        listCard0_test = new ArrayList<>();
        listCard0_test.add(card2);
        listCard0_test.add(card3);
        listCard0_test.add(card4);
        schieberGameSession.updateCardsHeldAndPlayedCardOfPlayerWithId(peter.getId(), card1);
        schieberGameSession.updateCardsHeldAndPlayedCardOfPlayerWithId(ursine.getId(), card1);
        schieberGameSession.updateCardsHeldAndPlayedCardOfPlayerWithId(marie.getId(), card1);
        schieberGameSession.updateCardsHeldAndPlayedCardOfPlayerWithId(kai.getId(), card1);

        assertEquals(schieberGameSession.getCardPlayedByPlayer0(), card1);
        assertEquals(schieberGameSession.getCardPlayedByPlayer1(), card1);
        assertEquals(schieberGameSession.getCardPlayedByPlayer2(), card1);
        assertEquals(schieberGameSession.getCardPlayedByPlayer3(), card1);
    }

    @Test
    void allCardsPlayedThisTrick() {
        Boolean bool = schieberGameSession.allCardsPlayedThisTrick();
        assertEquals(bool, true);
  ;
    }

    @Test
    void assignPointsAndDetermineNextTrickStartingPlayerAccordingToCardsPlayedThisTrick_Test() {
        SchieberGamePutDTO schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setPlayedCard(card1);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setIngameMode(IngameMode.ACORN);
        schieberGamePutDTO.setLowOrHigh(Roundstart.UNDE);
        schieberGameSession.assignPointsAndDetermineNextTrickStartingPlayerAccordingToCardsPlayedThisTrick(schieberGamePutDTO);
        assertEquals(schieberGameSession.getTricksWonThisRoundTeam0_2(), 1);
        SchieberGamePutDTO schieberGamePut2DTO = new SchieberGamePutDTO();
        schieberGamePut2DTO.setPlayedCard(card1);
        schieberGamePut2DTO.setUserId(peter.getId());
        schieberGamePut2DTO.setIngameMode(IngameMode.UNDENUFE);
        schieberGamePut2DTO.setLowOrHigh(Roundstart.OBE);
        assertEquals(schieberGameSession.getTricksWonThisRoundTeam1_3(), 0);
    }

    @Test
    void achievePointsWithMatchBonus_Test() {
        schieberGameSession.setPointsInCurrentRoundTeam0_2(200);
        schieberGameSession.setPointsInCurrentRoundTeam1_3(2000);
        schieberGameSession.setTricksWonThisRoundTeam0_2(1);
        schieberGameSession.setTricksWonThisRoundTeam0_2(1);
        schieberGameSession.setCurrentIngameMode(IngameMode.ACORN);
        schieberGameSession.achievePointsWithMatchBonus();
        assertEquals(schieberGameSession.getPointsInCurrentRoundTeam0_2(), 0);
        assertEquals(schieberGameSession.getPointsInCurrentRoundTeam1_3(), 0);
        assertEquals(schieberGameSession.getTricksWonThisRoundTeam0_2(), 0);
        assertEquals(schieberGameSession.getTricksWonThisRoundTeam1_3(), 0);

    }

    @Test
    void updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick_Test() {
        schieberGameSession.setIdOfRoundStartingPlayer(peter.getId());
        schieberGameSession.updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick();
        assertEquals(schieberGameSession.getPlayer0startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer1startsTrick(), true);
        assertEquals(schieberGameSession.getPlayer2startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer3startsTrick(), false);

    }

    @Test
    void updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick_secondUser_Test() {
    schieberGameSession.setIdOfRoundStartingPlayer(ursine.getId());
        schieberGameSession.updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick();
        assertEquals(schieberGameSession.getPlayer0startsTrick(), true);
        assertEquals(schieberGameSession.getPlayer1startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer2startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer3startsTrick(), false);

    }

    @Test
    void updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick_thirdUser_Test() {
        schieberGameSession.setIdOfRoundStartingPlayer(kai.getId());
        schieberGameSession.updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick();
        assertEquals(schieberGameSession.getPlayer0startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer1startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer2startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer3startsTrick(), true);
    }
    @Test
    void updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick_4User_Test() {
        schieberGameSession.setIdOfRoundStartingPlayer(marie.getId());
        schieberGameSession.updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick();
        assertEquals(schieberGameSession.getPlayer0startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer1startsTrick(), false);
        assertEquals(schieberGameSession.getPlayer2startsTrick(), true);
        assertEquals(schieberGameSession.getPlayer3startsTrick(), false);
    }


}