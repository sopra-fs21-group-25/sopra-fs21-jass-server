package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class FriendService {

    private final Logger log = LoggerFactory.getLogger(FriendService.class);

    private final UserRepository userRepository;

    @Autowired
    public FriendService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getFriends(User a){
        if(a.getFriends() != null){
            List<User> friends = new ArrayList<>(a.getFriends());
            friends.addAll(a.getfriendOf()); 
            return  friends;
        }
        return new ArrayList<>();
    }

    public void addFriends(User a, User b){
        List<User> friends = new ArrayList<>(a.getFriends());
        friends.add(b);
        a.setFriends(friends);
    }

    public void removeFriend(User a, User b){
        List<User> friends_A = a.getFriends();
        List<User> friends_B = b.getFriends();
        if(friends_A != null){
            if (friends_A.contains(b)){
                friends_A.remove(b);
            }
        }
        if(friends_B != null){
            if (friends_B.contains(a)){
                friends_B.remove(a);
            }
        }
        a.setFriends(friends_A);
        b.setFriends(friends_B);
    }
}
