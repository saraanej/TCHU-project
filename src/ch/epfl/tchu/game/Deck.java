package ch.epfl.tchu.game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Modelizes a bunch of cards
 * 
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 * 
 * @param C represents the type of the cards
 *
 */
public final class Deck<C extends Comparable<C>> {

	private final int size;
	private final List<C> cards;

	private Deck(List<C> cards) {
		this.cards = List.copyOf(cards);
		this.size = this.cards.size();
	}

	/**
	 * creates a Deck containing a shuffled version of the cards
	 * @param <C> the type of cards, must extends Comparable<C>
	 * @param cards (SortedBag<C>) the sorted collection of cards C to store in the Deck
	 * @param rng (Random) random number generator to shuffle the C-cards
	 * @return a deck composed of the shuffled version of cards
	 */
	public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
		List<C> shuffledCards = cards.toList();
		Collections.shuffle(shuffledCards,rng);
		return new Deck<C>(shuffledCards);
	}

	/**
	 * 
	 * @return (int) the size of the Deck
	 */
	public int size(){
		return size;
	}

	/**
	 *
	 * @return (Deck) true iff the deck is empty
	 */
	public boolean isEmpty(){
		return size == 0 ? true : false;
	}

	/**
	 *
	 * @return (C) the first card of this Deck
	 * @throws IllegalArgumentException
	             if this deck is empty
	 */
	public C topCard(){
		Preconditions.checkArgument(!this.isEmpty());
		return cards.get(0);
	}

	/**
	 *
	 * @return (Deck) a new deck without the first card
	 * @throws IllegalArgumentException
	              if this deck is empty
	 */
	public Deck<C> withoutTopCard(){
		Preconditions.checkArgument(!this.isEmpty());
		return new Deck<C>(cards.subList(1, cards.size()));
	}

	/**
	 *
	 * @param count (int) the desired number of first cards from the deck
	 * @return (SortedBag<C>) the sorted "count" first cards of the deck
	 * @throws IllegalArgumentException
	             if this deck is empty
	             if the count is not between 0 and the size of the deck (included)
	 */
	public SortedBag<C> topCards(int count){
		Preconditions.checkArgument(!this.isEmpty()
				                    && count <= cards.size()
				                    && count >= 0);

		return SortedBag.of(cards.subList(0,count));
	}

	/**
	 *
	 * @param count (int) the desired number of first cards from the deck to remove
	 * @return (Deck) a new deck without the "count" first card
	 * @throws IllegalArgumentException
	             if this deck is empty
	             if the count is not between 0 and the size of the deck (included)
	 */
	public Deck<C> withoutTopCards(int count){
		Preconditions.checkArgument(!this.isEmpty()
				                    && count <= cards.size()
				                    && count >= 0);
		return new Deck<C>(cards.subList(count, cards.size()));
	}

}
