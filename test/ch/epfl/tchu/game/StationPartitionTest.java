package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {

    private Station LUC = new Station(16, "Lucerne");
    private Station LUG = new Station(17, "Lugano");
    private Station MAR = new Station(18, "Martigny");
    private Station NEU = new Station(19, "Neuchâtel");
    private Station OLT = new Station(20, "Olten");
    private Station PFA = new Station(21, "Pfäffikon");
    private Station SAR = new Station(22, "Sargans");
    private Station SCE = new Station(23, "Schaffhouse");
    private Station SCZ = new Station(24, "Schwyz");

    @Test
    void partitionWithOnlyOneConnection(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(OLT, OLT));
    }

    @Test
    void partitionWithTwoConnectionsOfDifferentSets(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        builder.connect(PFA, SAR);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(OLT, OLT));
        assertTrue(thePartition.connected(PFA, SAR));
        assertFalse(thePartition.connected(LUC, SAR));
    }

    @Test
    void partitionWithTwoConnectionsOfTheSameSet(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        builder.connect(NEU, SAR);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(OLT, OLT));
        assertFalse(thePartition.connected(PFA, SAR));
        assertTrue(thePartition.connected(LUC, SAR));
        assertTrue(thePartition.connected(SAR, NEU));
    }

    @Test
    void partitionWithTwoConnectionsOfTheSameSetDifferentOrder(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        builder.connect(SAR, NEU);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(OLT, OLT));
        assertFalse(thePartition.connected(PFA, SAR));
        assertTrue(thePartition.connected(LUC, SAR));
        assertTrue(thePartition.connected(SAR, NEU));
    }

    @Test
    void partitionWithOneSetOfManyStations(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        builder.connect(SAR, NEU);
        builder.connect(SCE, MAR);
        builder.connect(SCE,NEU);
        builder.connect(MAR,OLT);
        StationPartition thePartition = builder.build();
        assertTrue(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(OLT, OLT));
        assertTrue(thePartition.connected(LUC, OLT));
        assertTrue(thePartition.connected(MAR, SAR));
        assertFalse(thePartition.connected(PFA, SAR));
        assertFalse(thePartition.connected(LUG, SCE));
        assertFalse(thePartition.connected(LUG, PFA));
        assertFalse(thePartition.connected(NEU, PFA));
    }

    @Test
    void partitionWithManySets(){
        StationPartition.Builder builder = new StationPartition.Builder(27);
        //First Set
        builder.connect(LUC, NEU);
        //Second Set
        builder.connect(SAR, MAR);
        builder.connect(SCE, MAR);
        //Third Set
        builder.connect(OLT,PFA);
        builder.connect(PFA,SCZ);
        StationPartition thePartition = builder.build();
        assertTrue(thePartition.connected(LUC, NEU));
        assertTrue(thePartition.connected(SCE, SAR));
        assertTrue(thePartition.connected(PFA, OLT));
        assertTrue(thePartition.connected(SCZ, OLT));
        assertTrue(thePartition.connected(MAR, MAR));
        assertFalse(thePartition.connected(LUG, NEU));
        assertFalse(thePartition.connected(OLT, LUC));
        assertFalse(thePartition.connected(MAR, PFA));
        assertFalse(thePartition.connected(LUC, SCZ));
    }

    @Test
    void partitionWithManyTimesTheSameConnection(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        builder.connect(LUC, NEU);
        builder.connect(NEU,LUC);
        builder.connect(OLT,OLT);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertTrue(thePartition.connected(LUC, NEU));
        assertFalse(thePartition.connected(OLT,NEU));
        assertTrue(thePartition.connected(OLT, OLT));
    }

    @Test
    void partitionWhenDoingNothing(){
        StationPartition.Builder builder = new StationPartition.Builder(24);
        StationPartition thePartition = builder.build();
        assertFalse(thePartition.connected(LUC, MAR));
        assertFalse(thePartition.connected(LUC, NEU));
        assertFalse(thePartition.connected(OLT,NEU));
        assertTrue(thePartition.connected(OLT, OLT));
    }

    @Test
    void failWithNegativeStationCount(){
        assertThrows(IllegalArgumentException .class, ()->{
            StationPartition.Builder builder = new StationPartition.Builder(-2);
        });
    }


}