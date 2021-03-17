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

    private final int links[];

    /**
     *
     * private constructor
     *
     * @param links (int[]) : list containing the links linking each element to the representative of their subset
     */
    private StationPartition(int[] links){ // RENDRE PRIVATE

        int[] newTab = (int[]) links.clone();

        this.links = newTab;

    }


    /**
     * verifies if the two given stations are connected
     *
     * @param s1 (Station) the first given station
     * @param s2 (Station) the second given station
     *
     * @return true if the two stations are connected. false if not.
     */
    @Override
    public boolean connected(Station s1, Station s2) {

        if (s1.id() >= links.length || s2.id() >= links.length){

            return s1.id() == s2.id() ? true : false; // comparer les id

        }else{

            return (links[s1.id()] == links[s2.id()]) ? true : false; // comparer les représentants

        }
    }


    /**
     * The station partition builder
     *
     */
    public final static class Builder{

        private int stations[];

        public int[] getStations() { // A SUPPRIMER
            return stations;
        }

        /**
         * default constructor
         *
         * @param stationCount (int) : the identity of the station partition
         * @throws IllegalArgumentException
         *             if the stationCount is a negative number
         */
        public Builder(int stationCount){

            Preconditions.checkArgument(stationCount >= 0);

            stations = new int[stationCount];

            for(int i = 0; i < stationCount; i++ ){
                stations[i] = i;
            }

        }

        /**
         *
         * joins the subsets containing the two stations by electing one of the two representatives
         * as representative of the joined subset
         *
         * @param s1 (Station) : the first given station
         * @param s2 (Station) : the second given station
         * @return (Builder) the builder this
         */
        public Builder connect(Station s1, Station s2){

            int repS1 = representative(s1.id());
            int repS2 = representative(s2.id());

            stations[repS2] = repS1;

            return this;
        }

        /**
         *
         * @return (StationPartition) the station partition corresponding to the profound partition that is being created
         */
        public StationPartition build(){

            for(int i = 0; i < stations.length; i++){

                if(stations[i] != representative(i)){

                    stations[i] = representative(i);
                }
            }

            StationPartition flattened = new StationPartition(stations);

            return flattened;
        }

        /**
         *
         * @param stationId (int) : a station identification number
         * @return the identification number of the representative of the subset containing the station
         */
        private int representative(int stationId){

            int representative = stations[stationId];
            int index = stationId;

            if(index == representative){

                return stationId;

            } else {

                do{
                    index = representative;
                    representative = stations[index];

                } while(representative != index);

                return representative;
            }
        }
    }
}