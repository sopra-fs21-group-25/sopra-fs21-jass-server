package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Entity
@DiscriminatorValue("GuestUser")
@SuppressWarnings("unchecked")
public class GuestUser extends User {

    public GuestUser() {
        this.userType = UserType.GUEST;
    }

    public void setFunnyUsername() throws IOException {
        InputStream animalJson = new FileInputStream("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/animals.json");
        List<HashMap<String,String>> animalMap = new ObjectMapper().readValue(animalJson, List.class);

        InputStream adjectivesJson = new FileInputStream("src/main/java/ch/uzh/ifi/hase/soprafs21/assets/adjectives.json");
        List<HashMap<String,String>> adjectivesMap = new ObjectMapper().readValue(adjectivesJson, List.class);

        var randomKeyAdjectives = new Random(System.currentTimeMillis()).nextInt(adjectivesMap.size());
        var randomKeyAnimals = new Random(System.currentTimeMillis()).nextInt(animalMap.size());

        var adjective = adjectivesMap.get(randomKeyAdjectives).get("Value");
        var animal = animalMap.get(randomKeyAnimals).get("Value");

        var funnyName = adjective + " " + animal;

        this.setUsername(funnyName);
    }

}
