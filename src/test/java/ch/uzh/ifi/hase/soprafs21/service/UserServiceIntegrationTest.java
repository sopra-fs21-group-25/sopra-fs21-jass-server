package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createRegisteredUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        RegisteredUser testUser = new RegisteredUser();
        testUser.setUsername("testUsername");
        testUser.setPassword("SuperDuperPasswordo");

        // when
        RegisteredUser createdUser = userService.createRegisteredUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createGuestUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        RegisteredUser testUser = new RegisteredUser();
        testUser.setUsername("testUsername");
        testUser.setPassword("SuperDuperPasswordo");

        // when
        RegisteredUser createdUser = userService.createRegisteredUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createRegisteredUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        RegisteredUser testUser = new RegisteredUser();
        testUser.setUsername("testUsername");
        testUser.setPassword("SuperDuperPasswordo");
        User createdUser = userService.createRegisteredUser(testUser);

        // attempt to create second user with same username
        RegisteredUser testUser2 = new RegisteredUser();

        // change the name but forget about the username
        testUser2.setUsername("testUsername");
        testUser.setPassword("SuperDuperPasswordo");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createRegisteredUser(testUser2));
    }
    @After
    public void cleanDatabase(){
        userRepository.deleteAll();

        assertTrue(userRepository.findAll().isEmpty());
    }

}
