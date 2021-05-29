package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceIntegrationTest {

    @Qualifier("messageRepository")

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    @Autowired
    ChatService chatService;


    private RegisteredUser claude;
    private RegisteredUser hanna;
    private RegisteredUser berthold;
    private RegisteredUser pia;
    private Set<User> users;
    private Set<User> only2users;
    private Group bidirectGroup;
    private Group collectGroup;
    private Message message;
    private Message message0;
    private Message message1;
    private List<Message> messageList;
    private Lobby lobby;
    private Set<User> userHashSet;
    private Card[] listofCard;
    private Card card0;
    private Card card1;
    private Card card2;
    private Card card3;
    private List<Card> listCard;
    private List<Group> listGroup;
    private Group twoPeopleGroup;

    private SchieberGameSession schieberGameSession;

    @BeforeEach
    public void setupGroup(){
        MockitoAnnotations.openMocks(this);

        //set up test users
        claude = new RegisteredUser();
        claude.setUsername("Claude");
        claude.setPassword("password1");
        claude.setStatus(UserStatus.OFFLINE);
        userRepository.save(claude);
        userRepository.flush();

        hanna= new RegisteredUser();
        hanna.setUsername("Hanna");
        hanna.setPassword("password2");
        hanna.setStatus(UserStatus.ONLINE);
        userRepository.save(hanna);
        userRepository.flush();


        berthold= new RegisteredUser();
        berthold.setUsername("Berthold");
        berthold.setPassword("password3");
        berthold.setStatus(UserStatus.ONLINE);
        userRepository.save(berthold);
        userRepository.flush();

        pia = new RegisteredUser();
        pia.setUsername("Pia");
        pia.setPassword("password4");
        pia.setStatus(UserStatus.ONLINE);
        userRepository.save(pia);
        userRepository.flush();

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
        groupRepository.saveAndFlush(bidirectGroup);

        collectGroup = new Group(GroupType.COLLECTIVE);
        collectGroup.setUsers(new ArrayList<>(users));
        groupRepository.saveAndFlush(collectGroup);

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
        messageRepository.saveAndFlush(message);



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
        lobbyRepository.saveAndFlush(lobby);


        //set up game

        card0 = new Card();
        card0.setRank(Rank.KING);
        card0.setSuit(Suit.ACORN);
        card0.setIsTrumpf(true);


        card1 = new Card();
        card1.setRank(Rank.KING);
        card1.setSuit(Suit.BELL);
        card1.setIsTrumpf(false);

        card2 = new Card();
        card2.setRank(Rank.NINE);
        card2.setSuit(Suit.BELL);
        card2.setIsTrumpf(false);

        card3 = new Card();
        card3.setRank(Rank.UNDER);
        card3.setSuit(Suit.BELL);
        card3.setIsTrumpf(false);

        listofCard = new Card[4];
        listofCard[0] = card1;
        listofCard[1] = card2;
        listofCard[2] = card3;
        listofCard[3] = card0;

        listCard = new ArrayList<>();
        listCard.add(card1);
        listCard.add(card2);
        listCard.add(card3);
        listCard.add(card0);


        // List of Ingame Multiplicator Objects
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);

        //List of Ingame Multiplicator Objects
        IngameModeMultiplicatorObject[] myObjectforDTO = new IngameModeMultiplicatorObject[1];
        myObjectforDTO[0] = new IngameModeMultiplicatorObject(IngameMode.ACORN, 1000);

        //setup of the schhieber Game Session
        schieberGameSession = new SchieberGameSession();
        schieberGameSession.setIngameModes(ingameModeMultiplicators);
        schieberGameSession.setCardPlayedByPlayer0(card1);
        schieberGameSession.setCardPlayedByPlayer1(card2);
        schieberGameSession.setCardPlayedByPlayer2(card3);
        schieberGameSession.setCardPlayedByPlayer3(card0);
        schieberGameSession.setCardsHeldByPlayer0(listCard);
        schieberGameSession.setCardsHeldByPlayer1(listCard);
        schieberGameSession.setCardsHeldByPlayer2(listCard);
        schieberGameSession.setCardsHeldByPlayer3(listCard);
        schieberGameSession.setCurrentIngameMode(IngameMode.ACORN);
        schieberGameSession.setPointsToWin(2000);
        schieberGameSession.setUser0(claude);
        schieberGameSession.setUser1(hanna);
        schieberGameSession.setUser2(berthold);
        schieberGameSession.setUser3(pia);
        schieberGameSession.setWeisAsk("true");
        schieberGameSession.setTrickToPlay(1);
        schieberGameSession.setCrossWeisAllowed(true);
        schieberGameSession.setWeisAllowed(Boolean.TRUE);
        schieberGameSession.setStartingCardRank(Rank.NINE);
        schieberGameSession.setStartingCardSuit(Suit.ROSE);
        schieberGameSession.setInitiallyStartingPlayer();
        schieberGameSession.setIdOfRoundStartingPlayer(claude.getId());
        schieberGameSession.setPointsTeam1_3(200);
        schieberGameSession.setPointsTeam0_2(200);
        schieberGameSession.setPlayer0startsTrick(true);
        schieberGameSession.setHasTrickStarted(false);
        schieberGameSession.setGroup(collectGroup);
        gameRepository.saveAndFlush(schieberGameSession);
    }

    @Test
    void storeAndConvert_Test() {
        ChatMessageDTO chatMessageDTO= new ChatMessageDTO();
        chatMessageDTO.setSenderId(pia.getId());
        Date date2 = new Date();
        chatMessageDTO.setTimestamp(new Timestamp(date2.getTime()));
        chatMessageDTO.setText("Hi Guys");
        chatMessageDTO.setSenderUsername(pia.getUsername());
        chatMessageDTO.setGroupType(GroupType.COLLECTIVE);
        chatMessageDTO.setEnvironmentId(lobby.getId());

        ChatMessageDTO returnedDTO = chatService.storeAndConvert(chatMessageDTO);
        assertEquals(returnedDTO.getText(), "Hi Guys");
        assertEquals(returnedDTO.getSenderId(), pia.getId());

    }

    @Test
    void getAllMessagesBetweenUserAAndUserB_Test() {

        twoPeopleGroup = new Group(GroupType.BIDIRECTIONAL);
        listGroup = new ArrayList<>();
        listGroup.add(twoPeopleGroup);
        claude.setGroups(listGroup);
        hanna.setGroups(listGroup);

        List<User> twoPeopleList =new ArrayList<>();
        twoPeopleList.add(claude);
        twoPeopleList.add(hanna);
        twoPeopleGroup.setUsers(twoPeopleList);


        message0 = new Message();
        message0.setSender(claude);
        message0.setText("Boa das Wetter ist so schlecht");
        Date date = new Date();
        message0.setTimestamp(new Timestamp(date.getTime()));

        message1 = new Message();
        message1.setSender(hanna);
        message1.setText("Na dann macht Jassen noch viel mehr Spass");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message0.setGroup(twoPeopleGroup);
        message1.setGroup(twoPeopleGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message0);
        messageList1.add(message1);
        twoPeopleGroup.setMessages(messageList1);

        groupRepository.saveAndFlush(twoPeopleGroup);
        messageRepository.saveAndFlush(message0);
        messageRepository.saveAndFlush(message1);
        userRepository.saveAndFlush(hanna);
        userRepository.saveAndFlush(claude);

        List<ChatMessageDTO> returnedChats = chatService.getAllMessagesBetweenUserAAndUserB(claude.getId(), hanna.getId());
        assertEquals(returnedChats.toArray().length, 2);
        assertEquals(returnedChats.get(0).getText(), "Boa das Wetter ist so schlecht");
        assertEquals(returnedChats.get(1).getText(), "Na dann macht Jassen noch viel mehr Spass");

        messageRepository.delete(message1);
        messageRepository.delete(message0);
    }

    @Test
    void getAllMessagesInLobby_Test() {
        message0 = new Message();
        message0.setSender(claude);
        message0.setText("Hallo Pia, was läuft?");
        Date date = new Date();
        message0.setTimestamp(new Timestamp(date.getTime()));

        message1 = new Message();
        message1.setSender(pia);
        message1.setText("Hello Claude, ich liebe Jassen so sehr mit dir <3");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message0.setGroup(collectGroup);
        message1.setGroup(collectGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message0);
        messageList1.add(message1);
        collectGroup.setMessages(messageList1);
        lobby.setGroup(collectGroup);

        lobbyRepository.saveAndFlush(lobby);
        groupRepository.saveAndFlush(collectGroup);
        messageRepository.saveAndFlush(message0);
        messageRepository.saveAndFlush(message1);
        userRepository.saveAndFlush(pia);
        userRepository.saveAndFlush(claude);

        List<ChatMessageDTO> messagesFound = chatService.getAllMessagesInLobby(lobby.getId());

        assertEquals(messagesFound.toArray().length, 2);
        assertEquals(messagesFound.get(0).getText(), "Hallo Pia, was läuft?");
        assertEquals(messagesFound.get(1).getText(), "Hello Claude, ich liebe Jassen so sehr mit dir <3");



        messageRepository.delete(message0);
        messageRepository.delete(message1);
    }

    @Test
    void getAllMessagesInGame_Test() {
        message0 = new Message();
        message0.setSender(claude);
        message0.setText("Hey das war aber ein fieser Stich");
        Date date = new Date();
        message0.setTimestamp(new Timestamp(date.getTime()));

        message1 = new Message();
        message1.setSender(pia);
        message1.setText("Reg dich ab Claude, du musst auch verlieren lernen");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message0.setGroup(collectGroup);
        message1.setGroup(collectGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message0);
        messageList1.add(message1);
        collectGroup.setMessages(messageList1);
        schieberGameSession.setGroup(collectGroup);

        gameRepository.saveAndFlush(schieberGameSession);
        groupRepository.saveAndFlush(collectGroup);
        messageRepository.saveAndFlush(message0);
        messageRepository.saveAndFlush(message1);
        userRepository.saveAndFlush(pia);
        userRepository.saveAndFlush(claude);

        List<ChatMessageDTO> messagesFound = chatService.getAllMessagesInGame(schieberGameSession.getId());

        assertEquals(messagesFound.toArray().length, 2);
        assertEquals(messagesFound.get(0).getText(), "Hey das war aber ein fieser Stich");
        assertEquals(messagesFound.get(1).getText(), "Reg dich ab Claude, du musst auch verlieren lernen");



        messageRepository.delete(message0);
        messageRepository.delete(message1);
    }

    @AfterEach
    public void cleanUp(){
        lobbyRepository.delete(lobby);
        gameRepository.delete(schieberGameSession);
        groupRepository.delete(bidirectGroup);
        groupRepository.delete(collectGroup);
        messageRepository.delete(message);
        userRepository.delete(hanna);
        userRepository.delete(pia);
        userRepository.delete(berthold);
        userRepository.delete(claude);
}


}