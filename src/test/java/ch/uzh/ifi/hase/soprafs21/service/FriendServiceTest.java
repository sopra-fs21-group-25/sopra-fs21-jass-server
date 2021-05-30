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
    private RegisteredUser testUser; 

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
        louise.setId(UUID. randomUUID());
        userService.createRegisteredUser(louise);

        rebekka = new RegisteredUser();
        rebekka.setPassword("verySafe");
        rebekka.setUsername("Rebekka");
        userService.createRegisteredUser(rebekka);

        testUser = new RegisteredUser();
        testUser.setPassword("testUser");
        testUser.setUsername("testUser");
        testUser.setId(UUID. randomUUID());
        testUser.setFriends(new ArrayList<User>());
        userService.createRegisteredUser(testUser);

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

    @Test
    public void addFriend_success(){
        List<User> testUserFriends = new ArrayList<>();
        Mockito.doAnswer(invocation -> {
            User arg0 = invocation.getArgument(0);
            User arg1 = invocation.getArgument(1);
            testUserFriends.add(arg1);
            assertEquals(testUserFriends.get(0), louise);
            return null;
        }).when(friendService).addFriends(Mockito.any(User.class), Mockito.any(User.class));
        friendService.addFriends(testUser, louise);
    }

    @Test
    public void removeFriend_success(){
        List<User> testUserFriends = new ArrayList<>();
        testUserFriends.add(louise);
        testUser.setFriends(testUserFriends);
        Mockito.doAnswer(invocation -> {
            User arg0 = invocation.getArgument(0);
            User arg1 = invocation.getArgument(1);
            testUserFriends.remove(arg1);
            assertEquals(testUserFriends.size(), 0);
            return null;
        }).when(friendService).removeFriend(Mockito.any(User.class), Mockito.any(User.class));
        friendService.removeFriend(testUser, louise);
    }
}
