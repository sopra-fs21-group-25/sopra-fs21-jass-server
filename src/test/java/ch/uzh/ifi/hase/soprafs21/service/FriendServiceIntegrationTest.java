package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.IngameModeMultiplicatorObject;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class FriendServiceIntegrationTest {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    private RegisteredUser willi;
    private RegisteredUser louise;
    private RegisteredUser rebekka;

        @BeforeEach
        public void createFriends(){
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

//            List<User> friendList = new ArrayList<>();
//            friendList.add(rebekka);
//            friendList.add(louise);
//
//            List<User> friendList2 = new ArrayList<>();
//            friendList.add(rebekka);
//            friendList.add(willi);
//
//            List<User> friendList3 = new ArrayList<>();
//            friendList.add(willi);
//            friendList.add(louise);
//
//            willi.setFriends(friendList);
//            louise.setFriends(friendList2);
//            rebekka.setFriends(friendList3);

            userRepository.saveAndFlush(rebekka);
            userRepository.saveAndFlush(willi);
            userRepository.saveAndFlush(louise);
        }

//    @Test
//    public void getFriendsfromUser_success(){
//        //setup
//        assertNotNull(userRepository.findById(willi.getId()));
//
//
//        //when
//        List<User> willisFriends = friendService.getFriends(willi);
//
//        // then
//        assertEquals( rebekka, willisFriends.get(0));
//        assertEquals( louise, willisFriends.get(1));
//        assertEquals(2, willisFriends.size());
//    }

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
