package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.controller.UserController;
import ch.uzh.ifi.hase.soprafs21.entity.FacebookUser;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.GuestUser;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.game.Card;
import ch.uzh.ifi.hase.soprafs21.game.Suit;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper(
        uses = {GameService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


/*
    User related mappings
*/
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    RegisteredUser convertUserPostDTOtoRegisteredUser(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    FacebookUser convertUserPostDTOtoFacebookUser(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "status", target = "status")
    RegisteredUser convertUserPutDTOtoRegisteredUser(UserPutDTO userPostDTO);

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
    @Mapping(target = "startingCard", expression = "java(new Card(lobby.getStartingCardSuit(), lobby.getStartingCardRank()))")
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

/*
    Game related mappings
*/

    /**
     * Here we are leveraging the getUserById method from the UserService.class to
     * convert a UUID userId to its corresponding user counterpart in the database;
     * For this purpose we extended the Mapper (see annotation of this interface)
     * to use UserService.class and specified the qualifier, i.e. the getUserById
     * method, by annotating the method in UserService.class with @Named(..).
     *
     * Additionally, since the player to start the first round of the game is only
     * known after the starting-card has been mapped already and the cards have been
     * distributed to the players, the BeanMapping ensures to set the starting-player
     * flag via calling an AfterMapping method defined in the SchieberGameSession.class.
     */
    @Mapping(source = "ingameModes", target = "ingameModes")
    @Mapping(source = "pointsToWin", target = "pointsToWin")
    @Mapping(target = "startingCardSuit", expression = "java(schieberGamePostDTO.getStartingCard().getSuit())")
    @Mapping(target = "startingCardRank", expression = "java(schieberGamePostDTO.getStartingCard().getRank())")
    @Mapping(source = "weisAllowed", target = "weisAllowed")
    @Mapping(source = "crossWeisAllowed", target = "crossWeisAllowed")
    @Mapping(source = "weisAsk", target = "weisAsk")
    SchieberGameSession convertSchieberGamePostDTOtoEntity(SchieberGamePostDTO schieberGamePostDTO, @Context GameService gameService);
    @AfterMapping
    default void mapUserIdToUser(
            @MappingTarget SchieberGameSession schieberGameSession,
            SchieberGamePostDTO schieberGamePostDTO,
            @Context GameService gameService)
    {   schieberGameSession.setUser0(gameService.getUserWithId(schieberGamePostDTO.getPlayer0id()));
        schieberGameSession.setUser1(gameService.getUserWithId(schieberGamePostDTO.getPlayer1id()));
        schieberGameSession.setUser2(gameService.getUserWithId(schieberGamePostDTO.getPlayer2id()));
        schieberGameSession.setUser3(gameService.getUserWithId(schieberGamePostDTO.getPlayer3id())); }
    @AfterMapping
    default void initializeStartingPlayer(@MappingTarget SchieberGameSession schieberGameSession) {
        schieberGameSession.setInitiallyStartingPlayer(); }


    @Mapping(source = "id", target = "id")
    @Mapping(target = "player0id", expression = "java(schieberGameSession.getUser0().getId())")
    @Mapping(target = "player1id", expression = "java(schieberGameSession.getUser1().getId())")
    @Mapping(target = "player2id", expression = "java(schieberGameSession.getUser2().getId())")
    @Mapping(target = "player3id", expression = "java(schieberGameSession.getUser3().getId())")
    @Mapping(source = "pointsToWin", target = "pointsToWin")
    @Mapping(source = "ingameModes", target = "ingameModes")
    @Mapping(source = "weisAllowed", target = "weisAllowed")
    @Mapping(source = "crossWeisAllowed", target = "crossWeisAllowed")
    @Mapping(source = "weisAsk", target = "weisAsk")
    @Mapping(source = "pointsTeam0_2", target = "pointsTeam1_3")
    @Mapping(source = "trickToPlay", target = "trickToPlay")
    @Mapping(target = "playerStartsTrick", expression = "java(schieberGameSession.composePlayerStartsTrickArray())")
    @Mapping(target = "cardsPlayed", expression = "java(schieberGameSession.composeCardsPlayedArray())")
    @Mapping(source = "hasTrickStarted", target = "hasTrickStarted")
    @Mapping(source = "idOfRoundStartingPlayer", target = "idOfRoundStartingPlayer")
    SchieberGameGetDTO convertEntityToSchieberGameGetDTO(SchieberGameSession schieberGameSession);


}
