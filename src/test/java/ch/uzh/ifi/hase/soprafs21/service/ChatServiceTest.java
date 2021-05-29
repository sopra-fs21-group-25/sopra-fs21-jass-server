package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;





class ChatServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatService chatService;


    private RegisteredUser claude;
    private RegisteredUser hanna;
    private RegisteredUser berthold;
    private RegisteredUser pia;
    private Set<User> users;
    private Set<User> only2users;
    private Group bidirectGroup;
    private Group collectGroup;
    private Message message;
    private List<Message> messageList;
    private Lobby lobby;
    private Set<User> userHashSet;

    @BeforeEach
    public void setupGroup(){
        MockitoAnnotations.openMocks(this);

        //set up test users
        claude = new RegisteredUser();
        claude.setUsername("Claude");
        claude.setPassword("password1");
        claude.setStatus(UserStatus.OFFLINE);
        claude.setId(UUID.randomUUID());

        hanna= new RegisteredUser();
        hanna.setUsername("Hanna");
        hanna.setPassword("password2");
        hanna.setStatus(UserStatus.ONLINE);
        hanna.setId(UUID.randomUUID());

        berthold= new RegisteredUser();
        berthold.setUsername("Berthold");
        berthold.setPassword("password3");
        berthold.setStatus(UserStatus.ONLINE);
        berthold.setId(UUID.randomUUID());

        pia = new RegisteredUser();
        pia.setUsername("Pia");
        pia.setPassword("password4");
        pia.setStatus(UserStatus.ONLINE);
        pia.setId(UUID.randomUUID());
        pia.setId(UUID.randomUUID());

        //add users to two sets
        users = new HashSet<>();
        users.add(pia);
        users.add(berthold);
        users.add(hanna);
        users.add(claude);


        only2users = new HashSet<>();
        users.add(pia);
        users.add(berthold);


        //group
        bidirectGroup = new Group(GroupType.BIDIRECTIONAL);
        bidirectGroup.setUsers(new ArrayList<>(only2users));

        collectGroup = new Group(GroupType.COLLECTIVE);
        collectGroup.setUsers(new ArrayList<>(users));

        //messages
        message = new Message();
        message.setGroup(bidirectGroup);
        message.setSender(berthold);
        message.setText("Hey Pia willst du jassen?");
        Date date1 = new Date();
        message.setTimestamp(new Timestamp(date1.getTime()));
        messageList = new ArrayList<>();
        messageList.add(message);

        bidirectGroup.setMessages(messageList);

        //lobby

        userHashSet = new HashSet<>();
        userHashSet.add(pia);
        userHashSet.add(berthold);
        userHashSet.add(hanna);
        userHashSet.add(claude);

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        lobby = new Lobby();
        lobby.setCreatorUsername(pia.getUsername());
        lobby.setUsersInLobby(userHashSet);
        lobby.setLobbyType("public");
        lobby.setMode(GameMode.SCHIEBER);
        lobby.setStartingCardSuit(Suit.ROSE);
        lobby.setStartingCardRank(Rank.TEN);
        lobby.setPointsToWin(2500);
        lobby.setWeisAllowed(false);
        lobby.setCrossWeisAllowed(false);
        lobby.setWeisAsk("never");
        lobby.setIngameModes(ingameModeMultiplicators);
        lobby.setGroup(collectGroup);
    }

    @Test
    void evaluateGroupAssignment_Bidirectional_Test() {
        Mockito.when(groupRepository.findByGroupTypeAndUsersWithIds(GroupType.BIDIRECTIONAL, berthold.getId(), berthold.getId())).thenReturn(bidirectGroup);
        Group foundGroup = chatService.evaluateGroupAssignment(berthold.getId(), berthold.getId(), GroupType.BIDIRECTIONAL);
        assertEquals(foundGroup, bidirectGroup);
        assertEquals(foundGroup.getUsers(), bidirectGroup.getUsers());
        assertEquals(foundGroup.getMessages(), bidirectGroup.getMessages());
    }

    @Test
    void evaluateGroupAssignment_Collective_Test() {
        Mockito.when(groupRepository.findByLobbyIdOrGameId(lobby.getId())).thenReturn(collectGroup);
        Group foundGroup = chatService.evaluateGroupAssignment(berthold.getId(), lobby.getId(), GroupType.COLLECTIVE);
        assertEquals(foundGroup, collectGroup);
        assertEquals(foundGroup.getId(), collectGroup.getId());
        assertEquals(foundGroup.getLobby(), collectGroup.getLobby());
    }

}