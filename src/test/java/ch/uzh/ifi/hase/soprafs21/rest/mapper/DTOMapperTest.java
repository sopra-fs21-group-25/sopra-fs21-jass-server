package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.GameMode;
import ch.uzh.ifi.hase.soprafs21.game.Rank;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("name");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        RegisteredUser user = DTOMapper.INSTANCE.convertUserPostDTOtoRegisteredUser(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new RegisteredUser();
        user.setUsername("firstname@lastname");
        ((RegisteredUser) user).setStatus(UserStatus.OFFLINE);
        ((RegisteredUser) user).setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(((RegisteredUser) user).getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void convertEntitytoLobbyGetDTO() {
        User user = new RegisteredUser();
        user.setUsername("Thomas");
        user.setId(UUID.randomUUID());
        User user1 = new RegisteredUser();
        user1.setUsername("Thomas1");
        user1.setId(UUID.randomUUID());
        User user2 = new RegisteredUser();
        user2.setUsername("Thomas2");
        user2.setId(UUID.randomUUID());
        User user3 = new RegisteredUser();
        user3.setUsername("Thomas3");
        user3.setId(UUID.randomUUID());
        User[] userList = new User[4];
        userList[0]= user;
        userList[1]= user1;
        userList[2]= user2;
        userList[3]= user3;

        // create lobbypostDTO
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setMode(GameMode.SCHIEBER);
        lobbyPostDTO.setLobbyType("public");
        lobbyPostDTO.setStartingCard(new Card(Suit.BELL, Rank.ACE));
        lobbyPostDTO.setPointsToWin(1000);
        lobbyPostDTO.setWeis(true);
        lobbyPostDTO.setWeisAsk("allowed");
        lobbyPostDTO.setCrossWeis(true);
        lobbyPostDTO.setCreatorUsername("Thomas");
        lobbyPostDTO.setUsersInLobby(userList);
        // MAP -> Create UserGetDTO
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoLobby(lobbyPostDTO);

        // check content
        assertEquals( lobby.getCreatorUsername(), lobbyPostDTO.getCreatorUsername());
        assertEquals(lobby.getMode(), lobbyPostDTO.getMode());
        assertEquals(lobby.getPointsToWin(), lobbyPostDTO.getPointsToWin());
    }
}
