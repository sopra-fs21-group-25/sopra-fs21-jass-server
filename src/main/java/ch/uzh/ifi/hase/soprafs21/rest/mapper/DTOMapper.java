package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.RegisteredUser;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper(
        uses = {GameService.class, UserService.class, ChatService.class},
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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "status", target = "status")
    RegisteredUser convertUserPutDTOtoRegisteredUser(UserPutDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userType.type", target = "userType")
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
    @Mapping(source = "usersInLobby", target = "usersInLobby", qualifiedByName = "convertUsersToUserGetDTOs")
    @Mapping(target = "userTop", expression = "java(this.convertEntityToUserGetDTO(lobby.getUserTop()))")
    @Mapping(target = "userRight", expression = "java(this.convertEntityToUserGetDTO(lobby.getUserRight()))")
    @Mapping(target = "userBottom", expression = "java(this.convertEntityToUserGetDTO(lobby.getUserBottom()))")
    @Mapping(target = "userLeft", expression = "java(this.convertEntityToUserGetDTO(lobby.getUserLeft()))")
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

    @Named("convertUsersToUserGetDTOs")
    static UserGetDTO[] convertUsersToUserGetDTOs(Set<User> users) {
        UserGetDTO[] userGetDTOs = new UserGetDTO[users.size()];

        int i = 0;
        for(User user : users) { userGetDTOs[i++] = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user); }

        return userGetDTOs;
    }



/*
    FriendRequest related mappings
*/

    @Mapping(target = "fromUser", expression = "java(userService.getUserById(friendRequestPostDTO.getFromId()))")
    @Mapping(target = "toUser", expression = "java(userService.getUserById(friendRequestPostDTO.getToId()))")
    FriendRequest convertFriendRequestPostDTOToFriendRequest(FriendRequestPostDTO friendRequestPostDTO, @Context UserService userService);


    @Mapping(target = "fromId", expression = "java(friendRequest.getFromUser().getId())")
    @Mapping(target = "toId", expression = "java(friendRequest.getToUser().getId())")
    @Mapping(target = "fromUsername", expression = "java(friendRequest.getFromUser().getUsername())")
    FriendRequestGetDTO convertEntityToFriendRequestGetDTO(FriendRequest friendRequest);


/*
    Game related mappings
*/

    /**
     * Since the player to start the first round of the game is only
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
    @Mapping(source = "pointsTeam0_2", target = "pointsTeam0_2")
    @Mapping(source = "pointsTeam1_3", target = "pointsTeam1_3")
    @Mapping(source = "trickToPlay", target = "trickToPlay")
    @Mapping(target = "playerStartsTrick", expression = "java(schieberGameSession.composePlayerStartsTrickArray())")
    @Mapping(target = "cardsPlayed", expression = "java(schieberGameSession.composeCardsPlayedArray())")
    @Mapping(source = "hasTrickStarted", target = "hasTrickStarted")
    @Mapping(source = "idOfRoundStartingPlayer", target = "idOfRoundStartingPlayer")
    @Mapping(source = "currentIngameMode", target = "currentIngameMode")
    SchieberGameGetDTO convertEntityToSchieberGameGetDTO(SchieberGameSession schieberGameSession);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    RegisteredUser convertUserPutDTOtoEntity(UserPutDTO userPutDTO);


/*
    Message related mappings
*/
    @Mapping(target = "sender", expression = "java(userService.getUserById(chatMessageDTO.getSenderId()))")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "timestamp", target = "timestamp")
    Message convertChatMessageDTOToMessage(ChatMessageDTO chatMessageDTO, @Context UserService userService, @Context ChatService chatService);
    @AfterMapping
    default void attainAndAssignGroup(@MappingTarget Message message, ChatMessageDTO chatMessageDTO, @Context ChatService chatService) {
        Group group = chatService.evaluateGroupAssignment(chatMessageDTO.getSenderId(), chatMessageDTO.getEnvironmentId(), chatMessageDTO.getGroupType());
        message.setGroup(group);
    }

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "group.groupType", target = "groupType")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(source = "text", target = "text")
    @Mapping(target = "environmentId", expression = "java(message.getGroup().evaluateEnvironmentId(message.getSender().getId()))")
    ChatMessageDTO convertMessageToChatMessageDTO(Message message);
}
