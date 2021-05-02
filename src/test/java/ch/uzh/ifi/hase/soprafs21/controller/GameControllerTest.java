package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private SchieberGameSession schieberGameSession;
    private SchieberGameGetDTO schieberGameGetDTO;
    private SchieberGameGetDTO schieber;

    @BeforeEach
    public void setupGameSession(){
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
        kai.setId(kaiID);

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
        schieberGameSession.setHasTrickStarted(true);


        //setup of the DTO



//        schieberGameGetDTO = new SchieberGameGetDTO();
//        schieberGameGetDTO.setTrickToPlay(1);
//        schieberGameGetDTO.setId(gameID);
//        schieberGameGetDTO.setPointsToWin(2000);
//        schieberGameGetDTO.setPlayer0id(peterID);
//        schieberGameGetDTO.setPlayer1id(marieID);
//        schieberGameGetDTO.setPlayer2id(kaiID);
//        schieberGameGetDTO.setPlayer3id(ursineID);
//        schieberGameGetDTO.setWeisAsk("true");
//        schieberGameGetDTO.setCrossWeisAllowed(Boolean.TRUE);
//        schieberGameGetDTO.setWeisAllowed(Boolean.FALSE);
//        schieberGameGetDTO.setCardsOfPlayer(listofCard);
//        schieberGameGetDTO.setIngameModes(myObjectforDTO);
//        schieberGameGetDTO.setHasTrickStarted(Boolean.FALSE);
//        schieberGameGetDTO.setTrickToPlay(1);
//        schieberGameGetDTO.setCardsPlayed(listofCard);
//        schieberGameGetDTO.setIdOfRoundStartingPlayer(peter.getId());
//        schieberGameGetDTO.setPointsTeam0_2(200);



        schieber = DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);

        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);
    }

//    @Test
//    public void getGamewithID_test()throws Exception{
//
//        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(getRequest)
//                .andExpect(jsonPath("$.id", is(schieber.getId())))
//                .andExpect(status().isOk());
//    }
//
//    private String asJsonString(final Object object) {
//        try {
//            return new ObjectMapper().writeValueAsString(object);
//        }
//        catch (JsonProcessingException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
//        }
//    }

}