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
	 * @param from (Station) the departure Station of the Trip
	 * @param to (Station) the arrival Station of the Trip
	 * @param points (int) the number of points associated to the Trip
	 * @throws IllegalArgumentException
	            if the points are negative 
	            if the departure's station is the same as the arrival's station
	 * @throws NullPointerException 
	 *          if the departure's station (to) is empty
	 *          if the arrival's station(from) is empty
	 */
	public Trip(Station from, Station to, int points) { 
		Preconditions.checkArgument(points>0 && from != to);
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
		this.points = points;

	}
	
	
	/**
	 * Returns all the Trips between a list of Stations and another
	 * @param from (Station) the departure Stations of the Trips
	 * @param to (Station) the arrival Stations of the Trips
	 * @param points (int) points attributed to the Trips
	 * @return (List<Trip>) all the Trips possible from a Station of the first list "from" to the station of the second list "to"
	 * @throws IllegalArgumentException 
	 *         if the departure's station is empty 
	 *         if the arrival's station is empty 
	 *         if the points are negative
	 *         if the departure's station is the same as the arrival's station 
	 */
	public static List<Trip> all(List<Station> from, List<Station>to, int points){
		Preconditions.checkArgument(from != null && to != null && points > 0 && from != to);
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
	 * @return from (Station) the departure Station of the Trip
	 */
	public Station from() {
		return from;
	}
	
	/**
	 * Getter for the arrival's Station 
	 * @return to (Station) the arrival Station of the Trip
	 */
	public Station to() {
		return to;
	}
	
	/**
	 * Getter for the points assigned to the Trip
	 * @return points (int) the points attributed to the Trip
	 */
	public int points() {
		return points;
	}
	
	/**
	 * Getter for the number of points depending on the connectivity of the departure's and arrival's Station 
	 * @param connectivity (StationConnectivity) 
	 * @return points (int) if the Stations are connected, - points if not
	 */
	public int points(StationConnectivity connectivity) {
		return connectivity.connected(to, from) ? points : - points;
	}
}
