package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.*;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class CardStateTest {

    private static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };

   private  List<Card> cards = List.of(Card.YELLOW,
            Card.BLUE,
            Card.BLUE,
            Card.ORANGE,
            Card.WHITE,
            Card.LOCOMOTIVE,
            Card.BLACK,
            Card.BLUE,
            Card.GREEN);
    private Deck<Card> Deck9 = Deck.of(SortedBag.of(cards),NON_RANDOM);
    private CardState state = CardState.of(Deck9);


   // private static Random RANDOM = new Random(6);

    private List<Card> l = List.of();
    private Deck<Card> emptyDeck = Deck.of(SortedBag.of(l),NON_RANDOM);


    @Test
    public void ofWorks(){
        assertEquals(4,state.deckSize());
        assertEquals(9, state.totalSize());
        assertEquals(5, state.faceUpCards().size());
        assertTrue(state.discardsSize()==0);
    }
/*
    @Test
    public void withDrawnFaceUpWorks(){
        Card topCard = state.deck().topCard();
        Deck Deck8 = state.deck().withoutTopCard();
        assertEquals(3,state.withDrawnFaceUpCard(4).deckSize());
        assertEquals(Deck8.cards(),state.withDrawnFaceUpCard(3).deck().cards());
        assertEquals(topCard,state.withDrawnFaceUpCard(2).faceUpCard(2));
    }


    @Test
    public void topDeckWorks(){
        List<Card> Cards = List.of(Card.YELLOW,
                Card.BLUE,
                Card.RED,
                Card.ORANGE,
                Card.WHITE,
                Card.ORANGE,
                Card.BLACK);
        Deck<Card> deck = Deck.of(SortedBag.of(Cards),NON_RANDOM);
        System.out.println(deck.cards());

        Card topCard = deck.cards().get(5);
        System.out.println(deck.cards());
        CardState stateCard = CardState.of(deck);
        System.out.println(stateCard.deck().cards());
        assertEquals(topCard, stateCard.topDeckCard());

    }

    @Test
    public void withoutTopDeckWorks() {
        assertEquals(3, state.withoutTopDeckCard().deck().size());
    }*/

    @Test
    public void withDeckRecreatedFromDiscardsDiscardEmpty(){
        CardState stateCard = state.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();

        assertTrue(stateCard.withDeckRecreatedFromDiscards(NON_RANDOM).discardsSize() == 0);
    }

    @Test
    public void WithDeckRecreatedFromDiscardsWorks(){
        CardState stateCard = state.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();
        stateCard= stateCard.withoutTopDeckCard();
        stateCard = stateCard.withMoreDiscardedCards(SortedBag.of(cards));
        stateCard = stateCard.withDeckRecreatedFromDiscards(NON_RANDOM);
        assertEquals(cards.size(),stateCard.deckSize());
        assertTrue(stateCard.discardsSize() == 0);
    }

    @Test
    public void withMoreDiscardedCards(){
        CardState stateCard = state.withMoreDiscardedCards(SortedBag.of(cards));
        assertEquals( cards.size(), stateCard.discardsSize());
        stateCard = stateCard.withMoreDiscardedCards(SortedBag.of(cards));
        assertEquals(2*cards.size(), stateCard.discardsSize());
    }

    @Test
    public void assertThrowsIleggOf(){

        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(emptyDeck);
        });
    }

    @Test
    public void assertThrowsDrawnFaceUpCards(){
       // CardState emptyState = CardState.of(emptyDeck);
       // CardState state = CardState.of(Deck9);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            state.withDrawnFaceUpCard(6);
            state.withDrawnFaceUpCard(-1);
            state.withDrawnFaceUpCard(5);
        });
    }

    @Test
    public void recreateddECKThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            state.withDeckRecreatedFromDiscards(NON_RANDOM);
        });
    }


	

}
