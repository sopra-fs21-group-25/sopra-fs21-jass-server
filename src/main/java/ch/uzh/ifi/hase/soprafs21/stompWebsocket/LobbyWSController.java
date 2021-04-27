package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.UUID;

@Controller
public class LobbyWSController {


    @MessageMapping("/lobbies/{lobbyId}/table")
    @SendTo("/lobbies/{lobbyId}/table")
    public JasstableMSG lobbySitTop(@Payload JasstableMSG msg) throws Exception {
        return msg;
    }

    @MessageMapping("/lobbies/{lobbyId}/fetch")
    @SendTo("/lobbies/{lobbyId}/fetch")
    public String notifyAboutUsersInLobbyUpdate(@Payload String msg) {
        return msg;
    }

    @MessageMapping("/lobbies/{lobbyId}/notifyShutdown")
    @SendTo("/lobbies/{lobbyId}/shutdown")
    public String notifyAboutLobbyShutdown() {
        return "shutdown";
    }

    @MessageMapping("/lobbies/{lobbyId}/kicked/{username}")
    @SendTo("/lobbies/{lobbyId}/kicked/{username}")
    public String kickPlayerOutOfLobby() {
        return "kicked!";
    }
}
