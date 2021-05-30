//package ch.uzh.ifi.hase.soprafs21.stomp;
//
//
//import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
//import ch.uzh.ifi.hase.soprafs21.service.ChatService;
//import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
//import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.GameInitializationDTO;
//import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.LobbyJoinDTO;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.converter.StringMessageConverter;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//
//import java.lang.reflect.Type;
//import java.util.Date;
//import java.util.UUID;
//import java.util.concurrent.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.BDDMockito.given;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class StompEndpointTests {
//
//    @LocalServerPort
//    private Integer port;
//
//    @MockBean
//    private ChatService chatService;
//
//    private WebSocketStompClient webSocketStompClientJackson
//            = new WebSocketStompClient(new StandardWebSocketClient());
//
//    private StompSession sessionJackson;
//    private StompSession sessionString;
//
//    private WebSocketStompClient webSocketStompClientString
//            = new WebSocketStompClient(new StandardWebSocketClient());
//
//    private UUID senderId;
//    private String senderUsername;
//    private UUID environmentId;
//    private Date timestamp;
//    private String text;
//    private ChatMessageDTO chatMessageDTO;
//
//    private UUID toId;
//
//    private UUID lobbyId;
//    private UUID userId;
//
//
//    @BeforeAll
//    public void setup() throws ExecutionException, InterruptedException, TimeoutException {
//
//        webSocketStompClientJackson.setMessageConverter(new MappingJackson2MessageConverter());
//        webSocketStompClientString.setMessageConverter(new StringMessageConverter());
//
//        sessionJackson = webSocketStompClientJackson
//                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
//                .get(5, TimeUnit.SECONDS);
//
//        sessionString = webSocketStompClientString
//                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
//                .get(5, TimeUnit.SECONDS);
//
//        senderId = UUID.randomUUID();
//        senderUsername = "stomper";
//        environmentId = UUID.randomUUID();
//        timestamp = new Date();
//        text = "ChatWSControllerStompTest test message";
//        chatMessageDTO = new ChatMessageDTO();
//
//        chatMessageDTO.setSenderId(senderId);
//        chatMessageDTO.setSenderUsername(senderUsername);
//        chatMessageDTO.setEnvironmentId(environmentId);
//        chatMessageDTO.setGroupType(GroupType.BIDIRECTIONAL);
//        chatMessageDTO.setTimestamp(timestamp);
//        chatMessageDTO.setText(text);
//
//        toId = UUID.randomUUID();
//
//        lobbyId = UUID.randomUUID();
//        userId = UUID.randomUUID();
//    }
//
//    @Test
//    public void verify_storeAndRedirectMessage_ChatMessageDTOIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        given(chatService.storeAndConvert(Mockito.any())).willReturn(chatMessageDTO);
//
//        BlockingQueue<ChatMessageDTO> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionJackson.subscribe("/messages/outgoing/" + environmentId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return ChatMessageDTO.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((ChatMessageDTO) payload);
//            }
//        });
//
//        sessionJackson.send("/app/messages/incoming", chatMessageDTO);
//
//        assertEquals(chatMessageDTO, blockingQueue.poll(1, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void verify_sendGameInitializationData_GameInitializationDTOIsReceived() throws InterruptedException {
//
//        UUID lobbyId = UUID.randomUUID();
//        GameInitializationDTO gameInitializationDTO =
//                new GameInitializationDTO(
//                        UUID.randomUUID(),
//                        UUID.randomUUID(),
//                        UUID.randomUUID(),
//                        UUID.randomUUID(),
//                        UUID.randomUUID());
//
//        BlockingQueue<GameInitializationDTO> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionJackson.subscribe("/lobbies/" + lobbyId + "/gameInitialization", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return GameInitializationDTO.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((GameInitializationDTO) payload);
//            }
//        });
//
//        sessionJackson.send("/app/lobbies/" + lobbyId + "/initialSynchronization", gameInitializationDTO);
//
//        assertEquals(gameInitializationDTO, blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_notifyToFetchNewGameState_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        UUID gameId = UUID.randomUUID();
//
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionString.subscribe("/games/" + gameId + "/fetch", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/games/" + gameId + "/fetch", "DoesNotMatterWhatWeSendHere.ItsJustForNotifying");
//
//        assertNull(blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_sendShovingInfo_shoveToPartner_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        String msg = "ThisWillEitherBe\"choose\"OrOneOfTheIngameModesSuchAs\"SLALOM\"ButWillBeHandledInFrontend";
//
//        UUID gameId = UUID.randomUUID();
//        UUID partnerId = UUID.randomUUID();
//
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionString.subscribe("/games/" + gameId + "/shove/receive/" + partnerId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/games/" + gameId + "/shove/notify/" + partnerId, msg);
//
//        assertEquals(msg, blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_notifyFetchFriendRequests_messageReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        String msg = "new-request";
//
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionString.subscribe("/friend_requests/notify/" + toId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/friend_requests/" + toId, msg);
//
//        assertEquals(msg, blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_notifyToFetchFriendsAndUsers_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        String payloadId = UUID.randomUUID().toString();
//
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//
//        sessionString.subscribe("/friends/notify_remove/" + toId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/friends/notify_remove/" + toId, payloadId);
//
//        assertEquals(payloadId, blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_notifyAboutUsersInLobbyUpdate_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionString.subscribe("/lobbies/" + lobbyId + "/fetch", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/lobbies/" + lobbyId + "/fetch", "DoesNotMatterWhatWeSendHere");
//
//        assertEquals("fetch", blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_notifyAboutLobbyShutdown_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//
//        sessionString.subscribe("/lobbies/" + lobbyId + "/shutdown", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/lobbies/" + lobbyId + "/notifyShutdown", "DoesNotMatterWhatWeSendHere");
//
//        assertEquals("shutdown", blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_kickPlayerOutOfLobby_messageIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionString.subscribe("/lobbies/" + lobbyId + "/kicked/" + userId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((String) payload);
//            }
//        });
//
//        sessionString.send("/app/lobbies/" + lobbyId + "/kicked/" + userId, "DoesNotMatterWhatWeSendHere");
//
//        assertEquals("kicked", blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @Test
//    public void verify_sendLobbyInvitationToUser_LobbyJoinDTOIsReceived() throws ExecutionException, InterruptedException, TimeoutException {
//        LobbyJoinDTO lobbyJoinDTO = new LobbyJoinDTO();
//        lobbyJoinDTO.setLobbyId(lobbyId);
//        lobbyJoinDTO.setLobbyCreator("creatinski");
//
//        BlockingQueue<LobbyJoinDTO> blockingQueue = new ArrayBlockingQueue<>(1);
//
//        sessionJackson.subscribe("/lobbies/invite/" + userId, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return LobbyJoinDTO.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.add((LobbyJoinDTO) payload);
//            }
//        });
//
//        sessionJackson.send("/app/lobbies/invite/" + userId, lobbyJoinDTO);
//
//        assertEquals(lobbyJoinDTO, blockingQueue.poll(1, TimeUnit.SECONDS));
//
//    }
//
//    @AfterAll
//    public void after() {
//        sessionString.disconnect();
//        sessionJackson.disconnect();
//    }
//}
