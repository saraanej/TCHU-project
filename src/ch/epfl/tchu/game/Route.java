package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Modelizes a route
 * 
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public final class Route { // Note : rendre la classe immuable
	
	
	private final String id;
	private final Station station1;
	private final Station station2;
	private final int length;
	private final Level level;
	private final Color color;

	
	public enum Level{
		
		OVERGROUND,
		UNDERGROUND;
	}
	
	/**
	 * public default constructor of a route
	 * 
	 * @param (String) id : the route's identity
	 * @param (Station) station1 : the first station of the route
	 * @param (Station) station2 : the second station of the route
	 * @param (int) length : the route's length
	 * @param (Level) level : the level to which the route belongs
	 * @param (Color) color : the route's color, null when it's a neutral route
	 * @throws IllegalArgumentException when the first station is the same as the second one or when the length of the route exceed the length it's supposed to have in the game 
	 * @throws NullPointerException when either the route's identity or first station or second station or level are equal to null
	 */
	public Route(String id, Station station1, Station station2, int length, Level level, Color color){
		Preconditions.checkArgument(!station1.equals(station2) && length>= Constants.MIN_ROUTE_LENGTH && length <= Constants.MAX_ROUTE_LENGTH);
		if(id.equals(null) || station1.equals(null) || station2.equals(null) || level.equals(null)) {
			throw new NullPointerException();
		}
		
		this.id = id;
		this.station1 = station1;
		this.station2 = station2;
		this.length = length;
		this.level = level;
		this.color = color;
	}
	
	/**
	 * public getter for the route's identity
	 * @return (String) the route's identity;
	 */
	public String id() {
		return this.id;
	}
	
	/**
	 * public getter for the first station of the route
	 * @return (Station) the first station of the route
	 */
	public Station station1() {
		return this.station1;
	}
	
	/**
	 * public getter for the second station of the route
	 * @return (Station) the second station of the route
	 */
	public Station station2() {
		return this.station2;
	}
	
	/**
	 * public getter for the route's length
	 * @return (int) the route's length
	 */
	public int length() {
		return this.length;
	}
	
	/**
	 * public getter for the level to which the route belongs
	 * @return (Level) the level to which the route belongs
	 */
	public Level level() {
		return this.level;
	}
	
	/**
	 * public getter for the route's color
	 * @return (Color) the route's color, null if it's a neutral route
	 */
	public Color color() {
		return this.color;
	}
	
	/**
	 * @return (List<Station>) the list of the two stations of the route, in the same order as in the route's constructor
	 */
	public List<Station> stations(){
		List<Station> stations = new ArrayList<Station>();
		stations.add(station1);
		stations.add(station2);
		return stations;
	}
	
	/**
	 * @param station to which we want its opposite one
	 * @return (Station) the opposite station to the one given in the parameters
	 * @throws IllegalArgumentException if the station given in parameters doesn't correspond to any of the stations of the actual route
	 */
	public Station stationOpposite(Station station) {
		Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
		return station.equals(station1) ? station2 : station1;
	}
	
	/**
	 * @return (List<SortedBag<Card>>) the list of all the cards that could be used to get the route
	                                 sorted in increasing order of the number of locomotive cards and the by color
	 */
	public List<SortedBag<Card>> possibleClaimCards(){
		return null;
	}
	
	/**
	 * @param (SortedBag<Card>) claimCards : cards that the player already used
	 * @param (SortedBag<Card>) drawnCards : the three cards drawn from the top of the deck
	 * @return (int) the number of additional cards to play to get the route in the tunnel
	 * @throws IllegalArgumentException if the route is not a tunnel or if drawnCards doesn't contain exactly three cards
	 */
	public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
		return 0;
	}
	
	/**
	 * @return (int) the points of construction earned by the player when he/she gets the route
	 */
	public int claimPoints() {
		return 0;
	}

}
