package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Modelizes a Ticket
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public final class Ticket implements Comparable<Ticket> { //NOTE rendre immutable apres

	private final List<Trip> trips;
	private final String Text;
	
	
	/**
	 * Public default constructor of a Ticket
	 * 
	 * @param trips (List<Trip>) all the Trips that the Ticket covers
	 */
	public Ticket(List<Trip> trips){
		this.trips = trips;
		this.Text = computeText(); 
	}
	
	
	/**
	 * Public constructor of a Ticket
	 * 
	 * @param from (Station) the departure Station of the Ticket's Trip
	 * @param to (Station) the arrival Station of the Ticket's Trip
	 * @param points (int) the number of points associated to the Ticket's Trip
	 */
	public Ticket(Station from, Station to, int points) {
		this(List.of(new Trip(from, to, points)));
	}
	
	
	private static String computeText() {
		return null;
	}
	
	
	public String text() {
		return Text;
	}
	
	public int points(StationConnectivity connectivity) {
	//	return connectivity.connected(, s2);
		return 0;
	}
	 
	
	@Override
	public int compareTo(Ticket that) {
		return this.Text.compareTo(that.Text);
	}
	
	@Override 
	public String toString() {
		return text();
	}

}
