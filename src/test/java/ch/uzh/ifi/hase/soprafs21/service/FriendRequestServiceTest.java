package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FriendRequestServiceTest {
    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private RegisteredUserRepository userRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

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

        //willis friends

        louise = new RegisteredUser();
        louise.setPassword("verySafe");
        louise.setUsername("Louise");

        rebekka = new RegisteredUser();
        rebekka.setPassword("verySafe");
        rebekka.setUsername("Rebekka");


        FriendRequest request = new FriendRequest();
        request.setFromUser(willi);
        request.setToUser(rebekka);

    }

    @Test
    public void sendFriendRequest_Success(){
        //setup
        assertNotNull(userRepository.findById(willi.getId()));

        // when
        FriendRequest willirequesttoRebekka = friendRequestService.sendFriendRequest(willi, rebekka);

        // then
        assertEquals( willi, willirequesttoRebekka.getFromUser());
        assertEquals(rebekka, willirequesttoRebekka.getToUser());
    }


}
