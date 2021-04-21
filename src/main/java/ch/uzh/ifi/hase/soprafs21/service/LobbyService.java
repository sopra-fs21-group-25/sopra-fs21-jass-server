package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public List<Lobby> getLobbies() { return this.lobbyRepository.findAll(); }

    public List<Lobby> getPublicAndFriendsLobbies() {
        return lobbyRepository.getAllExcludePrivate();
    }

    public Lobby createLobby(Lobby newLobby) {

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    public Lobby addUserToLobby(LobbyPutUserWithIdDTO userIdDTO, UUID lobbyId) {

        Lobby lobby = this.lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));

        User userToAdd = this.lobbyRepository.findUserById(userIdDTO.getUserId());

        lobby.getUsersInLobby().add(userToAdd);

        return lobby;
    }
}
