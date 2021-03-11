package ch.epfl.tchu.game;

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


    private final List<Integer> links;

    /**
     *
     * @param links
     */
    private StationPartition(List<Integer> links){
        this.links = List.copyOf(links);
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
     * @param s1 (Station) the first station
     * @param s2 (Station) the second station
     *
     * @return
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        return false;
    }


    /**
     *
     */
    public final static class Builder{

        /**
         *
         * @param stationCount
         */
        public Builder(int stationCount){

        }

        /**
         *
         * @param s1
         * @param s2
         * @return
         */
        public Builder connect(Station s1, Station s2){
            return null;
        }

        /**
         *
         * @return
         */
        public StationPartition build(){
            return null;
        }

    }
}
