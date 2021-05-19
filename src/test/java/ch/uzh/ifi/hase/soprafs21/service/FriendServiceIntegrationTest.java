package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class FriendServiceIntegrationTest {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;


    @Qualifier("friendRequestRepository")
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    private RegisteredUser willi;
    private RegisteredUser louise;
    private RegisteredUser rebekka;

        @BeforeEach
        public void createFriends(){
            friendRequestRepository.deleteAll();
            userRepository.deleteAll();


            willi = new RegisteredUser();
            willi.setPassword("verySafe");
            willi.setUsername("Willi");
            userService.createRegisteredUser(willi);

            //willis friends

            louise = new RegisteredUser();
            louise.setPassword("superSafe");
            louise.setUsername("Louise");
            userService.createRegisteredUser(louise);

            rebekka = new RegisteredUser();
            rebekka.setPassword("soSafe");
            rebekka.setUsername("Rebekka");
            userService.createRegisteredUser(rebekka);

            List<User> friendListFrom = new ArrayList<>();
            List<User> friendListTo = new ArrayList<>();
            friendListFrom.add(rebekka);
            friendListTo.add(louise);

            // make mutual friends
            willi.setFriends(friendListTo);
            willi.setfriendOf(friendListFrom);

            userRepository.saveAndFlush(rebekka);
            userRepository.saveAndFlush(willi);
            userRepository.saveAndFlush(louise);
        }

    @Test
    public void getFriendsfromUser_success(){
        //setup
        assertNotNull(userRepository.findById(willi.getId()));

        //when
        List<User> willisFriends = friendService.getFriends(willi);

        // then
        assert(!rebekka.equals(louise));
        assert(willisFriends.remove(rebekka));
        assert(willisFriends.remove(louise));
        assert(!willisFriends.contains(rebekka));
        assert(!willisFriends.contains(louise));
    }

    @Test
    public void getFriendsfromUser_noFriends(){
        //setup
        assertNotNull(userRepository.findById(louise.getId()));

        // when
        List<User> louisesFriends = friendService.getFriends(louise);

        // then
        assertEquals(0, louisesFriends.size());
    }

    }
