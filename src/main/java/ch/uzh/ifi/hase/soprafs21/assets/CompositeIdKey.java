package ch.uzh.ifi.hase.soprafs21.assets;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("unused")
public class CompositeIdKey implements Serializable {
    private UUID fromUser;
    private UUID toUser;
}
