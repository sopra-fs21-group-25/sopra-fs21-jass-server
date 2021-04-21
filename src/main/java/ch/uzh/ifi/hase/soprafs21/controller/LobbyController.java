package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutUserWithIdDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public LobbyGetDTO addUserToLobby(@PathVariable("lobbyId") UUID lobbyId, @RequestBody LobbyPutUserWithIdDTO userIdDTO) {
        Lobby updatedLobby = lobbyService.addUserToLobby(userIdDTO, lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }
}
