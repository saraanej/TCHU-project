package ch.epfl.tchu.game;

import java.util.List;

/**
 * The public and immutable enumerated type Color, represents the eight colors
 * used in the game to color railcar and road maps.
 *
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
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

	/**
	 * List containing all the values of the enum type Color.
	 */
	public final static List<Color> ALL = List.of(Color.values());
	/**
	 * Integer containing the size of the enum type Color.
	 */
	public final static int COUNT = ALL.size();
}