package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyPosition;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyUsersGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) { this.lobbyService = lobbyService; }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

        for(Lobby lobby : lobbies) {
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }

        return lobbyGetDTOs;
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyWithId(@PathVariable("lobbyId") UUID id) {
        Lobby lobby = lobbyService.getLobbyWithId(id);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/accessible/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getLobbiesAccessibleByUserWithId(@PathVariable("userId") UUID userId) {
        List<Lobby> accessibleLobbies = lobbyService.getAccessibleLobbies(userId);

        List<LobbyGetDTO> result = accessibleLobbies.stream()
                .map(lobby -> DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby))
                .collect(Collectors.toList());

        return result;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        Lobby newLobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoLobby(lobbyPostDTO);
        newLobby = lobbyService.createLobby(newLobby);

        LobbyGetDTO returnedLobby = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(newLobby);

        return returnedLobby;
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO addOrRemoveUserToLobby(@PathVariable("lobbyId") UUID lobbyId, @RequestBody LobbyPutUserWithIdDTO userIdDTO) {
        Lobby updatedLobby = null;
        if(userIdDTO.getAdd() && !userIdDTO.getRemove()) {
            updatedLobby = lobbyService.addUserToLobby(userIdDTO, lobbyId);
        } else if(!userIdDTO.getAdd() && userIdDTO.getRemove()) {
            updatedLobby = lobbyService.removeUserFromLobby(userIdDTO, lobbyId);
        }
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PutMapping("/lobbies/{lobbyId}/sit/{userId}/{position}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO sitUserAtPositionInLobby(@PathVariable("lobbyId") UUID lobbyId, @PathVariable("position") LobbyPosition pos, @PathVariable("userId") UUID userId) {
        Lobby updatedLobby = lobbyService.placeUserAtPosition(lobbyId, userId, pos);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PutMapping("/lobbies/{lobbyId}/unsit/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO unsitUserInLobby(@PathVariable("lobbyId") UUID lobbyId, @PathVariable("userId") UUID userId) {
        Lobby updatedLobby = lobbyService.unsitUserInLobby(lobbyId, userId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @DeleteMapping("/lobbies/{lobbyId}/delete/no-cascade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteLobbyNoCascadeDeleteChatGroup(@PathVariable("lobbyId") UUID lobbyId) {
        Lobby lobbyToClose = lobbyService.getLobbyWithId(lobbyId);
        lobbyToClose = lobbyService.clearLobby(lobbyToClose);

        lobbyService.deleteNoCascadeChatGroupLobby(lobbyToClose);
    }

    @DeleteMapping("/lobbies/{lobbyId}/delete/cascade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteLobbyAndCascadeDeleteChatGroup(@PathVariable("lobbyId") UUID lobbyId) {
        Lobby lobbyToClose = lobbyService.getLobbyWithId(lobbyId);
        lobbyToClose = lobbyService.clearLobby(lobbyToClose);

        lobbyService.deleteCascadeChatGroupLobby(lobbyToClose);
    }

    /* Returns a DTO containing an array of users, whose fields are null except for
    the username, userId, userToken and userStatus */
    @GetMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyUsersGetDTO getUsersInLobbyWithId(@PathVariable("lobbyId") UUID lobbyId) {
        Lobby lobby = lobbyService.getLobbyWithId(lobbyId);

        // is a copy of the actual list of users in the lobby. Necessary for reference detaching
        List<User> usersInLobby = new ArrayList<>(lobby.getUsersInLobby());

        User[] users = new User[lobby.getUsersInLobby().size()];

        for(int i=0; i<lobby.getUsersInLobby().size(); i++) {
            User u = usersInLobby.get(i);
            u.setLobby(null);
            u.setfriendOf(null);
            u.setFriends(null);
            u.setPendingFriendRequests(null);
            u.setSentFriendRequests(null);

            users[i] = u;
        }

        LobbyUsersGetDTO result = new LobbyUsersGetDTO();
        result.setUsersInLobby(users);

        return result;
    }
}
