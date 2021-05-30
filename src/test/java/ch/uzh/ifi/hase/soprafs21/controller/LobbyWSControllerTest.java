package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.LobbyJoinDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LobbyWSControllerTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    UUID lobbyId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    @Test
    public void verify_notifyAboutUsersInLobbyUpdate_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/lobbies/" + lobbyId + "/fetch", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/lobbies/" + lobbyId + "/fetch", "DoesNotMatterWhatWeSendHere");

        assertEquals("fetch", blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_notifyAboutLobbyShutdown_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/lobbies/" + lobbyId + "/shutdown", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/lobbies/" + lobbyId + "/notifyShutdown", "DoesNotMatterWhatWeSendHere");

        assertEquals("shutdown", blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_kickPlayerOutOfLobby_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/lobbies/" + lobbyId + "/kicked/" + userId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/lobbies/" + lobbyId + "/kicked/" + userId, "DoesNotMatterWhatWeSendHere");

        assertEquals("kicked", blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_sendLobbyInvitationToUser_LobbyJoinDTOIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        LobbyJoinDTO lobbyJoinDTO = new LobbyJoinDTO();
        lobbyJoinDTO.setLobbyId(lobbyId);
        lobbyJoinDTO.setLobbyCreator("creatinski");

        BlockingQueue<LobbyJoinDTO> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/lobbies/invite/" + userId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return LobbyJoinDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((LobbyJoinDTO) payload);
            }
        });

        session.send("/app/lobbies/invite/" + userId, lobbyJoinDTO);

        assertEquals(lobbyJoinDTO, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }
}
