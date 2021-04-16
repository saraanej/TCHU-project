/**
 * 
 */
package ch.epfl.tchu.game;

/**
 * The StationConnectivity interface of the ch.epfl.tchu.game package, public,
 * Modelizes the connection between two stations of the player,
 * represents the connectivity of a player's network,
 * i.e. whether two stations in the tCHu network are connected or not by the player's network in question.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public interface StationConnectivity {

	/**
	 * Checks if two stations are connected or not
	 * @param s1 (Station) the first station
	 * @param s2 (Station) the second station
	 * @return (boolean) true if the stations s1 and s2 are connected
	 * Note: needs to be Override
	 */
	 boolean connected(Station s1 ,Station s2);
}
