/**
 * 
 */
package ch.epfl.tchu.game;

/**
 * Modelizes the connection between two stations of the player
 * 
 * @author Yasmin Ben Rahhal
 * @author Sara Anejjar
 *
 */
public interface StationConnectivity {

	/**
	 * Checks if two stations are connected or not
	 * 
	 * @param s1 (Station) the first station
	 * @param s2 (Station) the second station
	 * 
	 * @return (boolean) true if the stations s1 and s2 are connected
	 * 
	 * Note: needs to be Override
	 */
	public abstract boolean connected(Station s1 ,Station s2);
}
