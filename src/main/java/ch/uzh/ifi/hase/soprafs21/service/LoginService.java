package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class LoginService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public LoginService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisteredUser login(RegisteredUser userToLogIn) {
        userToLogIn = checkIfUserExistsAndPasswordIsValid(userToLogIn);
        userToLogIn.setToken(UUID.randomUUID().toString());
        userToLogIn.setStatus(UserStatus.ONLINE);

        RegisteredUser loggedInUser = userRepository.saveAndFlush(userToLogIn);

        log.debug("User logged in: {}", loggedInUser);
        return loggedInUser;
    }

    private RegisteredUser checkIfUserExistsAndPasswordIsValid(RegisteredUser userToLogIn) throws ResponseStatusException {
        RegisteredUser userByUsername = (RegisteredUser) userRepository.findRegisteredUserByUsername(userToLogIn.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such username");
        }

        Boolean isPasswordValid = userByUsername.getPassword().compareTo(userToLogIn.getPassword()) == 0;

        if(!isPasswordValid) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");
        }

        return userByUsername;
    }
}
