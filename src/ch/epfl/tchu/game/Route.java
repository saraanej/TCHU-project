package ch.epfl.tchu.game;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Modelizes a route.
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Route {

	private final int length;
	private final String id;
	private final Station station1, station2;
	private final Level level;
	private final Color color;

	/**
	 * Modelizes the route's level.
	 */
	public enum Level{
		OVERGROUND,
		UNDERGROUND;
	}
	
	/**
	 * Public default constructor of a route.
	 * @param id (String) : The route's identity.
	 * @param station1 (Station) : The first station of the route.
	 * @param station2 (Station) : The second station of the route.
	 * @param length (int) : The route's length.
	 * @param level (Level) : The level to which the route belongs.
	 * @param color (Color) : The route's color, null when it's a neutral route.
	 * @throws IllegalArgumentException 
	             if the first station is the same as the second one or when the length of the route exceed the length it's supposed to have in the game.
	 * @throws NullPointerException 
	             if the route's identity or first station or second station or level are equal to null.
	 */
	public Route(String id, Station station1, Station station2, int length, Level level, Color color){
		Preconditions.checkArgument(!station1.equals(station2));
		Preconditions.checkArgument(length >= Constants.MIN_ROUTE_LENGTH);
		Preconditions.checkArgument(length <= Constants.MAX_ROUTE_LENGTH);

		this.id = Objects.requireNonNull(id);
		this.level = Objects.requireNonNull(level);
		this.station1 = Objects.requireNonNull(station1);
		this.station2 = Objects.requireNonNull(station2);
		this.color = color;
		this.length = length;
	}

	/**
	 * Public getter for the route's length.
	 * @return (int) The route's length.
	 */
	public int length() {
		return this.length;
	}

	/**
	 * @return (int) The points of construction earned by the player when he/she gets the route.
	 */
	public int claimPoints() {
		switch(length) {
			case(1): return 1;
			case(2): return 2;
			case(3): return 4;
			case(4): return 7;
			case(5): return 10;
			case(6): return 15;
			default: return 0;}
	}

	/**
	 * @param claimCards (SortedBag<Card>): The cards that the player already used.
	 * @param drawnCards (SortedBag<Card>): The three cards drawn from the top of the deck.
	 * @return (int) The number of additional cards to play to get the route in the tunnel.
	 * @throws IllegalArgumentException
	if the route is not a tunnel or if drawnCards doesn't contain exactly three cards.
	 */
	public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
		Preconditions.checkArgument(level.equals(Level.UNDERGROUND));
		Preconditions.checkArgument(drawnCards.size() == 3);

		AtomicInteger additionalCards = new AtomicInteger(0);
		drawnCards.forEach(drawnCard -> { if(claimCards.contains(drawnCard) || drawnCard == Card.LOCOMOTIVE) {
			additionalCards.incrementAndGet(); }
		});

		return additionalCards.intValue();
	}
	
	/**
	 * Public getter for the route's identity.
	 * @return (String) The route's identity.
	 */
	public String id() { return this.id; }

	/**
	 * Public getter for the level to which the route belongs.
	 * @return (Level) The level to which the route belongs.
	 */
	public Level level() {
		return this.level;
	}

	/**
	 * Public getter for the route's color.
	 * @return (Color) The route's color, null if it's a neutral route.
	 */
	public Color color() {
		return this.color;
	}
	
	/**
	 * Public getter for the first station of the route.
	 * @return (Station) The first station of the route.
	 */
	public Station station1() {
		return this.station1;
	}
	
	/**
	 * Public getter for the second station of the route.
	 * @return (Station) The second station of the route.
	 */
	public Station station2() {
		return this.station2;
	}

	/**
	 * @param station (Station) The station to which we want its opposite one.
	 * @return (Station) The opposite station to the one given in the parameters.
	 * @throws IllegalArgumentException
	                 if the station given in parameters doesn't correspond to any of the stations of the actual route.
	 */
	public Station stationOpposite(Station station) {
		Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
		return station.equals(station1) ? station2 : station1;
	}

	/**
	 * @return (List<Station>) The list of the two stations of the route, in the same order as in the route's constructor.
	 */
	public List<Station> stations(){
		List<Station> stations = List.of(station1, station2);
		return stations;
	}
	
	/**
	 * @return (List<SortedBag<Card>>) The list of all the cards that could be used to get the route,
	                                   sorted in increasing order of the number of locomotive cards and then by color.
	 */
	public List<SortedBag<Card>> possibleClaimCards(){
		List<SortedBag<Card>> possibleCards = new ArrayList<>();

		if(level.equals(Level.OVERGROUND)) {
			if(color == null) {
				for(Card c : Card.CARS) {
					possibleCards.add(SortedBag.of(length, c));}
			} else {
				possibleCards.add(SortedBag.of(length, Card.of(color)));}
		}
        else if(level.equals(Level.UNDERGROUND)) {
			if(color == null) {
				for(int i = 0; i < this.length ; ++i) {
					for(Card c : Card.CARS) {
						possibleCards.add(SortedBag.of(length - i, c, i , Card.LOCOMOTIVE));}
				}
				possibleCards.add(SortedBag.of(length, Card.LOCOMOTIVE));
		    } else {
		    	for(int i = 0; i <= this.length ; ++i) {
		    		possibleCards.add(SortedBag.of(i, Card.LOCOMOTIVE, length-i, Card.of(color))); }
		    }
        }
		return possibleCards;
	}
}