package ch.uzh.ifi.hase.soprafs21.stompWebsocket.controllerWS;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FriendRequestWSController {

    @MessageMapping("/friend_requests/{toUserId}")
    @SendTo("/friend_requests/notify/{toUserId}")
    public String notifyToFetchFriendRequests(String msg) {
        return msg;
    }

    @MessageMapping("/friends/notify_remove/{toUserId}")
    @SendTo("/friends/notify_remove/{toUserId}")
    public String notifyToFetchFriendsAndUsers(@Payload String fromUserId) { return fromUserId; }
}
