package ch.epfl.tchu.game;

import java.util.List;

/**
 *  Modelizes a trail
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class Trail { // Note : rendre la classe immuable
	
	/**
	 * 
	 * @param (List<Route>) routes :
	 * @return (Trail) the longest path in the given list of routes
	 */
	static Trail longest(List<Route> routes) {
		return null;
	}
	
	/**
	 * @return (int) the trail's length
	 */
	public int length() {
		return 0;
	}
	
	/**
	 * @return (Station) the first station of the trail. null iff the trail's length is zero
	 */
	public Station station1() {
		return null;
	}
	
	/**
	 * @return (Station) the last station of the trail. null iff the trail's length is zero
	 */
    public Station station2() {
		return null;
	}
    
    @Override
    public String toString() {
    	return null;
    }

}
