package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.SchieberGameSession;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.Deck;
import ch.uzh.ifi.hase.soprafs21.game.IngameMode;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public SchieberGameSession getGameWithId(UUID id) {

        return this.gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find game with that id"));
    }

    public Optional<Card[]> getPlayerCards(UUID userId, SchieberGameSession game) {

        Card[] cards = null;
        List<Card> playersCards;

        if(game.getUser0().getId().equals(userId)) {
            playersCards = game.getCardsHeldByPlayer0();
            cards = playersCards.toArray(new Card[0]);
        } else if(game.getUser1().getId().equals(userId)) {
            playersCards = game.getCardsHeldByPlayer1();
            cards = playersCards.toArray(new Card[0]);
        } else if(game.getUser2().getId().equals(userId)) {
            playersCards = game.getCardsHeldByPlayer2();
            cards = playersCards.toArray(new Card[0]);
        } else if(game.getUser3().getId().equals(userId)) {
            playersCards = game.getCardsHeldByPlayer3();
            cards = playersCards.toArray(new Card[0]);
        }

        if(cards == null) {
            return Optional.empty();
        } else {
            return Optional.of(cards);
        }
    }

    public SchieberGameSession createNewGame(SchieberGameSession gameInput) {
        gameInput = gameRepository.save(gameInput);
        gameRepository.flush();

        log.debug("Created Information for Game {}", gameInput);
        return gameInput;
    }



    /**
     * Here happens the state update magic
     * @param gameId The id of the game to be updated
     * @param putDTO The DTO containing the updating user's id and either his card played or an ingameMode
     *               and in case the chosen ingameMode is Slalom also a Roundstart enum to indicate whether
     *               Slalom is being played from low to high or from high to low
     * @return The updated SchieberGameSession object
     */
    public SchieberGameSession updateStateOfGameWithId(UUID gameId, SchieberGamePutDTO putDTO) {
        SchieberGameSession schieberGameSession = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with specified id not found"));

        if(putDTO.getPlayedCard() == null && putDTO.getIngameMode() != null) {
            /*
            This is the case when the round-starting player has just chosen
            which ingameMode to play and hence only sends a putDTO containing
            the currentIngameMode he has chosen and if he has chosen "Slalom" then
            also if the round should start lowToHigh (Unde) or HighToLow (Obe).
            In this case the game only needs to update the following:
            - currentIngameMode: oldVal -> putDTO.getIngameMode
            - cardsHeldByPlayerANY: assign isTrumpf flag to true for all cards of Trump suit if ingameMode Trump was chosen
            - hasTrickStarted: false -> true
            - startedLowOrHigh: assign correct enum if Slalom has been chosen as ingameMode
             */

            schieberGameSession.setCurrentIngameMode(putDTO.getIngameMode());
            schieberGameSession.setTrumpFlags(putDTO.getIngameMode());
            schieberGameSession.setHasTrickStarted(true);
            if(putDTO.getIngameMode() == IngameMode.SLALOM) schieberGameSession.setStartedLowOrHigh(putDTO.getLowOrHigh());


        } else if(putDTO.getPlayedCard() != null && putDTO.getIngameMode() == null) {
            /*
            This is the case when at least one card has already been played and
            the game now needs to update the state such that the following get updated:
            - cardsHeldByPlayerX: remove playedCard from cards held by player X
            - cardPlayedByPlayerX: set to playedCard from player X
            */
            schieberGameSession.updateCardsHeldAndPlayedCardOfPlayerWithId(putDTO.getUserId(), putDTO.getPlayedCard());


            /*
            Additionally if cardPlayed by all players is not null (note (!): after I updated
            my own card played from null to someVal then I've been the last player to play
            this round hence the trick is ultimately finished and I have to update accordingly:
            - pointsInCurrentRoundTeam0_2: assign new points
            - pointsInCurrentRoundTeam1_3: assign new points
            - trickToPlay: oldVal -> (oldVal + 1) % 9; If this yields 0, then the round has terminated
            - playerXstartsTrick: true -> false
            - playerYstartsTrick: false -> true; where player Y is determined by winning the trick
            - hasTrickStarted: true -> false
            - cardPlayedByPlayerANY: oldVal -> null
            */
            if(schieberGameSession.allCardsPlayedThisTrick()) {
                schieberGameSession.assignPointsAndDetermineNextTrickStartingPlayerAccordingToCardsPlayedThisTrick();

                schieberGameSession.setHasTrickStarted(false);

                schieberGameSession.setCardPlayedByPlayer0(null);
                schieberGameSession.setCardPlayedByPlayer1(null);
                schieberGameSession.setCardPlayedByPlayer2(null);
                schieberGameSession.setCardPlayedByPlayer3(null);

                schieberGameSession.setTrickToPlay((schieberGameSession.getTrickToPlay() + 1) % 9);


                /*
                Additionally if the round has terminated, i.e. trickToPlay has been set
                to 0 again, then a new round starts. There is this mad thing in Swiss Jass
                called 'Match'. Essentially this means one team has won all tricks of the
                round. Since the total sum of points in any ingameMode always sums up to
                157, that's what that team would be rewarded with however when the team
                plays a 'Match', they receive another 100 extra points, i.e. in total
                they have 257 then (not accounting the multiplicator here).
                Thus the following needs to be adjusted:
                - pointsInCurrentRoundTeamX: pointsInCurrentRoundTeamX == 157 * multiplicator ? pointsInCurrentRoundTeamX += 100 * multiplicator : ...
                - pointsTeamX: pointsTeamX = pointsTeamX += pointsInCurrentRoundTeamX
                - currentIngameMode: oldVal -> null
                - playerYstartsTrick: true -> false
                - playerZstartsTrick: false -> true; where player Z is the one "right" to the player with id idOfRoundStartingPlayer
                - idOfRoundStartingPlayer: gets assigned the id of player Z from 1 line above
                - cardsHeldByPlayerALL: Deck.initializePlayerCards(...cardsHeldByPlayerALL) to deal new cards to each player
                */
                if(schieberGameSession.getTrickToPlay() == 0) {
                    schieberGameSession.achievePointsWithMatchBonus();

                    schieberGameSession.setCurrentIngameMode(null);

                    schieberGameSession.updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick();

                    Deck.initializePlayerCards(schieberGameSession);
                }
            }


            /*
            At some point at least one team will eventually achieve the pointsToWin upon
            which the game  terminates and will ultimately be removed from the database.
            That part of the logic will be handled in the frontend; the backend shall only
            provide the achieved points of each team and the points to win which is sufficient
            to determine a winner; additionally there will be provided another PUT mapping for
            shutting down the game session.
            */

        } else {
            /*
            In this case something went totally wrong. Throw an exception.
             */
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return schieberGameSession;
    }


    public void deleteGameSession(UUID gameId) {
        gameRepository.deleteById(gameId);
    }

    public User getUserWithId(UUID id) {
        return gameRepository.findByUserId(id);
    }
}
