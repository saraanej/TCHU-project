package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Random;

/**
 * Modelizes the private state of the wagon/locomotive cards
 * that the player doesn't possess
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class CardState extends PublicCardState {

	private final Deck<Card> deck;
	private final 


	/**
	 * default constructor for the card's state
	 * @param faceUpCards
	 * @param deckSize
	 * @param discardsSize
	 * @throws IllegalArgumentException
	             if the list of the visible cards doesn't contain exactly three cards
	             if the sizes of the deck or the discard pile are negative
	 */
	private CardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
		super(faceUpCards, deckSize, discardsSize);
	}

	/**
	 *
	 * @param deck (Deck<Card>)
	 * @return (CardState)
	 */
	public static CardState of(Deck<Card> deck) {
		Preconditions.checkArgument(deck.size()>=5);
		return new CardState(deck.topCards(5).toList(), deck.size()-5,0);
	}

	/**
	 *
	 * @param slot (int)
	 * @return (CardState)
	 */
	public CardState withDrawnFaceUpCard(int slot){
		return null;
	}

	/**
	 *
	 * @return (Card)
	 */
	public Card topDeckCard(){
		return null;
	}

	/**
	 *
	 * @return (CardState)
	 */
	public CardState withoutTopDeckCard(){
		return null;
	}

	/**
	 *
	 * @param rng (Random)
	 * @return (CardState)
	 */
	public CardState withDeckRecreatedFromDiscards(Random rng){
		return null;
	}

	/**
	 *
	 * @param additionalDiscards (SortedBag<Card>)
	 * @return (CardState)
	 */
	public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
		return null;
	}
	
}
