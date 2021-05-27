package ch.uzh.ifi.hase.soprafs21.constant;

public enum UserType {
    REGISTERED("RegisteredUser"),
    GUEST("GuestUser");

    private final String type;

    UserType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() { return type; }

}
