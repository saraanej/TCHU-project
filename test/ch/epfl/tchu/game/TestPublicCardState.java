package ch.epfl.tchu.game;


import org.junit.Test;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPublicCardState {

    @Test
    public void constructorWithNegativeValues(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(cards,3 ,-5);});

    }


    @Test
    public void constructorWithWrongCardsSize(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.LOCOMOTIVE);

        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(cards,3 ,5);});

    }

    @Test
    public void TestTotalSize(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,9,5);

        assertEquals(19, p.totalSize());

    }

    @Test
    public void TestFaceUpCards(){

        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,9,5);

        //A FAIRE
        assertEquals(cards,p.faceUpCards());
    }

    @Test
    public void TestFaceUpCardsSlot(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,9,5);

        assertEquals(Card.LOCOMOTIVE, p.faceUpCard(4));
    }

    @Test
    public void TestFaceUpCardsSlotWithException(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,9,5);

        assertThrows(IndexOutOfBoundsException.class, () -> {p.faceUpCard(5);});
        assertThrows(IndexOutOfBoundsException.class, () -> {p.faceUpCard(-1);});
        assertThrows(IndexOutOfBoundsException.class, () -> {p.faceUpCard(8);});
    }

    @Test
    public void TestDeckSize(){
        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,10,5);

        assertEquals(10, p.deckSize());
    }

    @Test
    public void TestIsEmpty(){

        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,0,5);
        assertTrue(p.isDeckEmpty());

        //PublicCardState p = new PublicCardState(cards,2,5);
        //assertTrue(p.isDeckEmpty());

    }

    @Test
    public void TestDiscardsSize(){

        List<Card> cards = new ArrayList<>();

        cards.add(Card.YELLOW);
        cards.add(Card.BLUE);
        cards.add(Card.WHITE);
        cards.add(Card.WHITE);
        cards.add(Card.LOCOMOTIVE);

        PublicCardState p = new PublicCardState(cards,0,280);

        assertEquals(280, p.discardsSize());
    }
}