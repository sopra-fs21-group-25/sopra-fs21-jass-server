package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    @Mock
    private RegisteredUserRepository registeredUserRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;
    @InjectMocks
    private UserService userService;

    private RegisteredUser testRegisteredUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        testRegisteredUser = new RegisteredUser();
        testRegisteredUser.setPassword("verySafe");
        testRegisteredUser.setUsername("testUsername");

        Mockito.when(registeredUserRepository.findByUsername(Mockito.any())).thenReturn(testRegisteredUser);
        Mockito.when(registeredUserRepository.saveAndFlush(Mockito.any())).thenReturn(testRegisteredUser);

        userService.createRegisteredUser(testRegisteredUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void loginRegisteredUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        RegisteredUser createdUser = loginService.login(testRegisteredUser);

        // then
        Mockito.verify(registeredUserRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        assertEquals(testRegisteredUser.getUsername(), createdUser.getUsername());
        assertEquals(testRegisteredUser.getPassword(), createdUser.getPassword());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
        assertNotNull(createdUser.getToken());
    }

    @Test
    public void loginRegisteredUser_invalidPassword_failed() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        RegisteredUser userWithWrongPassword = new RegisteredUser();
        userWithWrongPassword.setUsername("testUsername");
        userWithWrongPassword.setPassword("VeryWrongPassoword");

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> loginService.login(userWithWrongPassword));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.FORBIDDEN, status);
        assertEquals("Invalid credentials", reason);
    }

    @Test
    public void loginRegisteredUser_invalidUsername_failed() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(registeredUserRepository.findByUsername(Mockito.any())).thenReturn(null);

        RegisteredUser userWithWrongUsername = new RegisteredUser();
        userWithWrongUsername.setUsername("veryWrongUsername");

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> loginService.login(userWithWrongUsername));

        var status = thrown.getStatus();
        var reason = thrown.getReason();

        assertEquals(HttpStatus.NOT_FOUND, status);
        assertEquals("No user with such username", reason);
    }
    @After
    public void cleanDatabase(){
        userRepository.deleteAll();
        registeredUserRepository.deleteAll();

        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(registeredUserRepository.findAll().isEmpty());
    }
}

