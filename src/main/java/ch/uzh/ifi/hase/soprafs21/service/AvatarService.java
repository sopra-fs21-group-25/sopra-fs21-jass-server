package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Avatar;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.AvatarRepository;
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
public class AvatarService {

    private final Logger log = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    @Autowired
    public AvatarService(@Qualifier("avatarRepository") AvatarRepository avatarRepository, @Qualifier("userRepository")UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }


    public Avatar getAvatarOfUserWithId(UUID userId) throws ResponseStatusException {
        Avatar avatar = avatarRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find avatar of user with this id"));

        return avatar;
    }

    public Avatar updateAndGetAvatarOfUserWithId(UUID userId, byte[] imageFile) {
        Avatar avatar = avatarRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with this id whose avatar is to be updated"));

        avatar.setAvatarFile(imageFile);
        return avatar;
    }
}
