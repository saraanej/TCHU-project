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
     * index du tableau = id des stations
     *
     * private constructor
     *
     * @param links (int[]) : list containing the links linking each element to the representative of their subset
     */
    private StationPartition(int[] links){

        this.links = links;

    }


    /**
     * Dans le constructeur de StationPartition tu as un tableau reliant
     * l'id de la gare(qui est l'index du tableau) avec celui de son représentent
     * et 2 route reliées ont le même représentent.
     *
     *
     * dans le cas où elles sont dans le réseaux tu dois simplement vérifier si les représentants des deux stations sont les mêmes
     * et dans le cas contraire tu renvoies si oui ou non les id() sont les mêmes.
     *
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

        private final int stationCount;
        private final int st[];


        /**
         * default constructor
         *
         * @param stationCount (int) : the identity of the station partition
         * @throws IllegalArgumentException
         *             if the stationCount is a negative number
         */
        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount >= 0);

            this.stationCount = stationCount;
            st = new int[ChMap.stations().size()];

            for(int i = 0; i < ChMap.stations().size(); i++) {
                st[i] = i;
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

            return this;
        }

        /**
         *
         * aplatir la représentation puis
         * appeler le constructeur de StationPartition avec cette version aplatie de la représentation
         *
         * @return (StationPartition) the station partition corresponding to the profound partition that is being created
         */
        public StationPartition build(){

            return null;
        }

        /**
         *
         * @param stationId (int) : a station identification number
         * @return the identification number of the representative of the subset containing the station
         */
        private int representative(int stationId){

            return st[stationId];

        }


    }
}
