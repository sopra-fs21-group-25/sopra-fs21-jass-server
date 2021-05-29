package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Avatar;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.AvatarRepository;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendServiceTest {
    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendService friendService;

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private UserService userService;

    private RegisteredUser willi;
    private RegisteredUser louise;
    private RegisteredUser rebekka;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        willi = new RegisteredUser();
        willi.setPassword("verySafe");
        willi.setUsername("Willi");
        Mockito.when(avatarRepository.saveAndFlush(Mockito.any())).thenReturn(new Avatar());
        userService.createRegisteredUser(willi);

        //willis friends

        louise = new RegisteredUser();
        louise.setPassword("verySafe");
        louise.setUsername("Louise");
        userService.createRegisteredUser(louise);

        rebekka = new RegisteredUser();
        rebekka.setPassword("verySafe");
        rebekka.setUsername("Rebekka");
        userService.createRegisteredUser(rebekka);

        List<User> friendList = new ArrayList<>();
        friendList.add(rebekka);
        friendList.add(louise);

        willi.setFriends(friendList);
        willi.setfriendOf(friendList);

        Mockito.when(friendService.getFriends(Mockito.any())).thenReturn(friendList);
    }

    @Test
    public void getFriendsfromUser_success(){
        //setup
        assertNotNull(userRepository.findById(willi.getId()));

        // when
       List<User> willisFriends = friendService.getFriends(willi);

        // then
        assertEquals( rebekka, willisFriends.get(0));
        assertEquals( louise, willisFriends.get(1));
        assertEquals(2, willisFriends.size());
    }

}
