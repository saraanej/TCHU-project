package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {

    //à mettre dans StationPartition pour récuperer le tableau avec les représentants
    /*public int[] getLiens(){
        return liens;
    }*/

    private static final Station OLT = new Station(20, "Olten");
    private static final Station BER = new Station(3, "Berne");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");

    //for method id out of bounds
    private static final Station SCZ = new Station(24, "Schwyz");
    private static final Station ZUR = new Station(33, "Zürich");


    @Test
    public void stationPartitionBuilderTestWithTwoStations(){

        StationPartition.Builder builder = new StationPartition.Builder(17);
        builder.connect(BER, LUC);
        StationPartition something = builder.build();
        assertTrue(something.connected(BER, LUC));

        //sameRepresentative
        int[] liens = something.getLiens();
        assertEquals(liens[16], liens[3]);
        assertEquals(liens[3], liens[16]);
    }

    @Test
    public void stationPartitionBuilderWithMultipleStations(){

        //connect 7 stations
        StationPartition.Builder builder = new StationPartition.Builder(17);
        builder.connect(BER, LUC);
        builder.connect(BER, BAD);
        builder.connect(BAL, BEL);
        builder.connect(BAL, BAD);
        builder.connect(BER, BRI);
        builder.connect(BAD, BRU);
        StationPartition something = builder.build();
        List<Station> stations = List.of(BER, LUC, BAD, BAL, BEL, BRI, BRU);

        //methodConnect
        for(Station station : stations){
            for(Station s : stations){
                assertTrue(something.connected(station, s));
            }
        }

        //same representative
        int[] liens = something.getLiens();

        for(int i=0; i<6; i++){
            assertEquals(liens[i], liens[0]);
            assertEquals(liens[i], liens[1]);
            assertEquals(liens[i], liens[2]);
            assertEquals(liens[i], liens[3]);
            assertEquals(liens[i], liens[4]);
            assertEquals(liens[i], liens[5]);
            assertEquals(liens[i], liens[16]);
        }
        for (int i = 16; i<17; i++){
            assertEquals(liens[i], liens[0]);
            assertEquals(liens[i], liens[1]);
            assertEquals(liens[i], liens[2]);
            assertEquals(liens[i], liens[3]);
            assertEquals(liens[i], liens[4]);
            assertEquals(liens[i], liens[5]);
            assertEquals(liens[i], liens[16]);
        }

        //to be sure that the stations that are not connected are their own representative
        for(int i = 6; i<16; i++){
            assertEquals(liens[i], liens[i]);
        }
    }

    @Test
    public void connectedStationIdOutOfBoundsOfChartGivenToConstructor(){

        StationPartition.Builder builder = new StationPartition.Builder(17);
        builder.connect(BER, LUC);
        StationPartition something = builder.build();

        //returns false with an id out of bounds
        assertFalse(something.connected(BER, SCZ));
        assertFalse(something.connected(BER, ZUR));
        assertFalse(something.connected(LUC, SCZ));
        assertFalse(something.connected(LUC, ZUR));

        //both stations with id out of bounds
        assertFalse(something.connected(ZUR, SCZ));

        //returns true iff both stations have the same id
        assertTrue(something.connected(SCZ, SCZ));


    }

    @Test
    public void connectedReturnsFalseIfNotConnected(){
        StationPartition.Builder builder = new StationPartition.Builder(17);
        builder.connect(BER, LUC);
        StationPartition something = builder.build();
        assertFalse(something.connected(BER, BAD));
        assertFalse(something.connected(LUC, BAD));
    }

    @Test
    public void stationPartitionWithNegativeAmountOfStations(){
        assertThrows(IllegalArgumentException.class, ()->{
            new StationPartition.Builder(-1);
        });
    }


}