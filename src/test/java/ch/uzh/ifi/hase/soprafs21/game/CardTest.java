package ch.uzh.ifi.hase.soprafs21.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void determineHighestCard_noTrump_success_test() {
            Card card1 = new Card(Suit.ACORN, Rank.ACE);
            Card card2 = new Card(Suit.ACORN, Rank.KING);
            Card card3 = new Card(Suit.ACORN, Rank.OBER);
            Card card4 = new Card(Suit.ACORN, Rank.UNDER);
            Integer[] pointsCollector = new Integer[]{0};
            Card winningCard = Card.determineHighestCard(IngameMode.ACORN, Roundstart.OBE, 4, pointsCollector, card1, card2, card3, card4);

            //then
            assertEquals(card1, winningCard);
    }
    @Test
    public void determinehighestCard_Trumpbeatsothersuit_success_test() {
        Card card5 = new Card(Suit.SHIELD, Rank.ACE);
        Card card1 = new Card(Suit.ACORN, Rank.ACE);
        Card card2 = new Card(Suit.ACORN, Rank.KING);
        Card card3 = new Card(Suit.ACORN, Rank.OBER);
        Card card4 = new Card(Suit.ACORN, Rank.UNDER);
        card5.setTrumpf();
        Integer[] pointsCollector = new Integer[]{0};
        Card winningCard = Card.determineHighestCard(IngameMode.SHIELD, Roundstart.OBE, 4, pointsCollector, card1, card2, card3, card4, card5);
        //then
        assertEquals(card5, winningCard);
    }

    @Test
    public void determinehighestCard_UnderbeatsNill_success_test() {
        Card card1 = new Card(Suit.ROSE, Rank.ACE);
        Card card2 = new Card(Suit.ROSE, Rank.KING);
        Card card3 = new Card(Suit.ROSE, Rank.NINE);
        Card card4 = new Card(Suit.ROSE, Rank.UNDER);
        card1.setTrumpf();
        card2.setTrumpf();
        card3.setTrumpf();
        card4.setTrumpf();
        Integer[] pointsCollector = new Integer[]{0};
        Card winningCard = Card.determineHighestCard(IngameMode.ROSE, Roundstart.OBE, 4, pointsCollector, card1, card2, card3, card4);
        //then
        assertEquals(card4, winningCard);
    }


    @Test
    public void determinehighestCard_returnsfirst_fails() {
        Card card1 = new Card(Suit.ROSE, Rank.ACE);
        Card card2 = new Card(Suit.ROSE, Rank.ACE);
        Integer[] pointsCollector = new Integer[]{0};
        Card winningCard = Card.determineHighestCard(IngameMode.ROSE, Roundstart.OBE, 4, pointsCollector, card1, card2);
        //then
        assertEquals(card1, winningCard);
    }

    @Test
    void determineHighestCard_Obenabe_success_test() {
        Card card1 = new Card(Suit.ACORN, Rank.ACE);
        Card card2 = new Card(Suit.ACORN, Rank.KING);
        Card card3 = new Card(Suit.ACORN, Rank.OBER);
        Card card4 = new Card(Suit.ACORN, Rank.UNDER);
        Integer[] pointsCollector = new Integer[]{0};
        Card winningCard = Card.determineHighestCard(IngameMode.OBENABE, Roundstart.OBE, 4, pointsCollector, card1, card2, card3, card4);
        //then
        assertEquals(card1, winningCard);
    }

    @Test
    void determineHighestCard_Undenufe_success_test() {
        Card card1 = new Card(Suit.ACORN, Rank.ACE);
        Card card2 = new Card(Suit.ACORN, Rank.KING);
        Card card3 = new Card(Suit.ACORN, Rank.OBER);
        Card card4 = new Card(Suit.ACORN, Rank.SIX);
        Integer[] pointsCollector = new Integer[]{0};
        Card winningCard = Card.determineHighestCard(IngameMode.UNDENUFE, Roundstart.OBE, 4, pointsCollector, card1, card2, card3, card4);
        //then
        assertEquals(card4, winningCard);
    }


}