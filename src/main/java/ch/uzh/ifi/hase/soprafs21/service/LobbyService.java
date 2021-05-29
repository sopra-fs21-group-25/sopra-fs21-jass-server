package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyPosition;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
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
    private final GroupRepository groupRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("groupRepository") GroupRepository groupRepository) {
        this.lobbyRepository = lobbyRepository;
        this.groupRepository = groupRepository;
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

    public Lobby getLobbyWithId(UUID id) {
        Lobby foundLobby = lobbyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));

        return foundLobby;
    }

    public Lobby createLobby(Lobby newLobby) {
        Group chatGroup = new Group(GroupType.COLLECTIVE);
        chatGroup = groupRepository.saveAndFlush(chatGroup);
        newLobby.setGroup(chatGroup);
        newLobby = lobbyRepository.saveAndFlush(newLobby);

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    public Lobby addUserToLobby(LobbyPutUserWithIdDTO userIdDTO, UUID lobbyId) {

        Lobby lobby = this.lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find a lobby with this id."));

        User userToAdd = this.lobbyRepository.findUserById(userIdDTO.getUserId());

        lobby.getUsersInLobby().add(userToAdd);
        userToAdd.getGroups().add(lobby.getGroup());

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

        lobby.getUsersInLobby().remove(userToRemove);
        userToRemove.getGroups().remove(lobby.getGroup());

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
        Group group = lobby.getGroup();
        for(User user : lobby.getUsersInLobby()) {
            user.setLobby(null);
            user.getGroups().remove(group);
        }
        group.setUsers(null);
        lobby.getUsersInLobby().clear();

        return lobby;
    }

    public void deleteCascadeChatGroupLobby(Lobby lobby) {
        Group group = lobby.getGroup();
        for(User user : lobby.getUsersInLobby()) {
            user.setLobby(null);
            user.getGroups().remove(group);
        }
        group.setUsers(null);
        lobby.getUsersInLobby().clear();

        try {
            lobbyRepository.deleteById(lobby.getId());
            groupRepository.deleteById(group.getId());
        } catch(Exception e) {
            log.error("Could not delete lobby. Error: ", e);
            throw e;
        }
    }

    public void deleteNoCascadeChatGroupLobby(Lobby lobby) {
        List<User> usersAtTable = new ArrayList<>();
        usersAtTable.add(lobby.getUserTop());
        usersAtTable.add(lobby.getUserLeft());
        usersAtTable.add(lobby.getUserBottom());
        usersAtTable.add(lobby.getUserRight());

        Group group = lobby.getGroup();
        for(User user : lobby.getUsersInLobby()) {
            user.setLobby(null);
            if(!usersAtTable.contains(user)) {
                user.getGroups().remove(group);
            }
        }
        group.setUsers(usersAtTable);
        lobby.getUsersInLobby().clear();

        try {
            lobbyRepository.delete(lobby);
        } catch (Exception e) {
            log.error("Could not delete lobby. Error: ", e);
        }
    }

}
