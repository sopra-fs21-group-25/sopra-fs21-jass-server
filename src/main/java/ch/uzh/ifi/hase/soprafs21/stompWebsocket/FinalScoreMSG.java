package ch.uzh.ifi.hase.soprafs21.stompWebsocket;

public class FinalScoreMSG {

    private Integer scoreTeam0_2;
    private Integer scoreTeam1_3;

    public FinalScoreMSG(Integer sc0_2, Integer sc1_3) {
        scoreTeam0_2 = sc0_2;
        scoreTeam1_3 = sc1_3;
    }

    public FinalScoreMSG() {}

    public Integer getScoreTeam0_2() {
        return scoreTeam0_2;
    }

    public void setScoreTeam0_2(Integer scoreTeam0_2) {
        this.scoreTeam0_2 = scoreTeam0_2;
    }

    public Integer getScoreTeam1_3() {
        return scoreTeam1_3;
    }

    public void setScoreTeam1_3(Integer scoreTeam1_3) {
        this.scoreTeam1_3 = scoreTeam1_3;
    }
}
