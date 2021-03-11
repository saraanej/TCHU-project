package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;

import java.util.List;

public class DeckTest {

    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };
    
    public static Random RANDOM = new Random(6);
/*
    @Test
    public void ofWorksOnNormalList(){
        List<Integer> l = List.of(0,1,2);
        Deck<Integer> deck = Deck.of(SortedBag.of(l),NON_RANDOM);
        System.out.println(deck.cards());
        assertEquals(l.get(0),Deck.of(SortedBag.of(l),NON_RANDOM).topCard());
        assertEquals(l.size(),Deck.of(SortedBag.of(l),NON_RANDOM).size());

    }
    */
    @Test
    public void ofshuffles(){
        List<Integer> l = List.of(0,1,2);
        assertNotEquals(l.get(1), Deck.of(SortedBag.of(l),RANDOM).topCard());

    }
    
    @Test
    public void topCardsWorks(){
        List<Integer> l = List.of(0,1,2,3,4,5);
        assertEquals(SortedBag.of(l.subList(0,5)),Deck.of(SortedBag.of(l),NON_RANDOM).topCards(5));

    }
    
    @Test
    public void WithouttopCardsWorks(){
        List<Integer> l = List.of(0,1,2,3,4,5);
        assertEquals(SortedBag.of(l.subList(3,6)),Deck.of(SortedBag.of(l),NON_RANDOM).withoutTopCards(3).topCards(3));

    }

    @Test
    public void WithouttopCardWorks(){
        List<Integer> l = List.of(0,1,2,3,4,5);
       // assertEquals(,Deck.of(SortedBag.of(l),NON_RANDOM).withoutTopCard().topCard());

    }
    
    @Test
    public void withoutTopCardsWorks(){
        List<Integer> l = List.of(0,1,2,3,4,5);
        assertEquals(SortedBag.of(l.subList(1,6)),Deck.of(SortedBag.of(l),NON_RANDOM).withoutTopCard().topCards(5));

    }
    
    @Test
    public void isEmptyWorks(){
        List<Integer> l = List.of();
        List<Integer> l1 = List.of(0,1,2,3,4,5);

        assertFalse(Deck.of(SortedBag.of(l1),NON_RANDOM).isEmpty());

        assertTrue(Deck.of(SortedBag.of(l),NON_RANDOM).isEmpty());
    }
    
    @Test
    public void assertTopCardThrowsIllegaArg() {
        List<Integer> l = List.of();
    	Deck<Integer> empty = Deck.of(SortedBag.of(l), NON_RANDOM);
    	assertThrows(IllegalArgumentException.class, () -> {
    		empty.topCard();}
    	);
    }
    
    @Test
    public void assertWithoutTopCardThrowsIllegaArg() {
        List<Integer> l = List.of();
    	Deck<Integer> empty = Deck.of(SortedBag.of(l), NON_RANDOM);
    	assertThrows(IllegalArgumentException.class, () -> {
    		empty.withoutTopCard();}
    	);
    }
    
    @Test
    public void assertTopCardsThrowsIllegaArg() {
        List<Integer> l = List.of();
        List<Integer> l1 = List.of(1,2,3,4,5);

    	Deck<Integer> empty = Deck.of(SortedBag.of(l), NON_RANDOM);
    	Deck<Integer> notEmpty = Deck.of(SortedBag.of(l1), NON_RANDOM);

    	assertThrows(IllegalArgumentException.class, () -> {
    		empty.topCards(3);}
    	);
    	assertThrows(IllegalArgumentException.class, () -> {
    		notEmpty.topCards(-1);}
    	);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		notEmpty.topCards(8);}
    	);
    }
    
    @Test
    public void assertWithoutTopCardsThrowsIllegalArg() {
        List<Integer> l = List.of();
        List<Integer> l1 = List.of(1,2,3,4,5);

    	Deck<Integer> empty = Deck.of(SortedBag.of(l), NON_RANDOM);
    	Deck<Integer> notEmpty = Deck.of(SortedBag.of(l1), NON_RANDOM);

    	assertThrows(IllegalArgumentException.class, () -> {
    		empty.withoutTopCards(3);}
    	);
    	assertThrows(IllegalArgumentException.class, () -> {
    		notEmpty.withoutTopCards(-1);}
    	);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		notEmpty.withoutTopCards(8);}
    	);
    }

    @Test
    public void DeckWorksOnGivenExample(){
        SortedBag<String> cards =
                SortedBag.of(2, "as de pique", 3, "dame de cœur");
        Deck<String> deck = Deck.of(cards, NON_RANDOM);
       // System.out.println(deck.cards());

        while (!deck.isEmpty()) {
            String topCard = deck.topCard();
            //System.out.println(topCard);
            deck = deck.withoutTopCard();
            //System.out.println(deck.cards());
            //assertEquals(cards.get(n),topCard);
            }
    }
}




























