package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * The CardState class of the ch.epfl.tchu.game package, public, final and immutable.
 * Represents the private state of the wagon/locomotive cards that are not in the hands of the players.
 * It inherits from PublicCardState and adds to it the private elements of the state,
 * as well as the corresponding methods.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public final class CardState extends PublicCardState {

	/**
	 * Constructs a private state in which the Face Up Cards are the first 5 cards of the given deck,
	 * the cardState's deck consists of the remaining cards of the given deck, and the discard is empty.
	 *
	 * @param deck (Deck<Card>) the deck of cards of the game
	 * @return (CardState) the cardState corresponding to the deck
	 * @throws IllegalArgumentException if the deck's size is strictly smaller than 5
	 */
	public static CardState of(Deck<Card> deck) {
		Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);
		return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(),
				deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT),
				SortedBag.of());
	}


	private final Deck<Card> deck;
	private final SortedBag<Card> discard;

	/**
	 * Default private constructor for the card's state.
	 *
	 * @param faceUpCards (List<Card>) the list of the visible cards
	 * @param deck        (Deck<Card>) contains the cards of the deck
	 * @param discard     (SortedBag<Card>) contains the discarded cards of the game
	 */
	private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discard) {
		super(faceUpCards, deck.size(), discard.size());
		this.deck = deck;
		this.discard = discard;
	}


	/**
	 * Returns the card at the top of this cardState's deck.
	 *
	 * @return (Card) the top card of this CardState's deck
	 * @throws IllegalArgumentException if the deck is empty
	 */
	public Card topDeckCard() {
		Preconditions.checkArgument(!deck.isEmpty());
		return deck.topCard();
	}

	/**
	 * Returns a set of cards identical to the receiver (this),
	 * except that the slot-th face-up card has been replaced by the one at the top of the deck,
	 * which is removed at the same time.
	 *
	 * @param slot (int) index of the visible card
	 * @return (CardState) same as this CardState with the slot-th face up card
	 * replaced with the top card of the deck
	 * and its deck without the top card used
	 * @throws IndexOutOfBoundsException if slot is less than 0 or bigger or equal than 5
	 * @throws IllegalArgumentException  if the deck is empty
	 */
	public CardState withDrawnFaceUpCard(int slot) {
		Preconditions.checkArgument(!deck.isEmpty());
		List<Card> newFaceUpcards = new ArrayList<>(this.faceUpCards());
		newFaceUpcards.set(Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT), deck.topCard());
		return new CardState(newFaceUpcards, deck.withoutTopCard(), this.discard);
	}

	/**
	 * Returns a cardState identical to the receiver (this), but without the card at the top of its deck.
	 *
	 * @return (CardState) same as this CardState without the top card of its deck
	 * @throws IllegalArgumentException if the deck is empty
	 */
	public CardState withoutTopDeckCard() {
		Preconditions.checkArgument(!deck.isEmpty());
		return new CardState(this.faceUpCards(), deck.withoutTopCard(), discard);
	}

	/**
	 * Returns a set of cards identical to the receiver (this),
	 * except that the cards from the discard pile have been shuffled
	 * using the given random generator to form the new cardState's deck.
	 *
	 * @param rng (Random) random number generator to shuffle the discard's cards
	 * @return (CardState) same as this CardState with the deck composed of the shuffled discard's Cards
	 * @throws IllegalArgumentException if the deck is not empty
	 */
	public CardState withDeckRecreatedFromDiscards(Random rng) {
		Preconditions.checkArgument(deck.isEmpty());
		return new CardState(this.faceUpCards(), Deck.of(discard, rng), SortedBag.of());
	}

	/**
	 * Returns a cardState identical to the receiver (this), but with the given cards added to the discard.
	 *
	 * @param additionalDiscards (SortedBag<Card>) the Cards to add to the discard
	 * @return (CardState) same as this CardState with the additionalDiscards added to its discard
	 */
	public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards) {
		SortedBag.Builder<Card> newDiscard = new SortedBag.Builder<>();
		newDiscard.add(discard);
		newDiscard.add(additionalDiscards);

		return new CardState(this.faceUpCards(), this.deck, newDiscard.build());
	}


}
