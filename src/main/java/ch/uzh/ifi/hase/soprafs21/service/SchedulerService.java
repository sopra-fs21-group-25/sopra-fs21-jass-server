package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.repository.MessageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.Date;
import java.time.Duration; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchedulerService {

    @Autowired
    MessageRepository messageRepository;

    @Scheduled(cron="0 0 0 * * ?")
    public void cleanMessages()
    {
        Date creationDate = new Date(); 
        creationDate = new Date(creationDate.getTime() - Duration.ofDays(7).toMillis());
        messageRepository.removeOldMessages(creationDate);
    }
}
