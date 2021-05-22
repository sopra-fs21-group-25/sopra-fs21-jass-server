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
import org.springframework.stereotype.Component;
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
@Component
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
        boolean userExists = true;
        GuestUser newGuest = new GuestUser();

        try {
            while (userExists) {
                newGuest.setFunnyUsername();

                userExists = checkIfUserExists(newGuest);
            }
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create guest user");
        }

        newGuest.setStatus(UserStatus.ONLINE);
        newGuest.setToken(UUID.randomUUID().toString());

        // saves the given entity but data is only persisted in the database once flush() is called
        newGuest = userRepository.save(newGuest);
        userRepository.flush();
        log.debug("Created Information for User: {}", newGuest);
        return newGuest;
    }

    public User createGoogleUser(User newUser) {
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


    public User getUserById(UUID id) {
        User user = this.userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a user with this id."));
        return user;
    }

    public List<User> getOnlineUsers() { return userRepository.findAllByStatus(UserStatus.ONLINE); }

    public List<User> getAvailableUsers(UUID id){
        List<User> test = userRepository.availableUsersForUserWithId(id);
        return userRepository.availableUsersForUserWithId(id);
    }

//    public RegisteredUser updateUser(RegisteredUser user, String newUsername) {
//            String oldname = user.getUsername();
//            user.setUsername(newUsername);
//            if (!checkIfUserExists(user)){
//                userRepository.saveAndFlush(user);
//            }
//            else{
//               user.setUsername(oldname);
//               throwUserConflict();
//            }
//        return user;
//    }
    public void updateUserProfile(UUID userId, RegisteredUser userInput) throws ResponseStatusException {
        User userToUpdate = getUserById(userId);

        if (userRepository.findByUsername(userInput.getUsername()) != null){
            String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
        if (userInput.getUsername() != null){
            userToUpdate.setUsername(userInput.getUsername());}
        userRepository.saveAndFlush(userToUpdate);
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

    public void logout(String token) {
        User userToBeLoggedOut = userRepository.findByToken(token);
        userToBeLoggedOut.setStatus(UserStatus.OFFLINE);
    }

    // ---------------------------------------------------------

    public List<User> getRemainingUsersForUserWithId(UUID userId) {
        return userRepository.usersNotBefriendedWith(userId);
    }
}
