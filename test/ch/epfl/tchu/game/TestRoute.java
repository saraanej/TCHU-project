package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

//import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Route.Level;

public class TestRoute {
	
    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station BER = new Station(3, "Berne");
    private static final Station LUC = new Station(16, "Lucerne");


    private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK);
    private static final Route E = new Route("BER_LUC_1", BER, LUC, 6, Level.UNDERGROUND, null);
    
    @Test
	void testID() {
		assertEquals("NEU_YVE_1",A.id());
	}
	
	@Test
	void testStation1() {
		assertEquals(NEU, A.station1());
	}
	
	@Test
	void testStation2() {
		assertEquals(YVE, A.station2());
	}
	
	@Test
	void testLength() {
		assertEquals(2, A.length());
	}
	
	@Test
	void testLevel() {
		assertEquals(YVE, A.station2());
	}
	
	@Test
	void testColor() {
		assertEquals(Color.BLACK, A.color());
	}
	
	@Test
	void ConstructorWithWrongArguments() {
//		assertThrows(IllegalArgumentException.class, () -> {new Route("NEU_YVE_1", YVE, YVE, 2, Level.OVERGROUND, Color.BLACK);});
//		assertThrows(IllegalArgumentException.class, () -> {new Route("NEU_YVE_1", NEU, YVE, -1, Level.OVERGROUND, Color.BLACK);});
		assertThrows(NullPointerException .class, () -> {new Route("Neuchatal", null, YVE, 2, Level.OVERGROUND, Color.BLACK);});
	}

	@Test
	void listStations() {
		assertEquals(NEU, A.stations().get(0));
		assertEquals(YVE, A.stations().get(1));
	}
	
	@Test
	void oppositeStations() {
//		assertEquals(NEU, A.stationOpposite(YVE));
		assertThrows(IllegalArgumentException.class, () -> {A.stationOpposite(BER);});
	}
	
	@Test
	void Points() {
		assertEquals(15, E.claimPoints());
	}
	
	@Test
	void claim() {
	    Route R = new Route("NEU_YVE_1", NEU, YVE, 2, Level.UNDERGROUND, null);

		List<SortedBag<Card>> possibleClaimCard = new ArrayList<SortedBag<Card>>();
		possibleClaimCard.add(SortedBag.of(1, Card.BLACK));
		possibleClaimCard.add(SortedBag.of(1, Card.LOCOMOTIVE));
		assertEquals(possibleClaimCard, R.possibleClaimCards());

	}
	
	@Test
	void additional() {

		 Route route = new Route("BER_LUC_1", BER, LUC, 6, Level.UNDERGROUND, null);


		SortedBag<Card> claimcards = SortedBag.of(1, Card.BLUE);
		
		SortedBag<Card> drawncards = SortedBag.of(2,Card.LOCOMOTIVE, 1, Card.BLUE);
		
//		assertTrue(claimcards.isEmpty());
//		assertThrows(IllegalArgumentException.class, () -> {A.additionalClaimCardsCount(claimcards,drawncards);});
	    assertEquals(1,route.additionalClaimCardsCount(claimcards,drawncards));
	}
	

}
