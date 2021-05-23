package ch.uzh.ifi.hase.soprafs21.stompWebsocket.controllerWS;

import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RestController
public class ChatWSController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @Autowired
    ChatWSController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/messages/incoming")
    public void storeAndRedirectMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessageDTO convertedMessageDTO = chatService.storeAndConvert(chatMessageDTO);
        simpMessagingTemplate.convertAndSend("/messages/outgoing/" + convertedMessageDTO.getEnvironmentId().toString(), convertedMessageDTO);
    }


/*  stomp websocket mappings
    --------------------------------------------------------------------------------------------------
    rest mappings
*/


    @GetMapping("/messages/bidirectional/{aId}/{bId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatMessageDTO> getAllMessagesUserAAndUserB(@PathVariable("aId") UUID aId, @PathVariable("bId") UUID bId) {
        return chatService.getAllMessagesBetweenUserAAndUserB(aId, bId);
    }

    @GetMapping("/messages/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatMessageDTO> getAllMessagesSentInLobby(@PathVariable("lobbyId") UUID lobbyId) {
        return chatService.getAllMessagesInLobby(lobbyId);
    }

    @GetMapping("/messages/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatMessageDTO> getAllMessagesSentInGame(@PathVariable("gameId") UUID gameId) {
        return chatService.getAllMessagesInGame(gameId);
    }

}
