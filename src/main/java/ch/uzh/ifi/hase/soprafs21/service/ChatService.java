package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;
import ch.uzh.ifi.hase.soprafs21.entity.Group;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MessageRepository;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS.ChatMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Component
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;

    @Autowired
    public ChatService(@Qualifier("messageRepository") MessageRepository messageRepository, @Qualifier("groupRepository") GroupRepository groupRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    public Group evaluateGroupAssignment(UUID senderId, UUID environmentId, GroupType groupType) {
        if(groupType == GroupType.BIDIRECTIONAL) {
            return groupRepository.findByGroupTypeAndUsersWithIds(GroupType.BIDIRECTIONAL, senderId, environmentId);
        } else {
            return groupRepository.retrieveGroupByEnvironmentIdAsLobbyIdOrGameId(environmentId);
        }
    }

    public ChatMessageDTO storeAndConvert(ChatMessageDTO chatMessageDTO) {
        Message message = DTOMapper.INSTANCE.convertChatMessageDTOToMessage(chatMessageDTO, userService, this);
        message = messageRepository.saveAndFlush(message);
        return DTOMapper.INSTANCE.convertMessageToChatMessageDTO(message);
    }

    public List<ChatMessageDTO> getAllMessagesBetweenUserAAndUserB(UUID aId, UUID bId) {
        List<Message> messages = groupRepository.findOrderedByTimestampUserToUserMessagesSentBetweenUserAAndUserB(aId, bId);
        return messages.stream().map(DTOMapper.INSTANCE::convertMessageToChatMessageDTO).collect(Collectors.toList());
    }

    public List<ChatMessageDTO> getAllMessagesInLobby(UUID lobbyId) {
        List<Message> messages = groupRepository.findOrderedByTimestampLobbyMessages(lobbyId);
        return messages.stream().map(DTOMapper.INSTANCE::convertMessageToChatMessageDTO).collect(Collectors.toList());
    }

    public List<ChatMessageDTO> getAllMessagesInGame(UUID gameId) {
        List<Message> messages = groupRepository.findOrderedByTimestampGameMessages(gameId);
        return messages.stream().map(DTOMapper.INSTANCE::convertMessageToChatMessageDTO).collect(Collectors.toList());
    }
}
