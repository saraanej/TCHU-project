package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public final class PublicPlayerState {
    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    /**
     * default public constructor of the PublicPlayerState
     * @param ticketCount (int) the number of tickets the play owns
     * @param cardCount (int) the number of cards the player owns
     * @param routes (List<Routes) the roads the player owns
     * @throws IllegalArgumentException
                 if the ticketCount is strictly negative
                 if the cardCount is strictly negative
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) {
        Preconditions.checkArgument(ticketCount >= 0);
        Preconditions.checkArgument(cardCount >= 0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        this.carCount = computeCarCount();
        this.claimPoints = computeClaimPoints();

    }

    private int computeClaimPoints() {
        int points = 0;
        for (Route r : routes) {
            points += r.claimPoints();
        }
        return points;
    }

    private int computeCarCount() {
        int length = 0;
        for (Route r : routes) {
            length += r.length();
        }
        return 40 - length;
    }

    /**
     *
     * @return (int) the number of tickets the player owns
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     *
     * @return (int) the number of cards the player owns
     */
    public int cardCount() {
        return carCount;
    }

    /**
     *
     * @return (List<Routes>) all the roads the player owns
     */
    public List<Route> routes(){
        return routes;
    }

    /**
     *
     * @return (int) the number of wagons the player owns
     */
    public int carCount() {
        return carCount;
    }

    /**
     *
     * @return (int) the number of construction's points earned by the player
     */
    public int claimPoints() {
        return claimPoints;
    }
}
