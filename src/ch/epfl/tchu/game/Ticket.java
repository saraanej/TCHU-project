package ch.epfl.tchu.game;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import ch.epfl.tchu.Preconditions;


/**
 * Modelizes a Ticket
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public final class Ticket implements Comparable<Ticket> { 

	private final Station from;
	private final List<Trip> trips;
	private final String Text;

	/**
	 * Public default constructor of a Ticket
	 * @param trips (List<Trip>) all the Trips that the Ticket covers
	 * @throws IllegalArgumentException 
	             if List<Trips> is empty 
	             if all the departure's stations don't have the same name
	 */
	public Ticket(List<Trip> trips){
		Preconditions.checkArgument(trips != null);
		Preconditions.checkArgument(trips.size() != 0);
		this.from = trips.get(0).from();
		for ( Trip t : trips) {
			Preconditions.checkArgument(t.from().name().equals(this.from.name()));
		}
		this.trips = trips;
		this.Text = computeText(this.trips);
	}

	/**
	 * Public constructor of a Ticket
	 * @param from (Station) the departure Station of the Ticket's Trip
	 * @param to (Station) the arrival Station of the Ticket's Trip
	 * @param points (int) the number of points associated to the Ticket's Trip
	 */
	public Ticket(Station from, Station to, int points) {
		this(List.of(new Trip(from, to, points)));
	}

	/**
	 * @return (String) the textual representation of the Ticket
	 */
	public String text() {
		return Text;
	}

	/**
	 * @param connectivity (StationConnectivity) 
	 * @return (int) the maximal points if at least two Stations are connected, 
	                 - minimal points possible if none of the Stations are connected
	 */
	public int points(StationConnectivity connectivity) {
		int maxPoints;
		List<Integer> Points = new ArrayList<Integer>();
		for (Trip t : trips ) {
			int pts = t.points(connectivity);
			Points.add(pts);
		}
		maxPoints = Collections.max(Points);
		return maxPoints;
		
	}
	 
	/**
	 * Compares alphabetically this ticket's text with another Ticket's text 
	 * @param that (Ticket) the Ticket to compare
	 * @return (int) a strictly positive number if this is bigger than that
	                 a strictly negative number if this is smaller than that
	                 0 if they are equal
	 */
	@Override
	public int compareTo(Ticket that) {
		return this.Text.compareTo(that.Text);
	}

	/**
	 * Overrides the toString() method
	 * @return (String) the textual representation of the Ticket
	 */
	@Override 
	public String toString() {
		return text();
	}

	/**
	 * Private method to compute the textual representation of the Ticket
	 * @param trips (List<Trip>) all the Trips that the Ticket covers 
	 * @return (String) the textual representation of the Ticket
	 */
	private static String computeText(List<Trip> trips) {
		String text ;
		if (trips.size() == 1) {			
			text = String.format("%s - %s (%s)", 
					             trips.get(0).from().name(), 
					             trips.get(0).to().name(), 
					             trips.get(0).points());
		} else {
			TreeSet<String> countries = new TreeSet<>();
			for (Trip t : trips ) {
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
