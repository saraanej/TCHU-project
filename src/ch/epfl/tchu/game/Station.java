package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;


/**
 * Modelizes a station of the game.
 *
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Station {
	
	private final int id; 
	private final String name; 
	
	/**
	 * Public constructor of a station.
	 * @param id Station's identification number, positive and specific to each station.
	 * @param name Station's name, can be the same for different stations of a same country, cannot be null.
	 * @throws IllegalArgumentException 
	                 if the identification number is not between 0 and 50.
	 */
	public Station (int id, String name) {
		Preconditions.checkArgument(id>=0);
		Preconditions.checkArgument(name != null);
		this.id = id;
		this.name = name;
	}

	
	/**
	 * Public getter for the Station's identification number.
	 * @return The Station's identification number.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Public getter for the Station's name.
	 * @return The Station's name.
	 */
	public String name() {
		return name;
	}


	/**
	 * @see String#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
