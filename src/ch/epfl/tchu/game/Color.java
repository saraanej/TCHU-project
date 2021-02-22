package ch.epfl.tchu.game;

import java.util.List;

/**
 * 
 * enum type represents the colors used in the game 
 * for the cards and paths.
 *
 */

public enum Color {
	
		BLACK,
		VIOLET,
		BLUE,
		GREEN,
		YELLOW,
		ORANGE,
		RED,
		WHITE;
	
	public final static List<Color> ALL = List.of(Color.values());
	public final static int COUNT = ALL.size(); 

}
