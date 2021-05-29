package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Avatar;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.AvatarRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private UserService userService;

    private RegisteredUser testRegisteredUser;
    private GuestUser testGuestUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testRegisteredUser = new RegisteredUser();
        testRegisteredUser.setId(new UUID(1,1));
        testRegisteredUser.setUsername("testUsername");
        testRegisteredUser.setStatus(UserStatus.OFFLINE);

        testGuestUser = new GuestUser();
        testGuestUser.setStatus(UserStatus.ONLINE);
        testGuestUser.setUsername("Sharp Bat");
        Mockito.when(avatarRepository.saveAndFlush(Mockito.any())).thenReturn(new Avatar());
    }

    @Test
    public void createRegisteredUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testRegisteredUser);
        testRegisteredUser.setPassword("SuperSafePassword");

        RegisteredUser createdUser = userService.createRegisteredUser(testRegisteredUser);


        assertEquals(testRegisteredUser.getId(), createdUser.getId());
        assertEquals(testRegisteredUser.getUsername(), createdUser.getUsername());
        assertEquals(testRegisteredUser.getPassword(), createdUser.getPassword());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
        assertNotNull(createdUser.getToken());
    }

    @Test
    public void createRegisteredUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testRegisteredUser);
        userService.createRegisteredUser(testRegisteredUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testRegisteredUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createRegisteredUser((RegisteredUser) testRegisteredUser));
    }

    @Test
    public void createGuestUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testGuestUser);

        GuestUser createdUser = userService.createGuestUser();

        // then
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
        assertNotNull(createdUser.getUsername());
    }



}
