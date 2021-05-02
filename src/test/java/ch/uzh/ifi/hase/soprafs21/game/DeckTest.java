package ch.uzh.ifi.hase.soprafs21.game;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGameGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {


    @Test
    public void initializePlayerCard(){
        //setup
        SchieberGameSession schieberGameSession;
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

        User peter = new RegisteredUser();
        peter.setUsername("Peter");
        UUID peterID = UUID.randomUUID();
        peter.setId(peterID);

        User marie = new RegisteredUser();
        marie.setUsername("Marie");
        UUID marieID = UUID.randomUUID();
        marie.setId(marieID);

        User ursine = new RegisteredUser();
        ursine.setUsername("Ursine");
        UUID ursineID = UUID.randomUUID();
        ursine.setId(ursineID);

        User kai = new RegisteredUser();
        kai.setUsername("Kai");
        UUID kaiID = UUID.randomUUID();
        ursine.setId(kaiID);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);


        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
        UUID gameID = UUID.randomUUID();
        schieberGameSession.setId(gameID);
        schieberGameSession.setIngameModes(ingameModeMultiplicators);
        schieberGameSession.setCardPlayedByPlayer0(card1);
        schieberGameSession.setCardPlayedByPlayer1(card2);
        schieberGameSession.setCardPlayedByPlayer2(card3);
        schieberGameSession.setCardPlayedByPlayer3(card4);
        schieberGameSession.setCrossWeisAllowed(Boolean.TRUE);
        schieberGameSession.setCurrentIngameMode(IngameMode.ACORN);
        schieberGameSession.setPointsToWin(2000);
        schieberGameSession.setUser0(peter);
        schieberGameSession.setUser1(marie);
        schieberGameSession.setUser2(kai);
        schieberGameSession.setUser3(ursine);
        schieberGameSession.setWeisAsk("true");
        schieberGameSession.setTrickToPlay(1);
        schieberGameSession.setCrossWeisAllowed(true);
        schieberGameSession.setStartingCardRank(Rank.NINE);
        schieberGameSession.setStartingCardSuit(Suit.ROSE);
        //when
        Deck.initializePlayerCards(schieberGameSession);

        //then

        assertEquals(9, schieberGameSession.getCardsHeldByPlayer0().size());
        assertEquals(9, schieberGameSession.getCardsHeldByPlayer1().size());
        assertEquals(9, schieberGameSession.getCardsHeldByPlayer2().size());
        assertEquals(9, schieberGameSession.getCardsHeldByPlayer3().size());
        assertNotEquals(schieberGameSession.getCardsHeldByPlayer3(), schieberGameSession.getCardsHeldByPlayer2());
        assertNotEquals(schieberGameSession.getCardsHeldByPlayer1(), schieberGameSession.getCardsHeldByPlayer0());
        assertNotEquals(schieberGameSession.getCardsHeldByPlayer1(), schieberGameSession.getCardsHeldByPlayer2());
    }

}