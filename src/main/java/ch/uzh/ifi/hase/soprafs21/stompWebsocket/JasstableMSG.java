package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

public class JasstableMSG {

    private String playerTop;
    private String playerBottom;
    private String playerLeft;
    private String playerRight;

    public JasstableMSG(String top, String bot, String left, String right) {
        this.playerTop = top;
        this.playerBottom = bot;
        this.playerLeft = left;
        this.playerRight = right;
    }

    public JasstableMSG() {}

    public String getPlayerTop() { return playerTop; }
    public String getPlayerBottom() { return playerBottom; }
    public String getPlayerLeft() { return playerLeft; }
    public String getPlayerRight() { return playerRight; }

}
