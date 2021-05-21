package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWSController {

    @MessageMapping("/lobbies/{lobbyId}/initialSynchronization")
    @SendTo("/lobbies/{lobbyId}/gameInitialization")
    public GameInitializationMSG sendGameInitializationData(GameInitializationMSG msg) {
        return msg;
    }

    @MessageMapping("/games/{gameId}/fetch")
    @SendTo("/games/{gameId}/fetch")
    public String notifyToFetchNewGameState() {
        return "";
    }

}
