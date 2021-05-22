package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyPosition;
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

import java.util.*;

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

    public List<Lobby> getAccessibleLobbies(UUID userId) {
        Set<Lobby> publicLobbies = lobbyRepository.getPublicLobbies();
        Set<Lobby> friendsLobbies = lobbyRepository.getFriendsLobbiesOfUserWithId(userId);

        publicLobbies.addAll(friendsLobbies);

        List<Lobby> orderedLobbies = new ArrayList<>(publicLobbies);
        Collections.sort(orderedLobbies);

        return orderedLobbies;
    }

    public Lobby getLobbyWithId(UUID id) { return lobbyRepository.getOne(id); }

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

        Set<User> lobbyUsers = lobby.getUsersInLobby();
        lobbyUsers.add(userToAdd);
        lobby.setUsersInLobby(lobbyUsers);

        return lobby;
    }

    public Lobby removeUserFromLobby(LobbyPutUserWithIdDTO userIdDTO, UUID lobbyId) {
        Lobby lobby = this.lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));

        User userToRemove = null;

        for(User u : lobby.getUsersInLobby()) {
            if(u.getId().equals(userIdDTO.getUserId())) {
                userToRemove = u;
                break;
            }
        }

        if(userToRemove == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not in lobby");
        }

        Set<User> lobbyUsers = new HashSet<>(lobby.getUsersInLobby());
        lobbyUsers.remove(userToRemove);

        lobby.setUsersInLobby(lobbyUsers);

        if(lobby.getUserTop() != null && lobby.getUserTop().equals(userToRemove)) {
            lobby.setUserTop(null);
        }
        if(lobby.getUserRight() != null && lobby.getUserRight().equals(userToRemove)) {
            lobby.setUserRight(null);
        }
        if(lobby.getUserBottom() != null && lobby.getUserBottom().equals(userToRemove)) {
            lobby.setUserBottom(null);
        }
        if(lobby.getUserLeft() != null && lobby.getUserLeft().equals(userToRemove)) {
            lobby.setUserLeft(null);
        }

        return lobby;
    }

    public Lobby placeUserAtPosition(UUID lobbyId, UUID userId, LobbyPosition pos) {
        Lobby lobby = this.lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));

        User userToPlace = null;

        for(User u : lobby.getUsersInLobby()) {
            if(u.getId().equals(userId)) {
                userToPlace = u;
                break;
            }
        }

        if(userToPlace == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not in lobby");
        }

        switch (pos) {
            case TOP -> {
                if(lobby.getUserTop() != null) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat already taken"); }
                lobby.setUserTop(userToPlace);
            }
            case RIGHT -> {
                if(lobby.getUserRight() != null) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat already taken"); }
                lobby.setUserRight(userToPlace);
            }
            case BOTTOM -> {
                if(lobby.getUserBottom() != null) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat already taken"); }
                lobby.setUserBottom(userToPlace);
            }
            case LEFT -> {
                if(lobby.getUserLeft() != null) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat already taken"); }
                lobby.setUserLeft(userToPlace);
            }
        }

        return lobby;
    }

    public Lobby unsitUserInLobby(UUID lobbyId, UUID userId) {
        Lobby lobby = this.lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));



        if(lobby.getUserTop() != null && lobby.getUserTop().getId().equals(userId)) {
            lobby.setUserTop(null);
        }
        if(lobby.getUserRight() != null && lobby.getUserRight().getId().equals(userId)) {
            lobby.setUserRight(null);
        }
        if(lobby.getUserBottom() != null && lobby.getUserBottom().getId().equals(userId)) {
            lobby.setUserBottom(null);
        }
        if(lobby.getUserLeft() != null && lobby.getUserLeft().getId().equals(userId)) {
            lobby.setUserLeft(null);
        }

        return lobby;
    }

    public Lobby clearLobby(Lobby lobby) {
        for(User user : lobby.getUsersInLobby()) {
            user.setLobby(null);
        }
        lobby.getUsersInLobby().clear();

        return lobby;
    }

    public void deleteLobby(Lobby lobby) {
        try {
            lobbyRepository.delete(lobby);
        } catch(Exception e) {
            System.out.println("Could not delete lobby, needs to be cleared!");
            throw e;
        }
    }

}
