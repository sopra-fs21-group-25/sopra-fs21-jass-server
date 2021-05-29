package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.Avatar;
//import org.apache.tomcat.jni.FileInfo;
import ch.uzh.ifi.hase.soprafs21.service.AvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;

@RestController
public class AvatarController {

    private final Logger log = LoggerFactory.getLogger(AvatarController.class);
    private final AvatarService avatarService;

    AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }


    @GetMapping("/files/{userId}")
    @ResponseBody
    public ResponseEntity<byte[]> getAvatarAsBase64File(@PathVariable("userId") UUID userId) throws IOException {
        Avatar avatar = avatarService.getAvatarOfUserWithId(userId);

        try {
            byte[] imageFile = avatar.getAvatarFile();

            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(imageFile));
            MediaType type = MediaType.valueOf(URLConnection.guessContentTypeFromStream(is));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(type);

            imageFile = Base64.getEncoder().encode(imageFile);
            return new ResponseEntity<>(imageFile, headers, HttpStatus.OK);

        } catch (IOException e) {
            log.debug("Could not guess MimeType, exception thrown: ", e);
            throw e;
        }
    }

    @PostMapping("/files/{userId}")
    @ResponseBody
    public ResponseEntity<byte[]> uploadAvatarAsMultipartFile(@PathVariable("userId") UUID userId, @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.updateAndGetAvatarOfUserWithId(userId, file.getBytes());

        try {
            byte[] imageFile = avatar.getAvatarFile();

            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(imageFile));
            MediaType type = MediaType.valueOf(URLConnection.guessContentTypeFromStream(is));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(type);

            imageFile = Base64.getEncoder().encode(imageFile);
            return new ResponseEntity<>(imageFile, headers, HttpStatus.OK);

        } catch (IOException e) {
            log.debug("Could not guess MimeType, exception thrown: ", e);
            throw e;
        }
    }


}
