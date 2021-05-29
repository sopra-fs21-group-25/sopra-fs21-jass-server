package ch.uzh.ifi.hase.soprafs21.entity;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "avatars")
public class Avatar {

    @Id
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private RegisteredUser user;

    @Type(type = "org.hibernate.type.ImageType")
    private byte[] avatarFile;



    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public byte[] getAvatarFile() throws IOException {
        if(avatarFile == null) {

            BufferedImage buffImg = ImageIO.read(new File("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/guest-icon.png"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", os);
            return os.toByteArray();
        }
        return avatarFile;
    }

    public void setAvatarFile(byte[] avatarFile) { this.avatarFile = avatarFile; }

    public RegisteredUser getUser() { return user; }

    public void setUser(RegisteredUser user) { this.user = user; }
}
