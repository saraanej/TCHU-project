package ch.epfl.tchu.game;

import java.util.List;

/**
 * 
 * enum type represents the different type of cards 
 * used in the game
 *
 */

public enum Card {

	BLACK ("black"),
	VIOLET ("violet"),
	BLUE ("blue"),
	GREEN ("green"),
	YELLOW ("yellow"),
	ORANGE ("orange"),
	RED ("red"),
	WHITE ("white"),
	LOCOMOTIVE (null);
	
	public final static List<Card> ALL = List.of(Card.values());
	public final static int COUNT = ALL.size(); 
	public final static List<Card> CARS = remove();
	
	private  String colorName;
	
	private  String colorName() {
		return colorName;
	}
	
	/**
	 * @return a new List<Card> containing only the wagon cards.
	 */
	private static List<Card> remove() {
		
		private static List<Card> wagonCards;
		
		for(int i = 0; i < (Card.values()).length; ++i) {
			if((Card.values())[i] != null) {
				wagonCards.add((Card.values())[i]);
			}
		}
		return wagonCards;
	}
	
	
	/**
	 * Constructor initializing the color of the wagon cards. 
	 * @param colorName
	 */
	private Card(String color) {
		colorName = color;
	}
	
	
	/**
	 * 
	 * @param color : the card's color.
	 * @return the type of the corresponding wagon card.
	 */
	public static Card of(Color color) {
		
		return color.colorName();
	}
	
	/**
	 * 
	 * @return the color of the card's type if it's a wagon card. null if not.
	 */
	public Color color() {
		
	}

	
}
