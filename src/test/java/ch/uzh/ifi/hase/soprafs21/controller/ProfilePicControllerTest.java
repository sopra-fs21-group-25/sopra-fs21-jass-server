package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.HeaderAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfilePicController.class)
class ProfilePicControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RegisteredUserRepository registeredUserRepository;

    @Test
    void uploadFile_successful_test() throws Exception {
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(new UUID(1,1));
        registeredUser.setUsername("Jean-Claude");
        registeredUser.setToken("1");
        registeredUser.setStatus(UserStatus.ONLINE);

        //create a mock multipartfile
        Path path = Paths.get("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/acorn.png");
        String name = "acorn.png";
        String originalFileName = "acorn.png";
        String contentType = MediaType.IMAGE_PNG_VALUE;;
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);

        //when
        given(userService.getUserById(Mockito.any())).willReturn(registeredUser);
        given(registeredUserRepository.save(Mockito.any())).willReturn(registeredUser);
       //doNothing().when(registeredUserRepository).save(registeredUser);

        //then
        MockHttpServletRequestBuilder postRequest = post("/files/"+ registeredUser.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(String.valueOf(result));

        mockMvc.perform(postRequest)
                .andExpect(header().stringValues("attachment; filename=\""));
    }

    @Test
    void getFile() throws Exception {
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(new UUID(1,1));
        registeredUser.setUsername("Zora");
        registeredUser.setToken("1");
        registeredUser.setStatus(UserStatus.ONLINE);

        //create a mock multipartfile
        Path path = Paths.get("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/acorn.png");
        String name = "acorn.png";
        String originalFileName = "acorn.png";
        String contentType = MediaType.IMAGE_PNG_VALUE;;
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);

        registeredUser.setProfilePicture(result.getBytes());

        given(userService.getUserById(Mockito.any())).willReturn(registeredUser);

        //then
        MockHttpServletRequestBuilder getRequest = get("/files/"+ registeredUser.getId());


        mockMvc.perform(getRequest)
                .andExpect(header().stringValues("attachment; filename=\""))
                .andExpect(status().isOk());
    }
}