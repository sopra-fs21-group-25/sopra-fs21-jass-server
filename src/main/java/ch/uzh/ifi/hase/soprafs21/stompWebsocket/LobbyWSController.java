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

    @MessageMapping("/lobbies/{lobbyId}/fetch")
    @SendTo("/lobbies/{lobbyId}/fetch")
    public String notifyAboutUsersInLobbyUpdate() {
        return "fetch";
    }

    @MessageMapping("/lobbies/{lobbyId}/notifyShutdown")
    @SendTo("/lobbies/{lobbyId}/shutdown")
    public String notifyAboutLobbyShutdown() {
        return "shutdown";
    }

    @MessageMapping("/lobbies/{lobbyId}/kicked/{userId}")
    @SendTo("/lobbies/{lobbyId}/kicked/{userId}")
    public String kickPlayerOutOfLobby() {
        return "kicked";
    }

    @MessageMapping("/lobbies/invite/{userId}")
    @SendTo("/lobbies/invite/{userId}")
    public String sendLobbyInvitationToUser(String msg) {
        return msg;
    }
}
