package ch.epfl.tchu.game;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import static ch.epfl.tchu.game.Constants.*;
import static ch.epfl.tchu.game.Card.LOCOMOTIVE;
import static ch.epfl.tchu.game.Route.Level.*;
import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * The public, final and immutable Route class represents a road connecting two neighboring towns.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Route {

	/**
	 * Modelizes the route's level.
	 */
	public enum Level{
		OVERGROUND,
		UNDERGROUND;
	}

	private final int length;
	private final String id;
	private final Station station1, station2;
	private final Level level;
	private final Color color;

	
	/**
	 * Public default constructor of a route.
	 * @param id The route's identity.
	 * @param station1 The first station of the route.
	 * @param station2 The second station of the route.
	 * @param length The route's length.
	 * @param level The level to which the route belongs.
	 * @param color The route's color, null when it's a neutral route.
	 * @throws IllegalArgumentException 
	             if the first station is the same as the second one or when the length of the route exceed the length it's supposed to have in the game.
	 * @throws NullPointerException 
	             if the route's identity or first station or second station or level are equal to null.
	 */
	public Route(String id, Station station1, Station station2, int length, Level level, Color color){
		Preconditions.checkArgument(!station1.equals(station2));
		Preconditions.checkArgument(length >= MIN_ROUTE_LENGTH);
		Preconditions.checkArgument(length <= MAX_ROUTE_LENGTH);

		this.id = Objects.requireNonNull(id);
		this.level = Objects.requireNonNull(level);
		this.station1 = Objects.requireNonNull(station1);
		this.station2 = Objects.requireNonNull(station2);
		this.color = color;
		this.length = length;
	}


	/**
	 * Public getter for the route's length.
	 * @return The route's length.
	 */
	public int length() {
		return length;
	}

	/**
	 * @return The points of construction earned by the player when he/she gets the route.
	 */
	public int claimPoints() {
		return ROUTE_CLAIM_POINTS.get(length);
	}

	/**
	 * @param claimCards The cards that the player already used.
	 * @param drawnCards The three cards drawn from the top of the deck.
	 * @return The number of additional cards to play to get the route in the tunnel.
	 * @throws IllegalArgumentException
	   if the route is not a tunnel or if drawnCards doesn't contain exactly three cards.
	 */
	public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
		Preconditions.checkArgument(level.equals(UNDERGROUND));
		Preconditions.checkArgument(drawnCards.size() == ADDITIONAL_TUNNEL_CARDS);

		AtomicInteger additionalCards = new AtomicInteger(0);
		drawnCards.forEach(drawnCard -> { if(claimCards.contains(drawnCard) || drawnCard == LOCOMOTIVE) {
			additionalCards.incrementAndGet(); }
		});

		return additionalCards.intValue();
	}
	
	/**
	 * Public getter for the route's identity.
	 * @return The route's identity.
	 */
	public String id() { return id; }

	/**
	 * Public getter for the level to which the route belongs.
	 * @return The level to which the route belongs.
	 */
	public Level level() {
		return level;
	}

	/**
	 * Public getter for the route's color.
	 * @return The route's color, null if it's a neutral route.
	 */
	public Color color() {
		return color;
	}
	
	/**
	 * Public getter for the first station of the route.
	 * @return The first station of the route.
	 */
	public Station station1() {
		return station1;
	}
	
	/**
	 * Public getter for the second station of the route.
	 * @return The second station of the route.
	 */
	public Station station2() {
		return station2;
	}

	/**
	 * @param station The station to which we want its opposite one.
	 * @return The opposite station to the one given in the parameters.
	 * @throws IllegalArgumentException
	                 if the station given in parameters doesn't correspond to any of the stations of the actual route.
	 */
	public Station stationOpposite(Station station) {
		Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
		return station.equals(station1) ? station2 : station1;
	}

	/**
	 * @return The list of the two stations of the route, in the same order as in the route's constructor.
	 */
	public List<Station> stations(){
		return List.of(station1, station2);
	}
	
	/**
	 * @return The list of all the cards that could be used to get the route,
	           sorted in increasing order of the number of locomotive cards and then by color.
	 */
	public List<SortedBag<Card>> possibleClaimCards(){
		List<SortedBag<Card>> possibleCards = new ArrayList<>();
		var maxLoco = level == OVERGROUND ? 0 : length;
		if(color == null) {
			for(int i = 0; maxLoco == length ? i < maxLoco : i <= maxLoco ; ++i) {
				for(Card c : Card.CARS)
					possibleCards.add(SortedBag.of(length - i, c, i , LOCOMOTIVE));
			}
			if (maxLoco == length) possibleCards.add(SortedBag.of(length, LOCOMOTIVE));
		} else {
			for(int i = 0; i <= maxLoco ; ++i)
				possibleCards.add(SortedBag.of(length - i, Card.of(color), i, LOCOMOTIVE));
		}
		return possibleCards;
	}
}