package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.CardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.RequestEntity.post;
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

    private RegisteredUser peter;
    private RegisteredUser marie;
    private RegisteredUser ursine;
    private RegisteredUser kai;
    private List<Card> listCard;
    private Card[] listofCard;

    private SchieberGameSession schieberGameSession;
    private SchieberGameSession postDTO;
    private SchieberGamePostDTO schieberGamePostDTO;
    private SchieberGamePutDTO schieberGamePutDTO;
    private SchieberGameGetDTO schieber;
    private CardsGetDTO playerCards;

    @BeforeEach
    public void setupGameSession(){
        Card card1 = new Card();
        card1.setRank(Rank.ACE);
        card1.setSuit(Suit.BELL);
        card1.setIsTrumpf(false);



        Card card2 = new Card();
        card2.setRank(Rank.KING);
        card2.setSuit(Suit.BELL);
        card2.setIsTrumpf(false);

        Card card3 = new Card();
        card3.setRank(Rank.UNDER);
        card3.setSuit(Suit.BELL);
        card3.setIsTrumpf(false);

        Card card4 = new Card();
        card4.setRank(Rank.SEVEN);
        card4.setSuit(Suit.BELL);
        card4.setIsTrumpf(false);

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

        listofCard = new Card[4];
        listofCard[0]= card1;
        listofCard[1]= card2;
        listofCard[2]= card3;
        listofCard[3]= card4;

        listCard = new ArrayList<>();
        listCard.add(card1);
        listCard.add(card2);
        listCard.add(card3);
        listCard.add(card4);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);
        IngameModeMultiplicatorObject[] myObjectforDTO = new IngameModeMultiplicatorObject[1];
        myObjectforDTO[0]= new IngameModeMultiplicatorObject(IngameMode.ACORN, 1000);

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


        //setup of the DTO
        schieberGamePostDTO = new SchieberGamePostDTO();
        schieberGamePostDTO.setPointsToWin(2000);

        schieberGamePostDTO.setPlayer0id(peter.getId());
        schieberGamePostDTO.setPlayer1id(marie.getId());
        schieberGamePostDTO.setPlayer2id(kai.getId());
        schieberGamePostDTO.setPlayer3id(ursine.getId());
        schieberGamePostDTO.setWeisAsk("true");
        schieberGamePostDTO.setCrossWeisAllowed(true);
        schieberGamePostDTO.setWeisAllowed(false);
        schieberGamePostDTO.setIngameModes(myObjectforDTO);
        schieberGamePostDTO.setStartingCard(card1);


//        postDTO = DTOMapper.INSTANCE.convertSchieberGamePostDTOtoEntity(schieberGamePostDTO, gameService);


        schieber = DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);

        playerCards = new CardsGetDTO();
        playerCards.setCards(listofCard);

        //set up of the put DTO
        schieberGamePutDTO = new SchieberGamePutDTO();
        schieberGamePutDTO.setIngameMode(IngameMode.ACORN);
        schieberGamePutDTO.setUserId(peter.getId());
        schieberGamePutDTO.setLowOrHigh(Roundstart.OBE);
        schieberGamePutDTO.setPlayedCard(card3);





    }

    @Test
    public void getGamewithID_test()throws Exception{
        //given
        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);

        //make request
        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString())
                .contentType(asJsonString(MediaType.APPLICATION_JSON));

        mockMvc.perform(getRequest)
                .andExpect(jsonPath("$.id", is(schieber.getId().toString())))
                .andExpect(jsonPath("$.player0id", is(schieber.getPlayer0id().toString())))
                .andExpect(jsonPath("$.player1id", is(schieber.getPlayer1id().toString())))
                .andExpect(jsonPath("$.player2id", is(schieber.getPlayer2id().toString())))
                .andExpect(jsonPath("$.player3id", is(schieber.getPlayer3id().toString())))
                .andExpect(jsonPath("$.weisAllowed", is(schieber.getWeisAllowed())))
                .andExpect(jsonPath("$.crossWeisAllowed", is(schieber.getCrossWeisAllowed())))
                .andExpect(status().isOk());
    }

    @Test
    public void getGamewithID_noID_test()throws Exception{
        //when
       given(gameService.getGameWithId(schieberGameSession.getId()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find game with that id"));
        //make request
        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString())
                .contentType(asJsonString(MediaType.APPLICATION_JSON));

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(status().reason(containsString("Could not find game with that id")));
    }

    @Test
    public void getGamewithID_ContainingCardsofPlayer_test()throws Exception{
        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);
        given(gameService.getPlayerCards(peter.getId(), schieberGameSession)).willReturn(java.util.Optional.ofNullable(listofCard));

        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString()+"/"+ peter.getId())
                .contentType(asJsonString(MediaType.APPLICATION_JSON));

        mockMvc.perform(getRequest)
                .andExpect(jsonPath("$.id", is(schieber.getId().toString())))
                .andExpect(jsonPath("$.pointsToWin", is(schieber.getPointsToWin())))
                .andExpect(jsonPath("$.pointsTeam0_2", is(schieber.getPointsTeam0_2())))
                .andExpect(jsonPath("$.pointsTeam1_3", is(schieber.getPointsTeam1_3())))
                .andExpect(jsonPath("$.trickToPlay", is(schieber.getTrickToPlay())))
                .andExpect(status().isOk());
    }

    @Test
    public void getGamewithID_ContainingCardsofPlayer_DoNotExist_test()throws Exception{
        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);
        //given(gameService.getPlayerCards(peter.getId(), schieberGameSession)).willReturn(java.util.Optional.ofNullable(listofCard));

        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString()+"/"+ peter.getId())
                .contentType(asJsonString(MediaType.APPLICATION_JSON));

        mockMvc.perform(getRequest)
                .andExpect(status().is(404))
                .andExpect(status().reason(containsString("Cards for this player do not exist")));
    }

    @Test
    public void getCardsOfUserWithId_test()throws Exception{
        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);
        given(gameService.getPlayerCards(peter.getId(), schieberGameSession)).willReturn(java.util.Optional.ofNullable(listofCard));

        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString()+"/cards/"+ peter.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                //.andExpect(jsonPath("$.cards", is(listofCard)))
                .andExpect(status().isOk());
    }

    @Test
    public void getCardsOfUserWithId_DoNotExist_test()throws Exception{
        given(gameService.getGameWithId(Mockito.any())).willReturn(schieberGameSession);
        //given(gameService.getPlayerCards(peter.getId(), schieberGameSession)).willReturn(java.util.Optional.ofNullable(listofCard));

        MockHttpServletRequestBuilder getRequest = get("/games/" + schieberGameSession.getId().toString()+"/cards/"+ peter.getId())
                .contentType(asJsonString(MediaType.APPLICATION_JSON));

        mockMvc.perform(getRequest)
                .andExpect(status().is(404))
                .andExpect(status().reason(containsString("Cards for this player do not exist")));
    }


//    @Test
//    public void createGame_test()throws Exception{
//        given(gameService.createNewGame(Mockito.any())).willReturn(schieberGameSession);
//
//        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/games")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(schieberGamePostDTO));
//
//
//        mockMvc.perform(postRequest)
//                .andExpect(status().is(405));
//    }



    @Test
    public void updateGameState_test()throws Exception{
        given(gameService.updateStateOfGameWithId(UUID.randomUUID(), schieberGamePutDTO)).willReturn(schieberGameSession);
        given(gameService.getPlayerCards(UUID.randomUUID(), schieberGameSession)).willReturn(java.util.Optional.ofNullable(listofCard));

        MockHttpServletRequestBuilder putRequest = put("/games/" + schieberGameSession.getId().toString())
                .content(asJsonString(schieberGamePutDTO))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().is(404));
    }



    @Test
    public void deleteGame_test()throws Exception{
        doNothing().when(gameService).deleteGameSession(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/games/" + schieberGameSession.getId().toString()+"/close");


        mockMvc.perform(putRequest)
                .andExpect(status().is(204));
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }

}