package ch.epfl.tchu.game.src.ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.Card;

/**
 * Modelizes the private state of the wagon/locomotive cards
 * that the player doesn't possess
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class CardState extends PublicCardState{

	
	public static CardState of(Deck<Card> deck) {
		Preconditions.checkArgument(deck.size()>=5);
	}
	
	
}
