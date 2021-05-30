package ch.uzh.ifi.hase.soprafs21.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
public class FriendRequestWSControllerTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    private UUID toId;

    @BeforeEach
    private void setup() {
        webSocketStompClient = new WebSocketStompClient((new StandardWebSocketClient()));
        webSocketStompClient.setMessageConverter(new StringMessageConverter());
        toId = UUID.randomUUID();
    }

    @Test
    public void verify_notifyFetchFriendRequests_messageReceived() throws ExecutionException, InterruptedException, TimeoutException {
        String msg = "new-request";

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/friend_requests/notify/" + toId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/friend_requests/" + toId, msg);

        assertEquals(msg, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }

    @Test
    public void verify_notifyToFetchFriendsAndUsers_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
        String payloadId = UUID.randomUUID().toString();

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/friends/notify_remove/" + toId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send("/app/friends/notify_remove/" + toId, payloadId);

        assertEquals(payloadId, blockingQueue.poll(1, TimeUnit.SECONDS));

        session.disconnect();
    }
}
