package ch.uzh.ifi.hase.soprafs21.entity;


import ch.uzh.ifi.hase.soprafs21.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.SchieberGamePutDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
public class SchieberGameSession implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Some attributes of a game session are always identical on
     * creation, e.g. both teams always start with 0 points achieved.
     * There are attributes that depend on the ingame-mode chosen
     * each round, e.g. the cards held by each player; for this
     * purpose we will leverage the SchieberGameSessionInitializeRoundDTO
     * which essentially will contain information for game entity update
     * upon initialization of a round.
     */
    public SchieberGameSession() {
        pointsTeam0_2 = 0;
        pointsTeam1_3 = 0;
        pointsInCurrentRoundTeam0_2 = 0;
        pointsInCurrentRoundTeam1_3 = 0;
        trickToPlay = 0;
        player0startsTrick = false;
        player1startsTrick = false;
        player2startsTrick = false;
        player3startsTrick = false;
        hasTrickStarted = false;
        cardsHeldByPlayer0 = new ArrayList<>();
        cardsHeldByPlayer1 = new ArrayList<>();
        cardsHeldByPlayer2 = new ArrayList<>();
        cardsHeldByPlayer3 = new ArrayList<>();
        Deck.initializePlayerCards(this);
    }




    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_0_id", updatable = false)
    private User user0;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_1_id", updatable = false)
    private User user1;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_2_id", updatable = false)
    private User user2;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_3_id", updatable = false)
    private User user3;

    @Column(nullable = false)
    private Integer pointsToWin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rank startingCardRank;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Suit startingCardSuit;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "schieber_game_ingame_modes",
            joinColumns = @JoinColumn(
                    name = "game_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "INGAME_MODES_FK_CONSTRAINT",
                            foreignKeyDefinition = "FOREIGN KEY (game_id) references public.games (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE"
                    )
            )
    )
    @AttributeOverrides({
            @AttributeOverride(name = "ingameMode", column = @Column(name = "ingameModeName")),
            @AttributeOverride(name = "multiplicator", column = @Column(name = "multiplicator"))
    })
    @OrderColumn
    @Enumerated(EnumType.STRING)
    private List<IngameModeMultiplicatorObject> ingameModes;

    @Column(nullable = false)
    private Boolean weisAllowed;

    @Column(nullable = false)
    private Boolean crossWeisAllowed;

    @Column
    private String weisAsk;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "group_id", updatable = false)
    private Group group;


/*
    Consistent/Immutable parts of the state above
    ---------------------------------------------------------------------------------------
    Inconsistent/Mutable parts of the state below (i.e. state that will change when game goes on)
*/


    @Column(nullable = false)
    private Integer pointsTeam0_2;

    @Column(nullable = false)
    private Integer pointsTeam1_3;

    @Column(nullable = false)
    private Integer pointsInCurrentRoundTeam0_2;

    @Column(nullable = false)
    private Integer pointsInCurrentRoundTeam1_3;

    @ElementCollection
    @CollectionTable(
            name = "schieber_game_held_cards_by_0",
            joinColumns = @JoinColumn(
                    name = "game_id",
                    referencedColumnName = "id"
            )
    )
    @Enumerated(EnumType.STRING)
    private List<Card> cardsHeldByPlayer0;

    @ElementCollection
    @CollectionTable(
            name = "schieber_game_held_cards_by_1",
            joinColumns = @JoinColumn(
                    name = "game_id",
                    referencedColumnName = "id"
            )
    )
    @Enumerated(EnumType.STRING)
    private List<Card> cardsHeldByPlayer1;

    @ElementCollection
    @CollectionTable(
            name = "schieber_game_held_cards_by_2",
            joinColumns = @JoinColumn(
                    name = "game_id",
                    referencedColumnName = "id"
            )
    )
    @Enumerated(EnumType.STRING)
    private List<Card> cardsHeldByPlayer2;

    @ElementCollection
    @CollectionTable(
            name = "schieber_game_held_cards_by_3",
            joinColumns = @JoinColumn(
                    name = "game_id",
                    referencedColumnName = "id"
            )
    )
    @Enumerated(EnumType.STRING)
    private List<Card> cardsHeldByPlayer3;

    @Column
    private Integer trickToPlay;

    @Column
    private Boolean player0startsTrick;

    @Column
    private Boolean player1startsTrick;

    @Column
    private Boolean player2startsTrick;

    @Column
    private Boolean player3startsTrick;

    @Embedded
    @AttributeOverride(name = "suit", column = @Column(name = "suit_played_by_0"))
    @AttributeOverride(name = "rank", column = @Column(name = "rank_played_by_0"))
    @AttributeOverride(name = "isTrumpf", column = @Column(name = "trump_played_by_0"))
    private Card cardPlayedByPlayer0;

    @Embedded
    @AttributeOverride(name = "suit", column = @Column(name = "suit_played_by_1"))
    @AttributeOverride(name = "rank", column = @Column(name = "rank_played_by_1"))
    @AttributeOverride(name = "isTrumpf", column = @Column(name = "trump_played_by_1"))
    private Card cardPlayedByPlayer1;

    @Embedded
    @AttributeOverride(name = "suit", column = @Column(name = "suit_played_by_2"))
    @AttributeOverride(name = "rank", column = @Column(name = "rank_played_by_2"))
    @AttributeOverride(name = "isTrumpf", column = @Column(name = "trump_played_by_2"))
    private Card cardPlayedByPlayer2;

    @Embedded
    @AttributeOverride(name = "suit", column = @Column(name = "suit_played_by_3"))
    @AttributeOverride(name = "rank", column = @Column(name = "rank_played_by_3"))
    @AttributeOverride(name = "isTrumpf", column = @Column(name = "trump_played_by_3"))
    private Card cardPlayedByPlayer3;

    @Column
    private Boolean hasTrickStarted;

    @Column
    @Enumerated(EnumType.STRING)
    private IngameMode currentIngameMode;

    @Column
    private UUID idOfRoundStartingPlayer;

    @Column
    private Roundstart startedLowOrHigh;



/*
    Helper methods ------------------------------------------------------------------------
*/
    public void setTrumpFlags(IngameMode choice) {
        if(choice == IngameMode.ACORN) {
            for(Card card : cardsHeldByPlayer0) if(card.getSuit() == Suit.ACORN) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer1) if(card.getSuit() == Suit.ACORN) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer2) if(card.getSuit() == Suit.ACORN) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer3) if(card.getSuit() == Suit.ACORN) card.setIsTrumpf(true);

        } else if(choice == IngameMode.ROSE) {
            for(Card card : cardsHeldByPlayer0) if(card.getSuit() == Suit.ROSE) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer1) if(card.getSuit() == Suit.ROSE) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer2) if(card.getSuit() == Suit.ROSE) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer3) if(card.getSuit() == Suit.ROSE) card.setIsTrumpf(true);

        } else if(choice == IngameMode.BELL) {
            for(Card card : cardsHeldByPlayer0) if(card.getSuit() == Suit.BELL) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer1) if(card.getSuit() == Suit.BELL) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer2) if(card.getSuit() == Suit.BELL) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer3) if(card.getSuit() == Suit.BELL) card.setIsTrumpf(true);

        } else if(choice == IngameMode.SHIELD) {
            for(Card card : cardsHeldByPlayer0) if(card.getSuit() == Suit.SHIELD) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer1) if(card.getSuit() == Suit.SHIELD) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer2) if(card.getSuit() == Suit.SHIELD) card.setIsTrumpf(true);
            for(Card card : cardsHeldByPlayer3) if(card.getSuit() == Suit.SHIELD) card.setIsTrumpf(true);
        }

        return;
    }

    public void setInitiallyStartingPlayer() {
        Card startingCard = new Card(startingCardSuit, startingCardRank);
        if(cardsHeldByPlayer0.contains(startingCard)) {
            player0startsTrick = true;
            idOfRoundStartingPlayer = user0.getId();
            return;
        }
        if(cardsHeldByPlayer1.contains(startingCard)) {
            player1startsTrick = true;
            idOfRoundStartingPlayer = user1.getId();
            return;
        }
        if(cardsHeldByPlayer2.contains(startingCard)) {
            player2startsTrick = true;
            idOfRoundStartingPlayer = user2.getId();
            return;
        }
        if(cardsHeldByPlayer3.contains(startingCard)) {
            player3startsTrick = true;
            idOfRoundStartingPlayer = user3.getId();
            return;
        }
    }

    public Boolean[] composePlayerStartsTrickArray() {
        Boolean[] arr = new Boolean[4];
        arr[0] = player0startsTrick;
        arr[1] = player1startsTrick;
        arr[2] = player2startsTrick;
        arr[3] = player3startsTrick;

        return arr;
    }

    public Card[] composeCardsPlayedArray() {
        Card[] arr = new Card[4];
        arr[0] = cardPlayedByPlayer0;
        arr[1] = cardPlayedByPlayer1;
        arr[2] = cardPlayedByPlayer2;
        arr[3] = cardPlayedByPlayer3;

        return arr;
    }

    public void updateCardsHeldAndPlayedCardOfPlayerWithId(UUID playerId, Card card) {
        if(playerId.equals(user0.getId())) {
            cardsHeldByPlayer0.remove(card);
            setCardPlayedByPlayer0(card);

        } else if(playerId.equals(user1.getId())) {
            cardsHeldByPlayer1.remove(card);
            setCardPlayedByPlayer1(card);

        } else if(playerId.equals(user2.getId())) {
            cardsHeldByPlayer2.remove(card);
            setCardPlayedByPlayer2(card);

        } else if(playerId.equals(user3.getId())) {
            cardsHeldByPlayer3.remove(card);
            setCardPlayedByPlayer3(card);
        }
    }

    public Boolean allCardsPlayedThisTrick() {
        return cardPlayedByPlayer0 != null &&
                cardPlayedByPlayer1 != null &&
                cardPlayedByPlayer2 != null &&
                cardPlayedByPlayer3 != null;
    }

    // ATTENTION: only call this method after the ones above have been invoked!
    public void assignPointsAndDetermineNextTrickStartingPlayerAccordingToCardsPlayedThisTrick(SchieberGamePutDTO putDTO) {

        // this will hold the amount of points after calling the Card.determineHighestCard method
        Integer[] pointsCollector = new Integer[1];
        pointsCollector[0] = 0;

        // determines the first card played this trick
        Card trickStartingCard = null;
        if(putDTO.getUserId().equals(user0.getId())) {
            trickStartingCard = cardPlayedByPlayer1;
        } else if(putDTO.getUserId().equals(user1.getId())) {
            trickStartingCard = cardPlayedByPlayer2;
        } else if(putDTO.getUserId().equals(user2.getId())) {
            trickStartingCard = cardPlayedByPlayer3;
        } else if(putDTO.getUserId().equals(user3.getId())) {
            trickStartingCard = cardPlayedByPlayer0;
        }

        Card highestCardPlayed = Card.determineHighestCard(
                currentIngameMode,
                startedLowOrHigh,
                trickToPlay,
                pointsCollector,
                trickStartingCard,
                cardPlayedByPlayer0,
                cardPlayedByPlayer1,
                cardPlayedByPlayer2,
                cardPlayedByPlayer3);

        // retrieve multiplicator of curentIngameMode
        int multiplicator = 0;
        for(IngameModeMultiplicatorObject obj : getIngameModes()) {
            if(obj.getIngameMode() == getCurrentIngameMode()) {
                multiplicator = obj.getMultiplicator();
            }
        }

        int achievedPoints = pointsCollector[0] * multiplicator;

        // set all players to start false
        player0startsTrick = false;
        player1startsTrick = false;
        player2startsTrick = false;
        player3startsTrick = false;

        // determine to which player the highestCardPlayer belongs to,
        // i.e. determine which player starts next trick and assign
        // points to that player's team
        if(cardPlayedByPlayer0.equals(highestCardPlayed)) {
            pointsInCurrentRoundTeam0_2 += achievedPoints;
            player0startsTrick = true;
        } else if(cardPlayedByPlayer1.equals(highestCardPlayed)) {
            pointsInCurrentRoundTeam1_3 += achievedPoints;
            player1startsTrick = true;
        } else if(cardPlayedByPlayer2.equals(highestCardPlayed)) {
            pointsInCurrentRoundTeam0_2 += achievedPoints;
            player2startsTrick = true;
        } else if(cardPlayedByPlayer3.equals(highestCardPlayed)) {
            pointsInCurrentRoundTeam1_3 += achievedPoints;
            player3startsTrick = true;
        }
    }

    public void achievePointsWithMatchBonus() {

        // retrieve multiplicator of currentIngameMode
        int multiplicator = 0;
        for(IngameModeMultiplicatorObject obj : getIngameModes()) {
            if(obj.getIngameMode() == getCurrentIngameMode()) {
                multiplicator = obj.getMultiplicator();
            }
        }

        // award match bonus if necessary
        if(pointsInCurrentRoundTeam0_2 == 157 * multiplicator) { pointsInCurrentRoundTeam0_2 += 100 * multiplicator; }
        if(pointsInCurrentRoundTeam1_3 == 157 * multiplicator) { pointsInCurrentRoundTeam1_3 += 100 * multiplicator; }

        // store accumulated points made this round
        pointsTeam0_2 += pointsInCurrentRoundTeam0_2;
        pointsTeam1_3 += pointsInCurrentRoundTeam1_3;

        // reset points accumulators for next round
        pointsInCurrentRoundTeam0_2 = 0;
        pointsInCurrentRoundTeam1_3 = 0;
    }

    public void updateIdOfPlayerWhoStartsNextRoundAndSetPlayerWhoStartsNextTrick() {
        player0startsTrick = false;
        player1startsTrick = false;
        player2startsTrick = false;
        player3startsTrick = false;

        if(user0.getId().equals(idOfRoundStartingPlayer)) {
            idOfRoundStartingPlayer = user0.getId();
            player0startsTrick = true;
        } else if(user1.getId().equals(idOfRoundStartingPlayer)) {
            idOfRoundStartingPlayer = user1.getId();
            player1startsTrick = true;
        } else if(user2.getId().equals(idOfRoundStartingPlayer)) {
            idOfRoundStartingPlayer = user2.getId();
            player2startsTrick = true;
        } else if(user3.getId().equals(idOfRoundStartingPlayer)) {
            idOfRoundStartingPlayer = user3.getId();
            player3startsTrick = true;
        }
    }





/*
    Getters and Setters ------------------------------------------------------------------
*/
    public User getUser0() {
        return user0;
    }

    public void setUser0(User user0) {
        this.user0 = user0;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getUser3() {
        return user3;
    }

    public void setUser3(User user3) {
        this.user3 = user3;
    }

    public Integer getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(Integer pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public Rank getStartingCardRank() {
        return startingCardRank;
    }

    public void setStartingCardRank(Rank startingCardRank) {
        this.startingCardRank = startingCardRank;
    }

    public Suit getStartingCardSuit() {
        return startingCardSuit;
    }

    public void setStartingCardSuit(Suit startingCardSuit) {
        this.startingCardSuit = startingCardSuit;
    }

    public List<IngameModeMultiplicatorObject> getIngameModes() {
        return ingameModes;
    }

    public void setIngameModes(List<IngameModeMultiplicatorObject> ingameModes) {
        this.ingameModes = ingameModes;
    }

    public Boolean getWeisAllowed() {
        return weisAllowed;
    }

    public void setWeisAllowed(Boolean weisAllowed) {
        this.weisAllowed = weisAllowed;
    }

    public Boolean getCrossWeisAllowed() {
        return crossWeisAllowed;
    }

    public void setCrossWeisAllowed(Boolean crossWeisAllowed) {
        this.crossWeisAllowed = crossWeisAllowed;
    }

    public String getWeisAsk() {
        return weisAsk;
    }

    public void setWeisAsk(String weisAsk) {
        this.weisAsk = weisAsk;
    }

    public Integer getPointsTeam0_2() {
        return pointsTeam0_2;
    }

    public void setPointsTeam0_2(Integer pointsTeam0_2) {
        this.pointsTeam0_2 = pointsTeam0_2;
    }

    public Integer getPointsTeam1_3() {
        return pointsTeam1_3;
    }

    public void setPointsTeam1_3(Integer pointsTeam1_3) {
        this.pointsTeam1_3 = pointsTeam1_3;
    }

    public List<Card> getCardsHeldByPlayer0() {
        return cardsHeldByPlayer0;
    }

    public void setCardsHeldByPlayer0(List<Card> cardsHeldByPlayer0) {
        this.cardsHeldByPlayer0 = cardsHeldByPlayer0;
    }

    public List<Card> getCardsHeldByPlayer1() {
        return cardsHeldByPlayer1;
    }

    public void setCardsHeldByPlayer1(List<Card> cardsHeldByPlayer1) {
        this.cardsHeldByPlayer1 = cardsHeldByPlayer1;
    }

    public List<Card> getCardsHeldByPlayer2() {
        return cardsHeldByPlayer2;
    }

    public void setCardsHeldByPlayer2(List<Card> cardsHeldByPlayer2) {
        this.cardsHeldByPlayer2 = cardsHeldByPlayer2;
    }

    public List<Card> getCardsHeldByPlayer3() {
        return cardsHeldByPlayer3;
    }

    public void setCardsHeldByPlayer3(List<Card> cardsHeldByPlayer3) {
        this.cardsHeldByPlayer3 = cardsHeldByPlayer3;
    }

    public Integer getTrickToPlay() {
        return trickToPlay;
    }

    public void setTrickToPlay(Integer trickToPlay) {
        this.trickToPlay = trickToPlay;
    }

    public Boolean getPlayer0startsTrick() {
        return player0startsTrick;
    }

    public void setPlayer0startsTrick(Boolean player0startsTrick) {
        this.player0startsTrick = player0startsTrick;
    }

    public Boolean getPlayer1startsTrick() {
        return player1startsTrick;
    }

    public void setPlayer1startsTrick(Boolean player1startsTrick) {
        this.player1startsTrick = player1startsTrick;
    }

    public Boolean getPlayer2startsTrick() {
        return player2startsTrick;
    }

    public void setPlayer2startsTrick(Boolean player2startsTrick) {
        this.player2startsTrick = player2startsTrick;
    }

    public Boolean getPlayer3startsTrick() {
        return player3startsTrick;
    }

    public void setPlayer3startsTrick(Boolean player3startsTrick) {
        this.player3startsTrick = player3startsTrick;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Card getCardPlayedByPlayer0() {
        return cardPlayedByPlayer0;
    }

    public void setCardPlayedByPlayer0(Card cardPlayedByPlayer0) {
        this.cardPlayedByPlayer0 = cardPlayedByPlayer0;
    }

    public Card getCardPlayedByPlayer1() {
        return cardPlayedByPlayer1;
    }

    public void setCardPlayedByPlayer1(Card cardPlayedByPlayer1) {
        this.cardPlayedByPlayer1 = cardPlayedByPlayer1;
    }

    public Card getCardPlayedByPlayer2() {
        return cardPlayedByPlayer2;
    }

    public void setCardPlayedByPlayer2(Card cardPlayedByPlayer2) {
        this.cardPlayedByPlayer2 = cardPlayedByPlayer2;
    }

    public Card getCardPlayedByPlayer3() {
        return cardPlayedByPlayer3;
    }

    public void setCardPlayedByPlayer3(Card cardPlayedByPlayer3) {
        this.cardPlayedByPlayer3 = cardPlayedByPlayer3;
    }

    public Boolean getHasTrickStarted() {
        return hasTrickStarted;
    }

    public void setHasTrickStarted(Boolean hasRoundStarted) {
        this.hasTrickStarted = hasRoundStarted;
    }

    public IngameMode getCurrentIngameMode() {
        return currentIngameMode;
    }

    public void setCurrentIngameMode(IngameMode currentIngameMode) {
        this.currentIngameMode = currentIngameMode;
    }

    public UUID getIdOfRoundStartingPlayer() {
        return idOfRoundStartingPlayer;
    }

    public void setIdOfRoundStartingPlayer(UUID idOfRoundStartingPlayer) {
        this.idOfRoundStartingPlayer = idOfRoundStartingPlayer;
    }

    public Roundstart getStartedLowOrHigh() {
        return startedLowOrHigh;
    }

    public void setStartedLowOrHigh(Roundstart startedLowOrHigh) {
        this.startedLowOrHigh = startedLowOrHigh;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getPointsInCurrentRoundTeam0_2() {
        return pointsInCurrentRoundTeam0_2;
    }

    public void setPointsInCurrentRoundTeam0_2(Integer pointsInCurrentRoundTeam0_2) {
        this.pointsInCurrentRoundTeam0_2 = pointsInCurrentRoundTeam0_2;
    }

    public Integer getPointsInCurrentRoundTeam1_3() {
        return pointsInCurrentRoundTeam1_3;
    }

    public void setPointsInCurrentRoundTeam1_3(Integer pointsInCurrentRoundTeam1_3) {
        this.pointsInCurrentRoundTeam1_3 = pointsInCurrentRoundTeam1_3;
    }
}
