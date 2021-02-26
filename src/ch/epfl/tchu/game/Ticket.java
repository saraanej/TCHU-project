package ch.epfl.tchu.game;

import java.util.ArrayList;

import java.util.Arrays;
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
	 * 
	 * @param trips (List<Trip>) all the Trips that the Ticket covers
	 * 
	 * @throws IllegalArgumentException 
	           if List<Trips> is empty or if all the departure's stations don't have the same name
	 */
	public Ticket(List<Trip> trips){

		Preconditions.checkArgument(trips != null && trips.size() != 0);
		
		this.from = trips.get(0).from();
		
		for ( Trip t : trips) {
			Preconditions.checkArgument(t.from().equals(this.from));
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
			
			String arrivals = String.join(",", countries);
			
			text = String.format("%s - {%s}", 
		            trips.get(0).from().name(), 
		            arrivals);
		}

		
		return text;
	}
	
	
	public String text() {
		return Text;
	}
	
	public int points(StationConnectivity connectivity) {
	
		int maxPoints;
		int minPoints;
		List<Integer> Points = new ArrayList<Integer>();
		
		for (Trip t : trips ) {
			
			int pts = t.points(connectivity);
			Points.add(pts);
		}
		
		maxPoints = Collections.max(Points);
		minPoints = Collections.min(Points);
		
		return maxPoints > 0 ? maxPoints : minPoints;
		
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
