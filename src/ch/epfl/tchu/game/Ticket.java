package ch.epfl.tchu.game;

import java.util.List;
import java.util.TreeSet;
import ch.epfl.tchu.Preconditions;

/**
 * The Ticket class public, final and immutable,
 * represents a Ticket.
 * A tCHu ticket is made up of a starting point and an ending point.
 * A tCHu ticket is worth a certain number of points, which are those that the player
 * who owns it will gain (resp. Lose) at the end of the game if he has succeeded (resp. Failed)
 * in connecting the starting point to the point arrival with its own wagons cars.
 * There are three types of tCHu tickets: city to city, city to country, country to country.
 * The tCHu game has a total of 46 tickets:
 * 34 city-to-city tickets, 4 city-to-country tickets,and 8 country-to-country tickets.
 * Country-to-country Tickets are available in two copies each, while all other Tickets are unique.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String text;


    /**
     * Public constructor of a Ticket made up of the list of trips given.
     *
     * @param trips all the Trips that the Ticket covers
     * @throws IllegalArgumentException if List<Trips> is empty
     *                                  if all the departure's stations don't have the same name
     */
    public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(trips != null);
        Preconditions.checkArgument(trips.size() != 0);
        Station from = trips.get(0).from();
        for (Trip t : trips) {
            Preconditions.checkArgument(t.from().name().equals(from.name()));
        }
        this.trips = List.copyOf(trips);
        this.text = computeText(this.trips);
    }

    /**
     * Public constructor of a Ticket consisting of a single Trip, whose attributes are from, to and points.
     *
     * @param from   the departure Station of the Ticket's Trip
     * @param to     the arrival Station of the Ticket's Trip
     * @param points the number of points associated to the Ticket's Trip
     */
    public Ticket(Station from, Station to, int points) {
        this(List.of(new Trip(from, to, points)));
    }

    /**
     * @param connectivity
     * @return the maximal points if at least two Stations are connected,
     *                -1 * minimal points possible if none of the Stations are connected
     */
    public int points(StationConnectivity connectivity) {
        int maxPoints = trips.get(0).points(connectivity);
        for (Trip t : trips) {
            int tempPoints = t.points(connectivity) ;
            if (tempPoints > maxPoints) maxPoints = tempPoints;
        }
        return maxPoints;
    }

    /**
     * @return the textual representation of the Ticket
     */
    public String text() {
        return text;
    }


    /**
     * @see Integer#compareTo(Integer)
     *
     * Compares alphabetically this ticket's text with another Ticket's text.
     * @param that the Ticket to compare with
     * @return  a strictly positive number if this is bigger than that
	 *          a strictly negative number if this is smaller than that
                0 if they are equal
     */
    @Override
    public int compareTo(Ticket that) {
        return this.text.compareTo(that.text);
    }

    /**
     * @see String#toString()
     *
     * Returns a string representation of this Ticket.
     * The string representation is of form :
     * if it's a city to city Ticket : "departure city - arrival city (points of the Ticket)".
     * if it's a city to country Ticket: "departure city - {list of arrival countries with its respective points}".
     * if it's a country to country Ticket: "departure country - {list of arrival countries with its respective points}".
     */
    @Override
    public String toString() {
        return text;
    }


    /**
     * Private static method to compute the textual representation of a Ticket made up of the list of trips given.
     *
     * @param trips all the Trips that the Ticket covers
     * @return the textual representation of the Ticket
     */
    private static String computeText(List<Trip> trips) {
        String text;
        if (trips.size() == 1)
            text = String.format("%s - %s (%s)",
                    trips.get(0).from().name(),
                    trips.get(0).to().name(),
                    trips.get(0).points());
        else {
            TreeSet<String> countries = new TreeSet<>();
            for (Trip t : trips) {
                countries.add(String.format("%s (%s)",
                        t.to().name(),
                        t.points()));
            }
            String arrivals = String.join(", ", countries);
            text = String.format("%s - {%s}",
                    trips.get(0).from().name(),
                    arrivals);
        }
        return text;
    }
}
