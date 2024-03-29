package ch.epfl.tchu.game;

import java.util.List;
import java.util.Objects;
import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import ch.epfl.tchu.Preconditions;

/**
 * The public and immutable PublicCardState class represents (a part of) the state
 * of the wagon/locomotive cards that are not in the hands of the players.
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
	 * @param faceUpCards The visible cards.
	 * @param deckSize The deck's size.
	 * @param discardsSize The discard pile's size.
	 * @throws IllegalArgumentException 
	 *                if the list of the visible cards doesn't contain exactly five cards,
	 *                if the sizes of the deck or the discard pile are negative.
	 */
	public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
		Preconditions.checkArgument(faceUpCards.size() == FACE_UP_CARDS_COUNT);
		Preconditions.checkArgument(deckSize >= 0);
		Preconditions.checkArgument(discardsSize >= 0);
		this.faceUpCards = List.copyOf(faceUpCards);
		this.deckSize = deckSize;
		this.discardsSize = discardsSize;
	}


	/**
	 * @return true if the deck is empty. false if not.
	 */
	public boolean isDeckEmpty() {
		return deckSize == 0;
	}

	/**
	 * @return The deck's size.
	 */
	public int deckSize() {
		return deckSize;
	}

	/**
	 * @return The discard pile's size.
	 */
	public int discardsSize() {
		return discardsSize;
	}

	/**
	 * @return The five visible cards in a list containing five elements.
	 */
	public List<Card> faceUpCards(){
		return faceUpCards;
	}

	/**
	 * @param slot Index of the visible card.
	 * @return The visible card in the slot-th position.
	 * @throws IndexOutOfBoundsException
	             if slot is less than 0 or bigger or equal than 5.
	 */
	public Card faceUpCard(int slot) {
		return faceUpCards().get(Objects.checkIndex(slot,FACE_UP_CARDS_COUNT));
	}
}