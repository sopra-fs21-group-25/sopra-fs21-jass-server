package ch.uzh.ifi.hase.soprafs21.entity;


import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
public class SchieberGameSession implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_top_id", referencedColumnName = "id")
    private User userTop;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_bottom_id", referencedColumnName = "id")
    private User userBottom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_left_id", referencedColumnName = "id")
    private User userLeft;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_right_id", referencedColumnName = "id")
    private User userRight;
}
