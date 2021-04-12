package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * Modelizes a partition of the stations
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class StationPartition implements StationConnectivity{

    private final int[] links;

    /**
     * private constructor
     * @param links (int[]) : list containing the links linking each element to the representative of their subset
     */
    private StationPartition(int[] links){
        this.links = links.clone();
    }


    /**
     * verifies if the two given stations are connected
     * @param s1 (Station) the first given station
     * @param s2 (Station) the second given station
     * @return true if the two stations are connected. false if not.
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if (s1.id() >= links.length || s2.id() >= links.length){
            return s1.id() == s2.id(); // compare iDs
        }else return links[s1.id()] == links[s2.id()]; // compare representatives
    }


    /**
     * The station partition builder
     */
    public final static class Builder{
        private int[] stations;

        /**
         * default constructor
         * @param stationCount (int) : the identity of the station partition
         * @throws IllegalArgumentException
         *             if the stationCount is a negative number
         */
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount >= 0);
            stations = new int[stationCount];
            for(int i = 0; i < stationCount; ++i){
                stations[i] = i;
            }
        }

        /**
         * joins the subsets containing the two stations by electing one of the two representatives
         * as representative of the joined subset
         * @param s1 (Station) : the first given station
         * @param s2 (Station) : the second given station
         * @return (Builder) the builder this
         */
        public Builder connect(Station s1, Station s2){
            int repS1 = representative(s1.id());
            int repS2 = representative(s2.id());
            stations[repS1] = repS2;
            return this;
        }

        /**
         * @return (StationPartition) the station partition corresponding to the profound partition that is being created
         */
        public StationPartition build(){
            for(int i = 0; i < stations.length; i++){
                    stations[i] = representative(i);
            }
            return new StationPartition(stations);
        }

        /**
         * @param stationId (int) : a station identification number
         * @return the identification number of the representative of the subset containing the station
         */
        public int representative(int stationId){
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