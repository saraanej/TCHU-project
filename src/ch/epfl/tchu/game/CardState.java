package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
	private final SortedBag<Card> discard;


	/**
	 * default constructor for the card's state
	 * @param faceUpCards (List<Card>) the list of the visible cards
	 * @param deck (Deck<Card>) contains the cards of the deck
	 * @param discard (SortedBag<Card>) contains the discarded cards of the game
	 * @throws IllegalArgumentException
	             if the list of the visible cards doesn't contain exactly five cards
	             if the sizes of the deck or the discard pile are negative
	 */
	private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discard) {
		super(faceUpCards, deck.size(), discard.size());
		this.deck = deck;
		this.discard = discard;
	}

	/**
	 * constructs the private CardState for the Deck
	 * @param deck (Deck<Card>) the deck of cards of the game
	 * @return (CardState) the cardState corresponding to the deck
	 * @throws IllegalArgumentException
	             if the deck's size is strictly smaller than 5
	 */
	public static CardState of(Deck<Card> deck) {
		Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);
		return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(),
				deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT),
				SortedBag.of());
	}

	/**
	 * @param slot (int) index of the visible card
	 * @return (CardState) this CardState with the slot-th card
	                       replaced with the top card of the deck
	                       and its deck without the top card used
	 * @throws IndexOutOfBoundsException
	             if slot is less than 0 or bigger or equal than 5
	 * @throws IllegalArgumentException
	             if the deck is empty
	 */
	public CardState withDrawnFaceUpCard(int slot){
		Preconditions.checkArgument(!deck.isEmpty());
		List<Card> newFaceUpcards = new ArrayList<>(this.faceUpCards());
		newFaceUpcards.set(Objects.checkIndex(slot,Constants.FACE_UP_CARDS_COUNT),deck.topCard());
		return new CardState(newFaceUpcards,deck.withoutTopCard(),this.discard);
	}

	/**
	 * @return (Card) the top card of this CardState's deck
	 * @throws IllegalArgumentException
	             if the deck is empty
	 */
	public Card topDeckCard(){
		Preconditions.checkArgument(!deck.isEmpty());
		return deck.topCard();
	}

	/**
	 * @return (CardState) this CardState without the top card of its deck
	 * @throws IllegalArgumentException
	            if the deck is empty
	 */
	public CardState withoutTopDeckCard(){
		Preconditions.checkArgument(!deck.isEmpty());
		return new CardState(this.faceUpCards(), deck.withoutTopCard(),discard);
	}

	/**
	 * @param rng (Random) random number generator to shuffle the discard's cards
	 * @return (CardState) this CardState with the deck composed of the shuffled discard's Cards
	 * @throws IllegalArgumentException
	 	            if the deck is not empty
	 */
	public CardState withDeckRecreatedFromDiscards(Random rng){
		Preconditions.checkArgument(deck.isEmpty());
		return new CardState(this.faceUpCards(),Deck.of(discard,rng),SortedBag.of());
	}

	/**
	 * @param additionalDiscards (SortedBag<Card>) the Cards to add to the discard
	 * @return (CardState) this CardState with the additionalDiscards added to its discard
	 */
	public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
		SortedBag.Builder<Card> newDiscard = new SortedBag.Builder<>();
		newDiscard.add(discard);
		newDiscard.add(additionalDiscards);
		return new CardState(this.faceUpCards(),this.deck, newDiscard.build());
	}

	
}
