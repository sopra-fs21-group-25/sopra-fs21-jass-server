package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.ProfileImage;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.ResponseMessage;
import ch.uzh.ifi.hase.soprafs21.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
//import org.apache.tomcat.jni.FileInfo;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import io.github.classgraph.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

@RestController
public class ProfilePicController {

    @Autowired
    UserService userService;


    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/files/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UUID uploadFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        ProfileImage dbImage = new ProfileImage();
        dbImage.setUserId(id);
        dbImage.setContent(file.getBytes());

        return imageRepository.save(dbImage)
                .getPicId();
    }

    @GetMapping("/files/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ByteArrayResource getFile(@PathVariable UUID id) {
        //User user = userService.getUserById(id);
        byte[] image = imageRepository.findByUserIdIs(id)
                //.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();

        return new ByteArrayResource(image);
    }
}
