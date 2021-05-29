package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.game.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class GroupRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;


    private Group group;
    private RegisteredUser fridolin;
    private RegisteredUser silvia;
    private RegisteredUser annegret;
    private RegisteredUser timon;

    List<User> users;
    HashSet<User> userHashSet;

    private Card[] listofCard;
    private Card card0;
    private Card card1;
    private Card card2;
    private Card card3;
    private List<Card> listCard;
    private List<Group> listGroup;

    private SchieberGameSession schieberGameSession;
    private Message message;
    private Message message0;
    private Message message1;
    private List<Message> messageList;

    private Group newGroup;
    private Group twoPeopleGroup;

    Lobby lobby;
    @BeforeEach
    public void setupGroup() {

        // set up users
        newGroup = new Group(GroupType.COLLECTIVE);
        listGroup = new ArrayList<>();
        listGroup.add(newGroup);
        entityManager.persistAndFlush(newGroup);

        fridolin = new RegisteredUser();
        fridolin.setUsername("Fridolin");
        fridolin.setPassword("password2");
        fridolin.setStatus(UserStatus.ONLINE);
        fridolin.setGroups(listGroup);


        //fridolin.setId(UUID.randomUUID());
        entityManager.persistAndFlush(fridolin);

        silvia = new RegisteredUser();
        silvia.setUsername("Silvia");
        silvia.setPassword("password3");
        silvia.setStatus(UserStatus.ONLINE);
        silvia.setGroups(listGroup);
        //silvia.setId(UUID.randomUUID());
        entityManager.persistAndFlush(silvia);

        annegret = new RegisteredUser();
        annegret.setUsername("Annegret");
        annegret.setPassword("password4");
        annegret.setStatus(UserStatus.ONLINE);
        annegret.setGroups(listGroup);
       // annegret.setId(UUID.randomUUID());
        entityManager.persistAndFlush(annegret);

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("TimonAndPumba");
        //timon.setId(UUID.randomUUID());
        timon.setStatus(UserStatus.ONLINE);
        timon.setGroups(listGroup);
        entityManager.persistAndFlush(timon);

        users = new ArrayList<>();
        users.add(timon);
        users.add(fridolin);
        users.add(silvia);
        users.add(annegret);


        userHashSet = new HashSet<>();
        userHashSet.add(timon);
        userHashSet.add(fridolin);
        userHashSet.add(silvia);
        userHashSet.add(annegret);

        // set up lobby

        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        lobby = new Lobby();
        lobby.setCreatorUsername(timon.getUsername());
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
        lobby.setGroup(newGroup);
        entityManager.persistAndFlush(lobby);

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
        schieberGameSession.setUser0(timon);
        schieberGameSession.setUser1(silvia);
        schieberGameSession.setUser2(fridolin);
        schieberGameSession.setUser3(annegret);
        schieberGameSession.setWeisAsk("true");
        schieberGameSession.setTrickToPlay(1);
        schieberGameSession.setCrossWeisAllowed(true);
        schieberGameSession.setWeisAllowed(Boolean.TRUE);
        schieberGameSession.setStartingCardRank(Rank.NINE);
        schieberGameSession.setStartingCardSuit(Suit.ROSE);
        schieberGameSession.setInitiallyStartingPlayer();
        schieberGameSession.setIdOfRoundStartingPlayer(fridolin.getId());
        schieberGameSession.setPointsTeam1_3(200);
        schieberGameSession.setPointsTeam0_2(200);
        schieberGameSession.setPlayer0startsTrick(true);
        schieberGameSession.setHasTrickStarted(false);
        schieberGameSession.setGroup(newGroup);
        entityManager.persistAndFlush(schieberGameSession);

        //set up messages

        message = new Message();
        message.setSender(fridolin);
        message.setText("Hello");
        Date date = new Date();
        message.setTimestamp(new Timestamp(date.getTime()));
        message.setGroup(newGroup);
        entityManager.persistAndFlush(message);

    }


    @Test
    void findByGroupTypeAndUsersWithIds_test() {
        Group foundGroup = groupRepository.findByGroupTypeAndUsersWithIds(GroupType.COLLECTIVE, fridolin.getId(), annegret.getId() );
        assertEquals(foundGroup.getId(), newGroup.getId());
        assertEquals(foundGroup.getGroupType(), newGroup.getGroupType());
    }


    @Test
    void findByLobbyIdOrGameId_Test() {
        Group foundGroup = groupRepository.retrieveGroupByEnvironmentIdAsLobbyIdOrGameId(lobby.getId());
        assertEquals(foundGroup.getId(), newGroup.getId());
        assertEquals(foundGroup.getUsers(), newGroup.getUsers());
    }

    @Test
    void findOrderedByTimestampByTimestampUserToUserMessagesSentBetweenUserAAndUserB_Test() {
        twoPeopleGroup = new Group(GroupType.BIDIRECTIONAL);
        listGroup = new ArrayList<>();
        listGroup.add(twoPeopleGroup);
        annegret.setGroups(listGroup);
        fridolin.setGroups(listGroup);

        List<User> twoPeopleList =new ArrayList<>();
        twoPeopleList.add(fridolin);
        twoPeopleList.add(annegret);
        twoPeopleGroup.setUsers(twoPeopleList);


        Message message = new Message();
        message.setSender(fridolin);
        message.setText("Hello Annegret");
        Date date = new Date();
        message.setTimestamp(new Timestamp(date.getTime()));

        Message message1 = new Message();
        message1.setSender(annegret);
        message1.setText("Hello Fridolin");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message.setGroup(twoPeopleGroup);
        message1.setGroup(twoPeopleGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message);
        messageList1.add(message1);
        twoPeopleGroup.setMessages(messageList1);

        entityManager.persistAndFlush(twoPeopleGroup);
        entityManager.persistAndFlush(message);
        entityManager.persistAndFlush(message1);


        List<Message> messagesFound = groupRepository.findOrderedByTimestampUserToUserMessagesSentBetweenUserAAndUserB(fridolin.getId(), annegret.getId());

        assertNotNull(messagesFound);
        assertEquals(messagesFound.get(0), message);
        assertEquals(messagesFound.get(1), message1);
        assertEquals(messagesFound.get(0).getText(), "Hello Annegret");
        assertEquals(messagesFound.get(1).getText(), "Hello Fridolin");

        entityManager.remove(twoPeopleGroup);
    }

    @Test
    void findOrderedByTimestampLobbyMessages_Test() {
        message0 = new Message();
        message0.setSender(fridolin);
        message0.setText("Hello Annegret");
        Date date = new Date();
        message0.setTimestamp(new Timestamp(date.getTime()));

        message1 = new Message();
        message1.setSender(annegret);
        message1.setText("Hello Fridolin");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message0.setGroup(newGroup);
        message1.setGroup(newGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message0);
        messageList1.add(message1);
        lobby.setGroup(newGroup);

        entityManager.persistAndFlush(lobby);
        entityManager.persistAndFlush(newGroup);
        entityManager.persistAndFlush(message0);
        entityManager.persistAndFlush(message1);

        List<Message> messagesFound = groupRepository.findOrderedByTimestampLobbyMessages(lobby.getId());

        assertEquals(messagesFound.get(0), message);
        assertEquals(messagesFound.get(1), message0);
        assertEquals(messagesFound.get(2), message1);

        entityManager.remove(message0);
        entityManager.remove(message1);
    }


    @Test
    void findOrderedByTimestampGameMessages_Test() {
        message0 = new Message();
        message0.setSender(timon);
        message0.setText("Hello Marie");
        Date date = new Date();
        message0.setTimestamp(new Timestamp(date.getTime()));

        message1 = new Message();
        message1.setSender(silvia);
        message1.setText("Ich bin nicht Marie, ich bin Silvia");
        Date date1 = new Date();
        message1.setTimestamp(new Timestamp(date1.getTime()));

        message0.setGroup(newGroup);
        message1.setGroup(newGroup);
        List<Message> messageList1 = new ArrayList<>();
        messageList1.add(message0);
        messageList1.add(message1);
        schieberGameSession.setGroup(newGroup);

        entityManager.persistAndFlush(schieberGameSession);
        entityManager.persistAndFlush(newGroup);
        entityManager.persistAndFlush(message0);
        entityManager.persistAndFlush(message1);

        List<Message> messagesFound = groupRepository.findOrderedByTimestampGameMessages(schieberGameSession.getId());

        assertEquals(messagesFound.get(0), message);
        assertEquals(messagesFound.get(1).getText(), message0.getText());
        assertEquals(messagesFound.get(2).getSender(), message1.getSender());
        assertEquals(messagesFound.size(), 3);
        assertEquals(messagesFound.get(0).getGroup(), message.getGroup());

        entityManager.remove(message0);
        entityManager.remove(message1);
    }

    @AfterEach
    public void cleanUpEach(){
        entityManager.remove(lobby);
        entityManager.remove(schieberGameSession);
        entityManager.remove(fridolin);
        entityManager.remove(annegret);
        entityManager.remove(silvia);
        entityManager.remove(timon);
        entityManager.remove(message);
        entityManager.remove(newGroup);
    }

}