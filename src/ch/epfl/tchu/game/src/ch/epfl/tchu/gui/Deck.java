package ch.epfl.tchu.game.src.ch.epfl.tchu.gui;

import java.util.ArrayList;
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

	public Deck(List<C> cards) {
		this.cards = Collections.unmodifiableList(new ArrayList<>(cards));
		this.size = this.cards.size();
	}

	/**
	 * 
	 * @param <C>
	 * @param cards
	 * @param rng
	 * @return a bunch of cards 
	 */
	public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
		List<C> shuffledCards = cards.toList();
		Collections.shuffle(shuffledCards,rng);
		return new Deck<C>(shuffledCards);
	}

	/**
	 *
	 * @return
	 */
	public boolean isEmpty(){
		return size == 0 ? true : false;
	}

	/**
	 *
	 * @return
	 * @throws IllegalArgumentException
	 */
	public C topCard(){
		Preconditions.checkArgument(!this.isEmpty());
		return null;
	}

	/**
	 *
	 * @return
	 */
	public Deck<C> withoutTopCard(){
		return null;
	}

	/**
	 *
	 * @param count
	 * @return
	 */
	public SortedBag<C> topCards(int count){
		Preconditions.checkArgument(!this.isEmpty());
		return null;
	}

	/**
	 *
	 * @param count
	 * @return
	 */
	public Deck<C> withoutTopCards(int count){
		return null;
	}
}
