package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendRequestServiceTest {
    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @InjectMocks
    private UserService userService;


    private RegisteredUser willi;
    private RegisteredUser louise;
    private RegisteredUser rebekka;
    private FriendRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        willi = new RegisteredUser();
        willi.setPassword("verySafe");
        willi.setUsername("Willi");
        willi.setId(new UUID(5L, 4L));

        //willis friends

        louise = new RegisteredUser();
        louise.setPassword("verySafe");
        louise.setUsername("Louise");

        rebekka = new RegisteredUser();
        rebekka.setPassword("verySafe");
        rebekka.setUsername("Rebekka");
        rebekka.setId(new UUID(1L, 2L));


        request = new FriendRequest();
        request.setFromUser(willi);
        request.setToUser(rebekka);

        Mockito.when(friendRequestRepository.save(Mockito.any())).thenReturn(request);
    }

    @Test
    public void sendFriendRequest_Success(){
        //setup
        assertNotNull(userRepository.findById(willi.getId()));
        Mockito.when(friendService.getFriends(Mockito.any())).thenReturn(new ArrayList<>());


        // when
        FriendRequest williRequestToRebekka = friendRequestService.createAndStoreNewFriendRequest(request);

        // then
        assertEquals(willi, williRequestToRebekka.getFromUser());
        assertEquals(rebekka, williRequestToRebekka.getToUser());
    }

    @AfterEach
    public void cleanUpEach() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();

        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(friendRequestRepository.findAll().isEmpty());
    }


}
