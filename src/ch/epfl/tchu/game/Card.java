package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
 * 
 * enum type represents the different types of cards 
 * used in the game
 *
 */

public enum Card {

	BLACK (Color.BLACK),
	VIOLET (Color.VIOLET),
	BLUE (Color.BLUE),
	GREEN (Color.GREEN),
	YELLOW (Color.YELLOW),
	ORANGE (Color.ORANGE),
	RED (Color.RED),
	WHITE (Color.WHITE),
	LOCOMOTIVE (null);
	
	public final static List<Card> ALL = List.of(Card.values());
	public final static int COUNT = ALL.size(); 
	public final static List<Card> CARS = Arrays.asList(
	BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

	private Color colorName;
	
	private Color colorName() {
		return colorName;
	}
	
	/**
	 * @return a new List<Card> containing only the wagon cards.
	 */
	
	
	
	
	
	/**
	 * Constructor initializing the color of the wagon cards. 
	 * @param colorName
	 */
	private Card(Color color) {
		colorName = color;
	}
	
	
	/**
	 * 
	 * @param color : the card's color.
	 * @return the type of the corresponding wagon card.
	 */
	
	
	/*public static Card of(Color color) {
		switch(colorName) {
		
		}
		return ;
	}*/
	
	
	/**
	 * 
	 * @return the color of the card's type if it's a wagon card. null if not.
	 */
	public Color color() {
		for(int i = 0; i < (Card.values()).length; ++i) {
			return (((Card.values())[i]).colorName() == null) ?  null :  this.colorName();	
			
	    }
	}
	
/*private static List<Card> removed() {
		
		 List<Card> wagonCards = null;
		
		for(int i = 0; i < (Card.values()).length; ++i) {
			if(((Card.values())[i]).colorName() != null) {
				wagonCards.add((Card.values())[i]);
			}
		}
		return wagonCards;
	}*/
}

