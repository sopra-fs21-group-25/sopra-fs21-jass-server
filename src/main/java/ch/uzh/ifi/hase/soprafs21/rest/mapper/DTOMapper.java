package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.FacebookUser;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

/*
    User related mappings
*/
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    RegisteredUser convertUserPostDTOtoRegisteredUser(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    FacebookUser convertUserPostDTOtoFacebookUser(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userType", target = "userType")
    @Mapping(source = "token", target = "token")
    // @Mapping(source = "friends", target = "friends")
    @Mapping(source = "lobby.id", target = "lobbyId")
    UserGetDTO convertEntityToUserGetDTO(User user);

/*
    Lobby related mappings
*/

    @Mapping(source = "id", target = "id")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "lobbyType", target = "lobbyType")
    @Mapping(source = "ingameModes", target = "ingameModes")
    @Mapping(source = "startingCardRank", target = "startingCard.rank")
    @Mapping(source = "startingCardSuit", target = "startingCard.suit")
    @Mapping(source = "pointsToWin", target = "pointsToWin")
    @Mapping(source = "weisAllowed", target = "weis")
    @Mapping(source = "crossWeisAllowed", target = "crossWeis")
    @Mapping(source = "weisAsk", target = "weisAsk")
    @Mapping(source = "creatorUsername", target = "creatorUsername")
    @Mapping(source = "usersInLobby", target = "usersInLobby", qualifiedByName = "userSetToUsernames")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "lobbyType", target = "lobbyType")
    @Mapping(source = "ingameModes", target = "ingameModes")
    @Mapping(source = "startingCard.suit", target = "startingCardSuit")
    @Mapping(source = "startingCard.rank", target = "startingCardRank")
    @Mapping(source = "pointsToWin", target = "pointsToWin")
    @Mapping(source = "weis", target = "weisAllowed")
    @Mapping(source = "crossWeis", target = "crossWeisAllowed")
    @Mapping(source = "weisAsk", target = "weisAsk")
    @Mapping(source = "creatorUsername", target = "creatorUsername")
    @Mapping(source = "usersInLobby", target = "usersInLobby")
    Lobby convertLobbyPostDTOtoLobby(LobbyPostDTO lobbyPostDTO);

    @Named("userSetToUsernames")
    static String[] userSetToUsernames(Set<User> users) {
        int length = users.size();

        String[] usernames = new String[length];

        int i = 0;
        for(User user : users) {
            usernames[i] = user.getUsername();
            i++;
        }

        return usernames;
    }

/*
    FriendRequest related mappings
*/

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fromId", target = "fromId")
    @Mapping(source = "toId", target = "toId")
    FriendRequestGetDTO convertEntityToFriendRequestGetDTO(FriendRequest friendRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fromUser", target = "fromUser")
    @Mapping(source = "toUser", target = "toUser")
    FriendRequest convertFriendRequestPostDTOToFriendRequest(FriendRequestPostDTO friendRequestPostDTO);
}
