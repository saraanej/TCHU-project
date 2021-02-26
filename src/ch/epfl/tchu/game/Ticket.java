package ch.epfl.tchu.game;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;


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
	 * 
	 * @param trips (List<Trip>) all the Trips that the Ticket covers
	 * 
	 * @throws IllegalArgumentException if List<Trips> is empty
	 */
	public Ticket(List<Trip> trips){

		Preconditions.checkArgument(trips != null);
		
		this.from = trips.get(0).from();
		
		for ( Trip t : trips) {
			assert t.from().equals(this.from);
		}
		this.trips = trips;
		this.Text = computeText(this.trips); 

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
	

	
	private static String computeText(List<Trip> trips) {
		
		if (trips.size() == 1) {
			
		} else {
			
		}

		
		return null;
	}
	
	
	public String text() {
		return Text;
	}
	
	public int points(StationConnectivity connectivity) {
	//	return connectivity.connected(from, s2);
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
