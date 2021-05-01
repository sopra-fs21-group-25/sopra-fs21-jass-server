package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWSController {

    @MessageMapping("/games/{gameId}/fetch")
    @SendTo("/games/{gameId}/fetch")
    public String notifyToFetchNewGameState() {
        return "";
    }

    @MessageMapping("/games/{gameId}/shutdown")
    @SendTo("/games/{gameId}/shutdown")
    public FinalScoreMSG sendFinalScoreMessage(@Payload FinalScoreMSG finalScore) {
        return finalScore;
    }

    @MessageMapping("/games/{gameId}/currentMode")
    @SendTo("/games/{gameId}/currentMode")
    public String sendCurrentMode(@Payload String currentMode){
            return currentMode;
    }
}
