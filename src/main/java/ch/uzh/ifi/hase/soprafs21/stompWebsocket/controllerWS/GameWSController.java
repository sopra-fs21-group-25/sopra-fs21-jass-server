package ch.uzh.ifi.hase.soprafs21.stompWebsocket.controllerWS;

import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.GameInitializationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWSController {

    @MessageMapping("/lobbies/{lobbyId}/initialSynchronization")
    @SendTo("/lobbies/{lobbyId}/gameInitialization")
    public GameInitializationDTO sendGameInitializationData(GameInitializationDTO msg) {
        return msg;
    }

    @MessageMapping("/games/{gameId}/fetch")
    @SendTo("/games/{gameId}/fetch")
    public String notifyToFetchNewGameState() {
        return "";
    }

    @MessageMapping("/games/{gameId}/shove/notify/{partnerId}")
    @SendTo("/games/{gameId}/shove/receive/{partnerId}")
    public String sendShovingInfo(String msg) { return msg; }
}
