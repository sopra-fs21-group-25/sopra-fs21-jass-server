package ch.uzh.ifi.hase.soprafs21.game;

import javax.persistence.Embeddable;

@Embeddable
public class IngameModeMultiplicatorObject {
    private IngameMode ingameMode;
    private Integer multiplicator;

    public IngameModeMultiplicatorObject(IngameMode ingameMode, Integer multiplicator) {
        this.ingameMode = ingameMode;
        this.multiplicator = multiplicator;
    }

    public IngameModeMultiplicatorObject() {}


    public IngameMode getIngameMode() { return ingameMode; }
    public void setIngameMode(IngameMode ingameMode) { this.ingameMode = ingameMode; }
    public Integer getMultiplicator() { return multiplicator; }
    public void setMultiplicator(Integer multiplicator) { this.multiplicator = multiplicator; }
}
