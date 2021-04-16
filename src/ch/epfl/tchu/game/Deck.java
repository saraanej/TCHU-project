package ch.epfl.tchu.game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * The Deck class from the ch.epfl.tchu.game package, public, final, and immutable.
 * Represents a bunch of cards.
 * Note that the word "card" here refers to any type of map, not just wagon / locomotive cards.
 * Thus, in this project, the Deck class will be used both to represent the draw pile of the wagon / locomotive cards
 * and to represent the draw pile of tickets.
 *
 * @param C represents the type of the deck's cards.
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Deck<C extends Comparable<C>> {

    /**
     * Creates a Deck containing a shuffled version of the cards using the rng random number generator.
     *
     * @param <C>   the type of cards, must extends Comparable<C>
     * @param cards (SortedBag<C>) the sorted collection of cards C to store in the Deck
     * @param rng   (Random) random number generator to shuffle the C-cards
     * @return a deck composed of the shuffled version of cards
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        List<C> shuffledCards = cards.toList();
        Collections.shuffle(shuffledCards, rng);
        return new Deck<C>(shuffledCards);
    }


    private final int size;
    private final List<C> cards;

    /**
     * Private constructor of a deck.
     *
     * @param cards (List<C>) the list of cards C to store in the Deck
     */
    private Deck(List<C> cards) {
        this.cards = List.copyOf(cards);
        this.size = this.cards.size();
    }


    /**
     * Returns wether or not the deck is empty.
     *
     * @return (Deck) true iff the deck is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of the pile, i.e. the number of cards it contains.
     *
     * @return (int) the size of the Deck
     */
    public int size() {
        return size;
    }

    /**
     * Returns the card C at the top of this deck.
     *
     * @return (C) the first card of this Deck
     * @throws IllegalArgumentException if this deck is empty
     */
    public C topCard() {
        Preconditions.checkArgument(!this.isEmpty());
        return cards.get(0);
    }

    /**
     * Returns a SortedBag<C> containing the count cards C at the top of the pile.
     *
     * @param count (int) the desired number of first cards from the deck
     * @return (SortedBag < C >) the sorted "count" first cards of the deck
     * @throws IllegalArgumentException if the count is not between 0 and the size of the deck (included)
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(count <= cards.size());
        Preconditions.checkArgument(count >= 0);
        return SortedBag.of(cards.subList(0, count));
    }

    /**
     * Returns a deck identical to the receiver (this) but without the top card.
     *
     * @return (Deck) a new deck (same as this) without the first card
     * @throws IllegalArgumentException if this deck is empty
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!this.isEmpty());
        return new Deck<C>(cards.subList(1, cards.size()));
    }

    /**
     * Returns a deck identical to the receiver (this) but without the count top cards.
     *
     * @param count (int) the desired number of first cards from the deck to remove
     * @return (Deck) a new deck (same as this) without the "count" first card
     * @throws IllegalArgumentException if the count is not between 0 and the size of the deck (included)
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(count <= cards.size());
        Preconditions.checkArgument(count >= 0);
        return new Deck<C>(cards.subList(count, cards.size()));
    }
}
