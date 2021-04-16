package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * The public and immutable PublicCardState class represents (a part of) the state of the wagon/locomotive cards that are not in the hands of the players.
 * By "public part of a state" is meant the part of this state which is known to all the players.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class PublicCardState {
	
	private final List<Card> faceUpCards;
	private final int deckSize;
	private final int discardsSize;

	
	/**
	 * Default constructor for the card's state.
	 * @param (List<Card>) faceUpCards : The visible cards.
	 * @param (int) deckSize : The deck's size.
	 * @param (int) discardsSize : The discard pile's size.
	 * @throws IllegalArgumentException 
	 *                if the list of the visible cards doesn't contain exactly five cards,
	 *                if the sizes of the deck or the discard pile are negative.
	 */
	public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
		Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT);
		Preconditions.checkArgument(deckSize >= 0);
		Preconditions.checkArgument(discardsSize >= 0);
		this.faceUpCards = List.copyOf(faceUpCards);
		this.deckSize = deckSize;
		this.discardsSize = discardsSize;
	}


	/**
	 * @return (boolean) true if the deck is empty. false if not.
	 */
	public boolean isDeckEmpty() {
		return deckSize == 0 ? true : false;
	}

	/**
	 * @return (int) The deck's size.
	 */
	public int deckSize() {
		return deckSize;
	}

	/**
	 * @return (int) The discard pile's size.
	 */
	public int discardsSize() {
		return discardsSize;
	}

	/**
	 * @return (int) The number of cards that the players don't possess.
	 */
	public int totalSize() {
		return deckSize + discardsSize + faceUpCards.size();
	}

	/**
	 * @return (List<Card>) The five visible cards in a list containing five elements.
	 */
	public List<Card> faceUpCards(){
		return faceUpCards;
	}

	/**
	 * @param (int) slot : Index of the visible card.
	 * @return (Card) The visible card in the slot-th position.
	 * @throws IndexOutOfBoundsException
	             if slot is less than 0 or bigger or equal than 5.
	 */
	public Card faceUpCard(int slot) {
		return faceUpCards().get(Objects.checkIndex(slot,5));
	}
}