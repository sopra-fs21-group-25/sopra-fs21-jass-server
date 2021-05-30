package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import static org.mockito.BDDMockito.given;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatWSControllerStompTest {

    @LocalServerPort
    private Integer port;

    @MockBean
    private ChatService chatService;

    private WebSocketStompClient webSocketStompClient;

    private UUID senderId;
    private String senderUsername;
    private UUID environmentId;
    private Date timestamp;
    private String text;
    private ChatMessageDTO chatMessageDTO;


    @BeforeEach
    public void setup() {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        senderId = UUID.randomUUID();
        senderUsername = "stomper";
        environmentId = UUID.randomUUID();
        timestamp = new Date();
        text = "ChatWSControllerStompTest test message";
        chatMessageDTO = new ChatMessageDTO();

        chatMessageDTO.setSenderId(senderId);
        chatMessageDTO.setSenderUsername(senderUsername);
        chatMessageDTO.setEnvironmentId(environmentId);
        chatMessageDTO.setGroupType(GroupType.BIDIRECTIONAL);
        chatMessageDTO.setTimestamp(timestamp);
        chatMessageDTO.setText(text);

        given(chatService.storeAndConvert(Mockito.any())).willReturn(chatMessageDTO);
    }

    @Test
    public void verify_storeAndRedirectMessage_ChatMessageDTOIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        BlockingQueue<ChatMessageDTO> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/messages/outgoing/" + environmentId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((ChatMessageDTO) payload);
            }
        });

        session.send("/app/messages/incoming", chatMessageDTO);

        assertEquals(chatMessageDTO, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }
}
