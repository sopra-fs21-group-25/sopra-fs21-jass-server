package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.controllerWS.ChatWSController;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@WebMvcTest(ChatWSController.class)
public class ChatWSControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private ChatService chatService;

    private UUID senderId;
    private String senderUsername;
    private UUID environmentId;
    private Date timestamp;
    private String text;
    private ChatMessageDTO chatMessageDTO;
    private List<ChatMessageDTO> chatMessageDTOList;

    @BeforeEach
    public void setup() {
        senderId = UUID.randomUUID();
        senderUsername = "rester";
        environmentId = UUID.randomUUID();
        timestamp = new Date();
        text = "ChatWSControllerRestTest test message";
        chatMessageDTO = new ChatMessageDTO();

        chatMessageDTO.setSenderId(senderId);
        chatMessageDTO.setSenderUsername(senderUsername);
        chatMessageDTO.setEnvironmentId(environmentId);
        chatMessageDTO.setTimestamp(timestamp);
        chatMessageDTO.setText(text);

        chatMessageDTOList = new ArrayList<>();
        chatMessageDTOList.add(chatMessageDTO);
    }

    @Test
    public void givenTwoUserIds_whenGetAllMessagesUserAAndUserB_thenReturnJsonArrayEncodingChatMessageDTOList() throws Exception {
        chatMessageDTO.setGroupType(GroupType.BIDIRECTIONAL);
        given(chatService.getAllMessagesBetweenUserAAndUserB(senderId, environmentId)).willReturn(chatMessageDTOList);

        MockHttpServletRequestBuilder getRequest = get(
                "/messages/bidirectional/" + senderId.toString() + "/" + environmentId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].senderId", is(senderId.toString())))
                .andExpect(jsonPath("$[0].senderUsername", is(senderUsername)))
                .andExpect(jsonPath("$[0].environmentId", is(environmentId.toString())))
                .andExpect(jsonPath("$[0].timestamp", is(parseDateToDefault(timestamp))))
                .andExpect(jsonPath("$[0].text", is(text)));
    }

    @Test
    public void givenLobbyId_whenGetAllMessagesSentInLobby_thenReturnJsonArrayEncodingChatMessageDTOList() throws Exception {
        chatMessageDTO.setGroupType(GroupType.COLLECTIVE);
        given(chatService.getAllMessagesInLobby(environmentId)).willReturn(chatMessageDTOList);

        MockHttpServletRequestBuilder getRequest = get("/messages/lobby/" + environmentId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].senderId", is(senderId.toString())))
                .andExpect(jsonPath("$[0].senderUsername", is(senderUsername)))
                .andExpect(jsonPath("$[0].environmentId", is(environmentId.toString())))
                .andExpect(jsonPath("$[0].timestamp", is(parseDateToDefault(timestamp))))
                .andExpect(jsonPath("$[0].text", is(text)));
    }

    @Test
    public void givenGameId_whenGetAllMessagesSentInGame_thenReturnJsonArrayEncodingChatMessageDTOList() throws Exception {
        chatMessageDTO.setGroupType(GroupType.COLLECTIVE);
        given(chatService.getAllMessagesInGame(environmentId)).willReturn(chatMessageDTOList);

        MockHttpServletRequestBuilder getRequest = get("/messages/game/" + environmentId.toString())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].senderId", is(senderId.toString())))
                .andExpect(jsonPath("$[0].senderUsername", is(senderUsername)))
                .andExpect(jsonPath("$[0].environmentId", is(environmentId.toString())))
                .andExpect(jsonPath("$[0].timestamp", is(parseDateToDefault(timestamp))))
                .andExpect(jsonPath("$[0].text", is(text)));
    }


    private static String parseDateToDefault(Date date) throws ParseException {
        return date.toInstant().toString().replaceAll("Z$", "+00:00");
    }
}
