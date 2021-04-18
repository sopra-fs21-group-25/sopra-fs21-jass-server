package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    RegisteredUser convertUserPostDTOtoRegisteredUser(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    GuestUser convertUserPostDTOtoGuestUser(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    FacebookUser convertUserPostDTOtoFacebookUser(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userType", target = "userType")
    @Mapping(source = "token", target = "token")
    UserGetDTO convertEntityToUserGetDTO(User user);

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
    Lobby convertLobbyPostDTOtoLobby(LobbyPostDTO lobbyPostDTO);
}
