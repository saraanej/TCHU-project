/**
 * 
 */
package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
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
	 * Public constructor of a Trip
	 * 
	 * @param from (Station) the departure Station of the Trip
	 * @param to (Station) the arrival Station of the Trip
	 * @param points (int) the number of points associated to the Trip
	 */
	public Trip(Station from, Station to, int points) { //NOTE FOR ME : REVERIFIE IMMUABILITE DE STATION
		Preconditions.checkArgument(points>0);
		
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
		this.points = points;

	}
	
	
	/**
	 * Returns all the Trips between a list of Stations and another
	 * 
	 * @param from (List<Station>)
	 * @param to (List<Station>)
	 * @param points (int) points attributed to the Trips
	 * 
	 * @return (List<Trip>) all the Trips possible from a Station of the first list "from" to the station of the second list "to"
	 */
	public static List<Trip> all(List<Station> from, List<Station>to, int points){
		Preconditions.checkArgument(from != null && to != null && points > 0);
		
		List<Trip> all = new ArrayList<Trip>();
		
		for (Station f : from ) {
			for (Station t : to) {
				all.add(new Trip(f,t,points));
			}
		}
		
		return all;
	}
	
	/**
	 * Getter for the departure's Station 
	 * @return (Station) from
	 */
	public Station from() {
		return from;
	}
	
	/**
	 * Getter for the arrival's Station 
	 * @return (Station) to
	 */
	public Station to() {
		return to;
	}
	
	/**
	 * Getter for the points assigned to the Trip
	 * @return (int) points
	 */
	public int points() {
		return points;
	}
	
	/**
	 * Getter for the number of points depending fo the connectivity of the departure's and arrival's Station 
	 * @param connectivity (StationConnectivity) 
	 * 
	 * @return (int) points if the Stations are connected, -points if not
	 */
	public int points(StationConnectivity connectivity) {
		return connectivity.connected(to, from) ? points : -points;
	}
}
