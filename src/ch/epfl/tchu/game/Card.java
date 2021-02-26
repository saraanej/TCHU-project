package ch.epfl.tchu.game;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * Modelizes the different types of cards used in the game
 * 
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
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
	
	/**
	 * List containing only the wagon cards
	 */
	public final static List<Card> CARS = List.of(

	BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

	
	/**
	 * 
	 * @param color (Color) : the card's color.
	 * @return the type of the corresponding wagon card depending on the card's color.
	 */
	
	public static Card of(Color color) {
		
		switch(color) {
		case BLACK : return Card.BLACK;
		case VIOLET : return Card.VIOLET; 
		case BLUE : return Card.BLUE;
		case GREEN : return Card.GREEN;
		case YELLOW : return Card.YELLOW;
		case ORANGE : return Card.ORANGE;
		case RED : return Card.RED;
		case WHITE : return Card.WHITE;
		default : return null;
		}
		
	}
	
	/**
	 * Constructor initializing the color of the wagon cards. 
	 * @param color (Color)
	 */
	private Card(Color color) {
		colorName = color;
	}
	
	private Color colorName;
	
	private Color colorName() {
		return colorName;
	}
	
	
	/**
	 * 
	 * @return (Color) the color of the card's type if it's a wagon card. null if not.
	 */
	public Color color() {
		return (this.colorName() == null) ?  null :  this.colorName();	
	}
	
}

