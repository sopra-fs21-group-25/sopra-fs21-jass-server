package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.UUID;

@Controller
public class LobbyWSController {


    @MessageMapping("/lobbies/{lobbyId}/table")
    @SendTo("/topic/lobbies/{lobbyId}/table")
    public JasstableMSG lobbySitTop(@Payload JasstableMSG msg) throws Exception {
        return msg;
    }

    @SubscribeMapping("/topic/lobbies/{lobbyId}/fetch")
    @SendTo("/topic/lobbies/{lobbyId}/fetch")
    public String notifyToFetch() {
        return "notify";
    }
}
