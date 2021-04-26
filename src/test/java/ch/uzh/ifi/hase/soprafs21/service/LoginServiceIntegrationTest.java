package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

//@AutoConfigureMockMvc
@SpringBootTest
public class LoginServiceIntegrationTest {

    @Qualifier("registeredUserRepository")
    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @Autowired
    private LoginService loginService;

//    @Autowired
//    private MockMvc mvc;


    @BeforeEach
    public void register(){
        registeredUserRepository.deleteAll();

        RegisteredUser user = new RegisteredUser();
        user.setUsername("coolName");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);

        registeredUserRepository.saveAndFlush(user);
    }

    @Test
    public void login_RegisteredUser_validInputs_success()  {
        // given
        assertNotNull(registeredUserRepository.findByUsername("coolName"));

        RegisteredUser usertoLogin = new RegisteredUser();
        usertoLogin.setUsername("coolName");
        usertoLogin.setPassword("password");

        // when
        RegisteredUser returnedUser = loginService.login(usertoLogin);

        // then
        assertNotNull(returnedUser.getId());
        assertEquals(UserStatus.ONLINE, returnedUser.getStatus());
    }

    @Test
    public void login_RegisteredUser_invalidPassword_fails ()  {
        assertNotNull(registeredUserRepository.findByUsername("coolName"));

        RegisteredUser usertoLogin = new RegisteredUser();
        usertoLogin.setUsername("coolName");
        usertoLogin.setPassword("wrongPassword");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> loginService.login(usertoLogin));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void login_RegisteredUser_invalidUsername_fails ()  {
        assertNotNull(registeredUserRepository.findByUsername("coolName"));

        RegisteredUser usertoLogin = new RegisteredUser();
        usertoLogin.setUsername("coolName_Wrongly_Written_EifachVollFalschHey");
        usertoLogin.setPassword("password");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> loginService.login(usertoLogin));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


//    // Problem here now is that we do POST the UserPostDTO instead of the corresponding JSON formatted object
//    // Should also be moved to the LoginControllerIntegrationTest
//    // Needs also the following annotation for the test class to work: @AutoConfigureMockMvc
//    @Test
//    public void loginThroughAllLayers() throws Exception {
//        UserPostDTO usertoLogin = new UserPostDTO();
//        usertoLogin.setUsername("coolName");
//        usertoLogin.setPassword("password");
//
//        ResultActions result = mvc.perform(MockMvcRequestBuilders
//                .post("/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(usertoLogin.toString()));
//
//        result.andExpect(status().isOk());
//    }
}