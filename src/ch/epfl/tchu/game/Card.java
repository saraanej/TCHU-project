package ch.epfl.tchu.game;

import java.util.List;

/**
 * 
 * enum type represents the different type of cards 
 * used in the game
 *
 */

public enum Card {

	BLACK,
	VIOLET,
	BLUE,
	GREEN,
	YELLOW,
	ORANGE,
	RED,
	WHITE,
	LOCOMOTIVE;
	
	public final static List<Card> ALL = List.of(Card.values());
	public final static int COUNT = ALL.size(); 
	static List<Card> c = List.of(Card.values());
	public final static List<Card> CARS = c.remove(9);
	
	public static Card of(Color color) {
		
		
		return color;
	}
	
	public Color color() {
		if(this)
	}

	
}
