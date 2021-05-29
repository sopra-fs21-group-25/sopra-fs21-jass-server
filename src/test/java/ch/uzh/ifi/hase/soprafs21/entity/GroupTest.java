package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.game.*;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;
    private RegisteredUser fridolin;
    private RegisteredUser silvia;
    private RegisteredUser annegret;
    private RegisteredUser timon;

    List<User> users;

    private Card[] listofCard;
    private Card card0;
    private Card card1;
    private Card card2;
    private Card card3;
    private List<Card> listCard;
    private List<Group> listGroup;
    HashSet<User> userHashSet;



    private SchieberGameSession schieberGameSession;

    @Test
    void evaluateEnvironmentId_Receiver_test() {
        fridolin = new RegisteredUser();
        fridolin.setUsername("Fridolin");
        fridolin.setPassword("password2");
        fridolin.setStatus(UserStatus.ONLINE);
        fridolin.setId(UUID.randomUUID());


        silvia = new RegisteredUser();
        silvia.setUsername("Silvia");
        silvia.setPassword("password3");
        silvia.setStatus(UserStatus.ONLINE);
        silvia.setId(UUID.randomUUID());



        users = new ArrayList<>();
        users.add(fridolin);
        users.add(silvia);

        group = new Group();
        group.setGroupType(GroupType.BIDIRECTIONAL);
        group.setUsers(users);


        Message message = new Message();
        message.setSender(fridolin);
        message.setText("Hello");
        Date date = new Date();
        message.setTimestamp(new Timestamp(date.getTime()));
        message.setGroup(group);
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        group.setMessages(messages);

       UUID returnedId =  group.evaluateEnvironmentId(fridolin.getId());
       assertEquals(silvia.getId(), returnedId);
    }

    @Test
    void evaluateEnvironmentId_Game_test() {
        fridolin = new RegisteredUser();
        fridolin.setUsername("Fridolin");
        fridolin.setPassword("password2");
        fridolin.setStatus(UserStatus.ONLINE);
        fridolin.setId(UUID.randomUUID());


        silvia = new RegisteredUser();
        silvia.setUsername("Silvia");
        silvia.setPassword("password3");
        silvia.setStatus(UserStatus.ONLINE);
        silvia.setId(UUID.randomUUID());

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("password2");
        timon.setStatus(UserStatus.ONLINE);
        timon.setId(UUID.randomUUID());


        annegret = new RegisteredUser();
        annegret.setUsername("Annegret");
        annegret.setPassword("password3");
        annegret.setStatus(UserStatus.ONLINE);
        annegret.setId(UUID.randomUUID());




        users = new ArrayList<>();
        users.add(fridolin);
        users.add(silvia);

        group = new Group();
        group.setGroupType(GroupType.COLLECTIVE);
        group.setUsers(users);


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
        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);



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

        Message message = new Message();
        message.setSender(fridolin);
        message.setText("Hello");
        Date date = new Date();
        message.setTimestamp(new Timestamp(date.getTime()));
        message.setGroup(group);
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        group.setMessages(messages);
        group.setGame(schieberGameSession);

        UUID returnedId =  group.evaluateEnvironmentId(fridolin.getId());
        assertEquals(schieberGameSession.getId(), returnedId);
    }

    @Test
    void evaluateEnvironmentId_Lobby_Test() {
        fridolin = new RegisteredUser();
        fridolin.setUsername("Fridolin");
        fridolin.setPassword("password2");
        fridolin.setStatus(UserStatus.ONLINE);
        fridolin.setId(UUID.randomUUID());


        silvia = new RegisteredUser();
        silvia.setUsername("Silvia");
        silvia.setPassword("password3");
        silvia.setStatus(UserStatus.ONLINE);
        silvia.setId(UUID.randomUUID());

        timon = new RegisteredUser();
        timon.setUsername("Timon");
        timon.setPassword("password2");
        timon.setStatus(UserStatus.ONLINE);
        timon.setId(UUID.randomUUID());


        annegret = new RegisteredUser();
        annegret.setUsername("Annegret");
        annegret.setPassword("password3");
        annegret.setStatus(UserStatus.ONLINE);
        annegret.setId(UUID.randomUUID());




        users = new ArrayList<>();
        users.add(fridolin);
        users.add(silvia);

        group = new Group();
        group.setGroupType(GroupType.COLLECTIVE);
        group.setUsers(users);


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
        List<IngameModeMultiplicatorObject> ingameModeMultiplicators = new ArrayList<>();
        IngameModeMultiplicatorObject oneObject = new IngameModeMultiplicatorObject();
        oneObject.setIngameMode(IngameMode.ACORN);
        oneObject.setMultiplicator(20);
        ingameModeMultiplicators.add(oneObject);



        userHashSet = new HashSet<>();
        userHashSet.add(timon);
        userHashSet.add(fridolin);
        userHashSet.add(silvia);
        userHashSet.add(annegret);

        // set up lobby

        Lobby lobby = new Lobby();
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

        Message message = new Message();
        message.setSender(fridolin);
        message.setText("Hello");
        Date date = new Date();
        message.setTimestamp(new Timestamp(date.getTime()));
        message.setGroup(group);
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        group.setMessages(messages);
        group.setLobby(lobby);

        UUID returnedId =  group.evaluateEnvironmentId(fridolin.getId());
        assertEquals(lobby.getId(), returnedId);
    }
}