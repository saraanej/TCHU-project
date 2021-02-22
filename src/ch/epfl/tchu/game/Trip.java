/**
 * 
 */
package ch.epfl.tchu.game;

import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * Modelizes a Trip from a Station to another 
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public final class Trip {

	private final Station from;
	private final Station to;
	private final int points;
	
	
	/**
	 * Public constructor 
	 * @param from
	 * @param to
	 * @param points
	 */
	public Trip(Station from, Station to, int points) { //NOTE FOR ME : REVERIFIE IMMUABILITE DE STATION
		Preconditions.checkArgument(points>0);
		
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
		this.points = points;

	}
	
	
}
