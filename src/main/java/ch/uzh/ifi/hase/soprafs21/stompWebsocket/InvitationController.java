package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.criteria.CriteriaBuilder;


@Controller
public class InvitationController {

    @MessageMapping("/invite/request/{username}")
    @SendTo("/invite/response/{username}")
    public InvitationResponse invite(@PathVariable String username, InvitationRequest request) {
        InvitationResponse response = new InvitationResponse();
        response.setContent(request.getUsernameToBeInvited());

        return response;
    }



}
