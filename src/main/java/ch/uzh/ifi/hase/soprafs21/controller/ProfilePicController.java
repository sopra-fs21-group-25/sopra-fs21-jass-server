package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.repository.RegisteredUserRepository;
//import org.apache.tomcat.jni.FileInfo;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import nonapi.io.github.classgraph.utils.FileUtils;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.tools.FileObject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@RestController
public class ProfilePicController {

    @Autowired
    UserService userService;


    @Autowired
    RegisteredUserRepository registeredUserRepository;

    @PostMapping(value = "/files/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity uploadFile(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
        RegisteredUser user = (RegisteredUser) userService.getUserById(id);

        user.setProfilePicture(file.getBytes());

        registeredUserRepository.save(user);

        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.newStringUtf8(Base64.getEncoder().encode(file.getBytes())));
        sb.toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"")
                .body(sb);
    }


    @GetMapping("/files/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity getFile(@PathVariable UUID id) throws IOException {
        RegisteredUser user = (RegisteredUser) userService.getUserById(id);
        byte[] picture = user.getProfilePicture();

        if (picture == null){
            BufferedImage bImage = ImageIO.read(new File("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/acorn.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos );
            picture = bos.toByteArray();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.newStringUtf8(Base64.getEncoder().encode(picture)));
        sb.toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"")
                .body(sb);
    }
}
