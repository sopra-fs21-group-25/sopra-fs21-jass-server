package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.GameInitializationDTO;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameWSControllerTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;


    @Test
    public void verify_sendGameInitializationData_GameInitializationDTOIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        UUID lobbyId = UUID.randomUUID();
        GameInitializationDTO gameInitializationDTO =
                new GameInitializationDTO(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID());

        BlockingQueue<GameInitializationDTO> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/lobbies/" + lobbyId + "/gameInitialization", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameInitializationDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((GameInitializationDTO) payload);
            }
        });

        session.send("/app/lobbies/" + lobbyId + "/initialSynchronization", gameInitializationDTO);

        assertEquals(gameInitializationDTO, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_notifyToFetchNewGameState_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        UUID gameId = UUID.randomUUID();

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/games/" + gameId + "/fetch", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/games/" + gameId + "/fetch", "DoesNotMatterWhatWeSendHere.ItsJustForNotifying");

        assertNull(blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_sendShovingInfo_shoveToPartner_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        String msg = "ThisWillEitherBe\"choose\"OrOneOfTheIngameModesSuchAs\"SLALOM\"ButWillBeHandledInFrontend";

        UUID gameId = UUID.randomUUID();
        UUID partnerId = UUID.randomUUID();

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/games/" + gameId + "/shove/receive/" + partnerId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/games/" + gameId + "/shove/notify/" + partnerId, msg);

        assertEquals(msg, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }
}
