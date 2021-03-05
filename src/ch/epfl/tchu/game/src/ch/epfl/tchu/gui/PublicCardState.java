package ch.epfl.tchu.game.src.ch.epfl.tchu.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.Card;

/**
 * Modelizes the public state of the wagon/locomotive cards
 * that the player doesn't possess
 * 
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public class PublicCardState { 

	
	private final List<Card> faceUpCards;
	private final int deckSize;
	private final int discardsSize;
	
	/**
	 * default constructor for the card's state
	 * 
	 * @param (List<Card>) faceUpCards : the list of the visible cards
	 * @param (int) deckSize : the deck's size
	 * @param (int) discardsSize : the discard pile's size
	 * 
	 * @throws IllegalArgumentException 
	 *                if the list of the visible cards doesn't contain exactly three cards
	 *                if the sizes of the deck or the discard pile are negative                 
	 */
	public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
		Preconditions.checkArgument(faceUpCards.size() == 5 
				                   && deckSize > 0 
				                   && discardsSize > 0);
		
		this.faceUpCards = Collections.unmodifiableList(new ArrayList<>(faceUpCards));
		this.deckSize = deckSize;
		this.discardsSize = discardsSize;
		
	}
	
	/**
	 * 
	 * @return (int) the number of cards that the players don't possess
	 */
	public int totalSize() {
		return deckSize + discardsSize + faceUpCards().size();
	}
	
	
	/**
	 * 
	 * @return (List<Card>) the five visible cards in a list containing five elements
	 */
	public List<Card> faceUpCards(){		
		return Collections.unmodifiableList(new ArrayList<>(faceUpCards));
	}
	
	
	/**
	 * 
	 * @param (int) slot : index of the visible card 
	 * 
	 * @throws IndexOutOfBoundsException 
	 *             if slot is less than 0 or bigger or equal than 5
	 * 
	 * @return (Card) the visible card in the slot-th position
	 */
	public Card faceUpCard(int slot) {
		return faceUpCards().get(Objects.checkIndex(slot,5));
	}
	
	/**
	 * 
	 * @return (int) the deck's size
	 */
	public int deckSize() {
		return deckSize;
	}
	
	
	/** 
	 * 
	 * @return (boolean) true if the deck is empty. false if not.
	 */
	public boolean isDeckEmpty() {
		return deckSize() == 0 ? true : false;
	}
	
	
	/**
	 * 
	 * @return (int) the discard pile's size
	 */
	public int discardsSize() {
		return discardsSize;
	}
	
	
}
