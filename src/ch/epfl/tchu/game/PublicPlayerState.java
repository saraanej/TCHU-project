package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;


/**
 * The PublicPlayerState class of the ch.epfl.tchu.game package, public and immutable,
 * Modelizes the public player's State.
 * Represents the public part of a player's state, namely the number of tickets, cards and cars he owns,
 * the routes he claimed, and the number of claim points he got as a result.
 * It offers a unique public constructor.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final int carCount;
    private final int claimPoints;
    private final List<Route> routes;


    /**
     * Default public constructor of the PublicPlayerState.
     * @param ticketCount (int): The number of tickets the player owns.
     * @param cardCount (int): The number of cards the player owns.
     * @param routes (List<Routes>): The roads the player owns.
     * @throws IllegalArgumentException
                 if the ticketCount is strictly negative,
                 if the cardCount is strictly negative.
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) {
        Preconditions.checkArgument(ticketCount >= 0);
        Preconditions.checkArgument(cardCount >= 0);
        this.routes = List.copyOf(routes);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.carCount = computeCarCount();
        this.claimPoints = computeClaimPoints();
    }


    /**
     * @return (int) The number of tickets the player owns.
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * @return (int) The number of cards the player owns.
     */
    public int cardCount() {
        return cardCount;
    }

    /**
     * @return (int) The number of wagons the player owns.
     */
    public int carCount() {
        return carCount;
    }

    /**
     * @return (int) The number of construction's points earned by the player.
     */
    public int claimPoints() {
        return claimPoints;
    }

    /**
     * @return (List<Routes>) All the roads the player owns.
     */
    public List<Route> routes(){
        return routes;
    }


    /**
     * @return (int) The number of construction's points earned by the player.
     */
    private int computeClaimPoints() {
        int points = 0;
        if(routes != null) {
            for (Route route : routes) { points += route.claimPoints(); }
        }
        return points;
    }

    /**
     * @return (int) The number of wagons the player owns.
     */
    private int computeCarCount() {
        int length = 0;
        if(routes != null) {
            for (Route route : routes) { length += route.length(); }
        }
        return Constants.INITIAL_CAR_COUNT - length;
    }
}