package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * The StationPartition class, public, final and immutable, represents a (flattened) partition of stations.
 * It implements the StationConnectivity interface, because its instances are meant to be passed to the points method of Ticket.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public final class StationPartition implements StationConnectivity {

    private final int[] links;


    /**
     * Private constructor.
     *
     * @param links (int[]) : List containing the links linking each element to the representative of their subset.
     */
    private StationPartition(int[] links) {
        this.links = links.clone();
    }


    @Override
    public boolean connected(Station s1, Station s2) {
        if (s1.id() >= links.length || s2.id() >= links.length)
             return s1.id() == s2.id();
        else
             return links[s1.id()] == links[s2.id()];
    }


    /**
     * The station partition builder.
     */
    public static final class Builder {

        private final int[] stations;


        /**
         * Default constructor.
         *
         * @param stationCount (int) : The identity of the station partition.
         * @throws IllegalArgumentException if the stationCount is a negative number.
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            stations = new int[stationCount];
            for (int i = 0; i < stationCount; ++i) {
                stations[i] = i;
            }
        }


        /**
         * Joins the subsets containing the two stations by electing one of the two representatives
         * as representative of the joined subset.
         *
         * @param s1 (Station) : The first given station.
         * @param s2 (Station) : The second given station.
         * @return (Builder) The builder this.
         */
        public Builder connect(Station s1, Station s2) {
            int repS1 = representative(s1.id());
            int repS2 = representative(s2.id());
            stations[repS1] = repS2;
            return this;
        }

        /**
         * @return (StationPartition) The station partition corresponding to the profound partition that is being created.
         */
        public StationPartition build() {
            for (int i = 0; i < stations.length; i++) {
                stations[i] = representative(i);
            }
            return new StationPartition(stations);
        }


        /**
         * @param stationId (int) : A station identification number.
         * @return The identification number of the representative of the subset containing the station.
         */
        private int representative(int stationId) {
            int representative = stations[stationId];
            int index = stationId;
            if (index != representative) {
                do {
                    index = representative;
                    representative = stations[index];
                } while (representative != index);
            }
            return representative;
        }
    }
}