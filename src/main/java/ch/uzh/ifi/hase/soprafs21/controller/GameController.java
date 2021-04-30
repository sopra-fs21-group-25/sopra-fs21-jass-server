package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.rest.dto.CardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }


    /*
    -----------------------------------------------------------------------------------
                                          GET methods
    */

    /**
     *
     * @param gameId The UUID id of this game to be retrieved
     * @return A SchieberGameGetDTO having the cardsOfPlayer Card[] Array set to null
     */
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SchieberGameGetDTO getGameWithId(@PathVariable("gameId") UUID gameId) {
        SchieberGameSession schieberGameSession = gameService.getGameWithId(gameId);

        return DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);
    }

    /**
     * This is the GET one would actually want to use continuously in a running game since
     * the response will always contain the cards of the calling user
     * @param gameId The UUID id of this game to be retrieved
     * @param userId The UUID id of the user whose cards in this game are to be included in the DTO response
     * @return A SchieberGameGetDTO having a non-null Card[] Array called cardsOfPlayer containing that users cards in this game
     */
    @GetMapping("/games/{gameId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SchieberGameGetDTO getGameWithIdContainingCardsOfPlayer(@PathVariable("gameId") UUID gameId, @PathVariable("userId") UUID userId) {
        SchieberGameSession schieberGameSession = gameService.getGameWithId(gameId);

        Card[] cards = gameService.getPlayerCards(userId, schieberGameSession)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cards for this player do not exist"));

        SchieberGameGetDTO schieberGameGetDTO = DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);
        schieberGameGetDTO.setCardsOfPlayer(cards);

        return schieberGameGetDTO;
    }

    /**
     * This GET method can be used if one is only interested in the cards currently held by a player,
     * however it might be more convenient to use getGameWithIdContainingCardsOfPlayer instead
     * @param userId The UUID id of the user whose cards are to be retrieved
     * @param gameId The UUID id of the game in which the users cards are to be retrieved from
     * @return A CardsGetDTO encoding an Aay containing the specified user's cards
     */
    @GetMapping("/games/{gameId}/cards/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CardsGetDTO getCardsOfUserWithId(@PathVariable("userId") UUID userId, @PathVariable("gameId") UUID gameId) {
        SchieberGameSession schieberGameSession = gameService.getGameWithId(gameId);

        Card[] cards = gameService.getPlayerCards(userId, schieberGameSession)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cards for this player do not exist"));

        CardsGetDTO cardsGetDTO = new CardsGetDTO();
        cardsGetDTO.setCards(cards);
        return cardsGetDTO;
    }


    /*
    -----------------------------------------------------------------------------------
                                          POST methods
    */

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SchieberGameGetDTO createGame(@RequestBody SchieberGamePostDTO postDTO) {
        SchieberGameSession schieberGameSession = DTOMapper.INSTANCE.convertSchieberGamePostDTOtoEntity(postDTO, gameService);
        schieberGameSession = gameService.createNewGame(schieberGameSession);

        return DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);
    }


    /*
    -----------------------------------------------------------------------------------
                                          PUT methods
    */

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SchieberGameGetDTO updateGameState(@PathVariable("gameId") UUID gameId, @RequestBody SchieberGamePutDTO putDTO) {
        SchieberGameSession schieberGameSession = gameService.updateStateOfGameWithId(gameId, putDTO);

        Card[] cards = gameService.getPlayerCards(putDTO.getUserId(), schieberGameSession)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cards for this player do not exist"));

        SchieberGameGetDTO schieberGameGetDTO = DTOMapper.INSTANCE.convertEntityToSchieberGameGetDTO(schieberGameSession);
        schieberGameGetDTO.setCardsOfPlayer(cards);

        return schieberGameGetDTO;
    }

    @PutMapping("/games/{gameId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void closeGameSession(@PathVariable("gameId") UUID gameId) {
        gameService.deleteGameSession(gameId);
    }

}
