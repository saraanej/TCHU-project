/* 
 *Author: Mohamed Benslimane
 * Date :  17 mars 2021
*/

package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.game.StationPartition.Builder;
import ch.epfl.tchu.game.Trail;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StationPartitionTest2 {

    final Station BAD = new Station(0, "Baden");
    final Station BAL = new Station(1, "Bâle");
    final Station BEL = new Station(2, "Bellinzone");
    final Station BER = new Station(3, "Berne");
    final Station BRI = new Station(4, "Brigue");
    final Station BRU = new Station(5, "Brusio");
    final Station COI = new Station(6, "Coire");
    final Station DAV = new Station(7, "Davos");
    final Station DEL = new Station(8, "Delémont");
    final Station FRI = new Station(9, "Fribourg");
    final Station GEN = new Station(10, "Genève");
    final Station INT = new Station(11, "Interlaken");
    final Station KRE = new Station(12, "Kreuzlingen");
    final Station LAU = new Station(13, "Lausanne");
    final Station LCF = new Station(14, "La Chaux-de-Fonds");
    final Station LOC = new Station(15, "Locarno");
    final Station LUC = new Station(16, "Lucerne");
    final Station LUG = new Station(17, "Lugano");
    final Station MAR = new Station(18, "Martigny");
    final Station NEU = new Station(19, "Neuchâtel");
    final Station OLT = new Station(20, "Olten");
    final Station PFA = new Station(21, "Pfäffikon");
    final Station SAR = new Station(22, "Sargans");
    final Station SCE = new Station(23, "Schaffhouse");
    final Station SCZ = new Station(24, "Schwyz");
    final Station SIO = new Station(25, "Sion");
    final Station SOL = new Station(26, "Soleure");
    final Station STG = new Station(27, "Saint-Gall");
    final Station VAD = new Station(28, "Vaduz");
    final Station WAS = new Station(29, "Wassen");
    final Station WIN = new Station(30, "Winterthour");
    final Station YVE = new Station(31, "Yverdon");
    final Station ZOU = new Station(32, "Zoug");

    @Test
    void connectedWorks() {

        Builder builder = new Builder(8);

        builder.connect(BAD, BAL);

        builder.connect(BER, BRI);
        

        StationPartition partition = builder.build();

        assertEquals(true, partition.connected(BAD, BAL));
        assertEquals(false, partition.connected(BAD, DAV));
        assertEquals(false, partition.connected(ZOU, DAV));
        assertEquals(true, partition.connected(WIN, WIN));

    }

    @Test
    void representativeWorks() {
        Builder builder = new Builder(8);

        builder.connect(BAD, BAL);

        builder.connect(BER, BRI);

        builder.connect(BRU, BEL);

        assertEquals(1, builder.representative(0));
        assertEquals(4, builder.representative(3));
        assertEquals(2, builder.representative(5));
        
    }

    /*
    @Test
    void PreconditionsCount() {
        Builder builder = new Builder(-1);
        StationPartition partition = builder.build();

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();

        });

    }

*/
    @Test 
    void connectWorksWithOneConnection() {
        Builder builder = new Builder(2);
        builder.connect(BAD, BAL);
        assertEquals(1, builder.representative(0));
    }
    
   @Test 
   void buildWorksWithTeacherExampleVersionNonAplatie() {
       Builder builder = new Builder(8);
       
       builder.connect(BAD, BRU);
       builder.connect(BAD, COI);
       builder.connect(BEL, DAV);
       builder.connect(DAV, BRU);
       builder.connect(BAL, BER);
       
       assertEquals(6, builder.representative(0));
       assertEquals(3, builder.representative(1));
       assertEquals(6, builder.representative(2));
       assertEquals(3, builder.representative(3));
       assertEquals(4, builder.representative(4));
       assertEquals(6, builder.representative(5));
       assertEquals(6, builder.representative(6));
       assertEquals(6, builder.representative(7));
       
   }
   
   @Test
   void buildWorksWithTeacherExampleVersionAplatie() {
Builder builder = new Builder(8);
       
       builder.connect(BAD, BRU);
       builder.connect(BAD, COI);
       builder.connect(BEL, DAV);
       builder.connect(DAV, BRU);
       builder.connect(BAL, BER);
       
       builder.build();
       
       assertEquals(6, builder.representative(0));
       assertEquals(3, builder.representative(1));
       assertEquals(6 , builder.representative(2));
       assertEquals(3, builder.representative(3));
       assertEquals(4, builder.representative(4));
       assertEquals(6, builder.representative(5));
       assertEquals(6, builder.representative(6));
       assertEquals(6, builder.representative(7));
       
       
   }
    
    
    
    
    
}
