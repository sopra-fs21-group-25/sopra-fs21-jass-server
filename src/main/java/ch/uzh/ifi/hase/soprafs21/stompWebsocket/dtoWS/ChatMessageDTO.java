package ch.uzh.ifi.hase.soprafs21.stompWebsocket.dtoWS;

import ch.uzh.ifi.hase.soprafs21.constant.GroupType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ChatMessageDTO {

    private UUID senderId;
    private String senderUsername;
    private UUID environmentId;
    private GroupType groupType;
    private Date timestamp;
    private String text;


    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof ChatMessageDTO)) {
            return false;
        }

        ChatMessageDTO dto = (ChatMessageDTO) o;
        return this.senderId.equals(dto.getSenderId()) &&
                this.senderUsername.equals(dto.getSenderUsername()) &&
                this.environmentId.equals(dto.getEnvironmentId()) &&
                this.groupType == dto.getGroupType() &&
                this.timestamp.equals(dto.getTimestamp()) &&
                this.text.equals(dto.getText());
    }



    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getEnvironmentId() { return environmentId; }

    public void setEnvironmentId(UUID environmentId) { this.environmentId = environmentId; }

    public GroupType getGroupType() { return groupType; }

    public void setGroupType(GroupType groupType) { this.groupType = groupType; }
}
