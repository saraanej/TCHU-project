package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.GraphicalPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.epfl.tchu.game.Card.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {
    @Test
    void cardValuesAreDefinedInTheRightOrder() {
        var expectedValues = new Card[]{
                BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE, LOCOMOTIVE
        };
        assertArrayEquals(expectedValues, Card.values());

        GraphicalPlayer.CardBagStringConverter c = new GraphicalPlayer.CardBagStringConverter();
        SortedBag<Card> b = SortedBag.of(1, Card.VIOLET, 3, Card.RED);
        assertEquals("1 violette et 3 rouges", c.toString(b));
    }

    @Test
    void cardAllIsDefinedCorrectly() {
        assertEquals(List.of(Card.values()), ALL);
    }

    @Test
    void cardCountIsDefinedCorrectly() {
        assertEquals(9, COUNT);
    }

    @Test
    void cardOfWorksForAllColors() {
        var allCards = Card.values();
        for (var color : Color.values())
            assertEquals(allCards[color.ordinal()], Card.of(color));
    }

    @Test
    void cardColorWorksForAllColors() {
        var allColors = Color.values();
        for (var card : values()) {
            if (card != LOCOMOTIVE)
                assertEquals(allColors[card.ordinal()], card.color());
        }
    }
}