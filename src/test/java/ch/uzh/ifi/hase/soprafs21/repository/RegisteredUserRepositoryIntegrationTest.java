package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RegisteredUserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @Test
    public void findByUsername_success() {
        // given
        RegisteredUser user = new RegisteredUser();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setPassword("password");
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = registeredUserRepository.findByUsername(user.getUsername());

        // then
        assertEquals(found.getId(), user.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getStatus(), user.getStatus());
    }
}
