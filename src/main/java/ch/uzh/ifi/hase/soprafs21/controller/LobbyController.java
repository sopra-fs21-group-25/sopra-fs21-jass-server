package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
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

/*    // No longer used. Maybe necessary for a later purpose
    @GetMapping("/lobbies/public_and_friends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getPublicAndFriendsLobbies() {
        List<Lobby> lobbies = lobbyService.getPublicAndFriendsLobbies();

        List<LobbyGetDTO> lobbyGetDTOs = lobbies
                .stream()
                .map(l -> DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(l))
                .collect(Collectors.toList());

        return lobbyGetDTOs;
    }*/

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

    @PutMapping("/lobbies/{lobbyId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void closeLobbyAndRemoveUsers(@PathVariable("lobbyId") UUID lobbyId) {
        Lobby lobbyToClose = lobbyService.getLobbyWithId(lobbyId);
        lobbyToClose = lobbyService.clearLobby(lobbyToClose);

        lobbyService.deleteLobby(lobbyToClose);
    }
}
