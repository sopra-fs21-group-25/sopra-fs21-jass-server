package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWSController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @Autowired
    ChatWSController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/messages/user-to-user")
    public ChatMessageDTO storeAndRedirectMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessageDTO convertedMessageDTO = chatService.storeAndConvert(chatMessageDTO);
        return null;
    }



}
