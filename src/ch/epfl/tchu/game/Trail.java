package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Modelizes a trail
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class Trail { 
	
	private final int length;
	private final Station station1;
	private final Station station2;
	private final List<Route> routes;
	
	
	/**
	 * private constructor for a Trail
	 * 
	 * @param station1 (Station) the departure's Station of the Trail
	 * @param station2 (Station) the arrival's Station of the Trail
	 * @param routes (List<Route>) all the roads taken by the Trail 
	 */
	private Trail(Station station1, Station station2, List<Route> routes) {
		this.station1 = station1;
		this.station2 = station2;
		this.routes = List.copyOf(routes);
		this.length = computeLength();
	}
	
	
	/**
	 * 
	 * @param routes (List<Route>) :
	 * @return (Trail) the longest path in the given list of routes
	 */
	public static Trail longest(List<Route> routes) {
		
		Trail longestTrail = new Trail(null, null, List.of());
		
		if (routes == (null) || routes.isEmpty()) {
			return longestTrail;
		}
		
		List<Trail> trails = new ArrayList<>(); 
		for (Route r : routes) {
			trails.add(new Trail(r.station1(),r.station2(),List.of(r)));
			trails.add(new Trail(r.station2(),r.station1(),List.of(r)));
		}
		while (!trails.isEmpty()) {
			List<Trail> cs = new ArrayList<>();
			for (Trail t : trails) {	
				List<Route> road = new ArrayList<>(routes);
				road.removeAll(t.routes);
				for (Route r : road) { 
					if ((r.station1().equals(t.station2)) || (r.station2().equals(t.station2))) { 
						List<Route> newRoad = new ArrayList<>(t.routes);
						newRoad.add(r); 
						Trail newTrail = new Trail(t.station1, r.stationOpposite(t.station2), newRoad);
						cs.add(newTrail); 
					}
				}
				if (longestTrail.length < t.length) {
					longestTrail = t;
				}
			}
			trails = cs;
		}
		return longestTrail;
	}
	
	/**
	 * @return (int) the trail's length
	 */
	public int length() {
		return this.length;
	}
	
	/**
	 * @return (Station) the first station of the trail. null if the trail's length is zero
	 */
	public Station station1() {
		return this.station1;
	}
	
	/**
	 * @return (Station) the last station of the trail. null if the trail's length is zero
	 */
    public Station station2() {
    	return this.station2;
	}
    
    @Override
    public String toString() {
    	if (length == 0) {
    		return "";
    	}
    	return String.format("%s - %s (%s)", station1, station2, length);
    }
    

	private int computeLength() {
		if (routes == null || routes.isEmpty()) {
			return 0;
		}
		int l=0;
		for (Route r : routes){
			l += r.length();
		}
		return l;
	}
	

}
