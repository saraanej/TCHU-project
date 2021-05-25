package ch.epfl.tchu.game;

import java.util.List;

/**
 * The public and immutable enumerated type Card represents the different types of cards in the game,
 * namely the eight types of wagon cards (one per suit), and the type of locomotive card.
 *
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
 */
public enum Card {

	BLACK(Color.BLACK),
	VIOLET(Color.VIOLET),
	BLUE(Color.BLUE),
	GREEN(Color.GREEN),
	YELLOW(Color.YELLOW),
	ORANGE(Color.ORANGE),
	RED(Color.RED),
	WHITE(Color.WHITE),
	LOCOMOTIVE(null);

	/**
	 * List containing only the wagon cards.
	 */
	public final static List<Card> CARS = List.of(
			BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);
	/**
	 * List containing all the values of the enum type Card.
	 */
	public final static List<Card> ALL = List.of(Card.values());
	/**
	 * Integer containing the size of the enum type Card.
	 */
	public final static int COUNT = ALL.size();

	/**
	 * @param color The card's color.
	 * @return The type of the corresponding wagon card depending on the card's color.
	 */
	public static Card of(Color color) {
		switch (color) {
			case BLACK:
				return Card.BLACK;
			case VIOLET:
				return Card.VIOLET;
			case BLUE:
				return Card.BLUE;
			case GREEN:
				return Card.GREEN;
			case YELLOW:
				return Card.YELLOW;
			case ORANGE:
				return Card.ORANGE;
			case RED:
				return Card.RED;
			case WHITE:
				return Card.WHITE;
			default:
				return null;
		}
	}


	private Color colorName;


	/**
	 * Constructor initializing the color of the wagon cards.
	 *
	 * @param color The color of the wagon's card.
	 */
	Card(Color color) {
		colorName = color;
	}


	/**
	 * @return The color of the card's type if it's a wagon card. null if not.
	 */
	public Color color() {
		return colorName;
	}
}

