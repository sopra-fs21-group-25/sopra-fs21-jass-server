package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public RegisteredUser createRegisteredUser(RegisteredUser newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);

        boolean userExists = checkIfUserExists(newUser);

        if (userExists) {
            throwUserConflict();
        }

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public GuestUser createGuestUser() {
        GuestUser newGuest = new GuestUser();
        boolean userExists = true;

        try {
            while (userExists) {
                newGuest.setFunnyUsername();
                newGuest.setStatus(UserStatus.ONLINE);

                userExists = checkIfUserExists(newGuest);
            }
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create guest user");
        }

        // saves the given entity but data is only persisted in the database once flush() is called
        newGuest = userRepository.save(newGuest);
        userRepository.flush();

        log.debug("Created Information for User: {}", newGuest);
        return newGuest;
    }

    public User createFacebookUser(User newUser) {
        boolean userExists = checkIfUserExists(newUser);

        if (userExists) {
            throwUserConflict();
        }

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void throwUserConflict() {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "create User failed because username already exists");
    }

    private boolean checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        return userByUsername != null;
    }
}
