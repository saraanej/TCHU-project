package ch.epfl.tchu.game;

import ch.epfl.tchu.game.StationPartition;
import ch.epfl.tchu.game.StationPartition.Builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStationPartition {


    StationPartition.Builder r = new StationPartition.Builder(6);

    Station zero = ChMap.stations().get(0);
    Station one = ChMap.stations().get(1);
    Station two = ChMap.stations().get(2);
    Station three = ChMap.stations().get(3);
    Station four = ChMap.stations().get(4);
    Station five = ChMap.stations().get(5);


    @Test
    void stationPartitionConstructorFailsForNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StationPartition.Builder(-3);
        });

    }

    @Test
    void TestInstancierStationPartition(){
        StationPartition.Builder r = new StationPartition.Builder(6);

        for(int i = 0; i < r.getStations().length; i++ ){
            assertEquals(i, r.getStations()[i]);
        }
   }

   @Test
    void TestBuilderConnected(){

       r.connect(zero,one);
       assertEquals(0, r.getStations()[1]);

   }

   @Test
    void TestNewTabConnectCorrect(){

       r.connect(zero,three);
       r.connect(five,one);
       r.connect(two,four);

       assertEquals(0, r.getStations()[0]);
       assertEquals(5, r.getStations()[1]);
       assertEquals(2, r.getStations()[2]);
       assertEquals(0, r.getStations()[3]);
       assertEquals(2, r.getStations()[4]);
       assertEquals(5, r.getStations()[5]);

       /**
        * rep(0) =
        * rep(1) =
        * rep(2) =
        * rep(3) =
        * rep(4) =
        * rep(5) =
        */

   }

   @Test
    void TestBuilderBuild(){

       r.connect(zero,three);
       r.connect(five,one);
       r.connect(two,four);

        r.connect(two,zero);

       assertEquals(2, r.getStations()[0]);
       assertEquals(2, r.getStations()[2]);

       r.connect(five,two);

       assertEquals(2, r.getStations()[0]);
       assertEquals(5, r.getStations()[1]);
       assertEquals(5, r.getStations()[2]);
       assertEquals(0, r.getStations()[3]);
       assertEquals(2, r.getStations()[4]);
       assertEquals(5, r.getStations()[5]);

       r.build();

       for(int i = 0; i < r.getStations().length; ++i){
           assertEquals(5, r.getStations()[i]);
       }
   }

    int[] lu = new int[]{3,2,4,3,4,5};

    //StationPartition s = new StationPartition(lu);

    @Test
    void TestConnectedStationPartition(){
        StationPartition.Builder r = new StationPartition.Builder(4);

        r.connect(two,one);

        assertEquals(3, r.getStations()[3]);
        assertEquals(2,r.getStations()[1]);


        StationPartition fi = r.build();

        assertTrue(fi.connected(one,two));

        assertTrue(fi.connected(two,two));

        assertTrue(fi.connected(five,five));

        //assertTrue(fi.connected(five,one));

        //assertTrue(fi.connected(zero,three));



    }
}



