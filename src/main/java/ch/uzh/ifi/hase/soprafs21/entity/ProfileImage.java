package ch.uzh.ifi.hase.soprafs21.entity;


import org.apache.catalina.connector.Response;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name= "images")
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID picId;

    @Lob
    private byte[] content;

    @Column(name = "id", table = "users", insertable = false, updatable = false)
    UUID userId;

    public UUID getPicId() {
        return picId;
    }

    public void setPicId(UUID picId) {
        this.picId = picId;
    }
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID id) {
        this.userId = id;
    }

}
