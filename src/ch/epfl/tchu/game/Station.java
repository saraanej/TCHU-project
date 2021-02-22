package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;


/**
 * Modelizes a station of the game
 * 
 * @author Yasmin Benrahhal
 * @author Sara Anejjar
 */
public final class Station {
	private final int NUMBER_STATIONS = 51;
	
	private final int id; 
	private final String name; 
	
	/**
	 * Public constructor of a station
	 * 
	 * @param id (int) Station's identification number, between 0 and 50, specific to each station
	 * @param name (String) Station's name, can be the same for different stations of a same country
	 * 
	 * @throws IllegalArgumentException if the identification number is not between 0 and 50
	 */
	public Station (int id, String name) {
		Preconditions.checkArgument(id>=0 && id<NUMBER_STATIONS);
		
		this.id=id;
		this.name=name;
	}
	
	/**
	 * public getter for the station's identification number
	 * @return id (int)
	 */
	public int id() {
		return id;
	}
	
	/**
	 * public getter for the station's name
	 * @return name (String)
	 */
	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
