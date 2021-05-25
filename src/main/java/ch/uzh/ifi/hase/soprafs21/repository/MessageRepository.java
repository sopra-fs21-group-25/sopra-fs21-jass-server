package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;
import java.util.Date;

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Modifying
    @Query(value = "DELETE FROM Message m WHERE m.timestamp < :date")
    void removeOldMessages(@Param("date") Date creationDate);
}
