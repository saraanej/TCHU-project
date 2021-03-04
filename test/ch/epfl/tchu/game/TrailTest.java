package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.game.Route.Level;

class TrailTest {
	
	
	@Test
	void longestTrailWorksWithSomeRoutes() {
		TestMap map = new TestMap();
		List<Route> routes = List.of(map.A, map.B, map.C, map.D, map.D, map.E, map.F, map.G);
		assertEquals(13, Trail.longest(routes).length());
	}
	
	@Test
	void longestTrailWorksWithSomeRoutes2() {
		TestMap map = new TestMap();
		List<Route> routes = List.of(map.A, map.B, map.C, map.D, map.D, map.E, map.F, map.G);
		assertEquals(13, Trail.longest(routes).length());
	}
	
	@Test
	void longestTrailWorksWithNullRoutes() {
		TestMap map = new TestMap();
		List<Route> routes = List.of();
		assertEquals(0, Trail.longest(null).length());
	}
	
	@Test
	void toStringWorksOnNormalTrail() {
		TestMap map = new TestMap();
		List<Route> routes = List.of(map.A, map.B, map.C, map.D, map.D, map.E, map.F, map.G);
		assertEquals("Lucerne - Fribourg (13)", Trail.longest(routes).toString());
	}
	
	@Test
	void toStringWorksOnTrivialTrail2() {
		TestMap map = new TestMap();
		List<Route> routes = List.of(map.A, map.B, map.C, map.D, map.F);
		Trail trail = Trail.longest(routes); 
		
		assertEquals("Yverdon - Neuchâtel (10)", trail.toString());
	}
	
	@Test
	void toStringWorksOnTrivialTrail3() {
		TestMap map = new TestMap();
		List<Route> routes = List.of( map.B, map.C, map.D,map.G);
		Trail trail = Trail.longest(routes); 
		assertEquals("Soleure - Olten (9)", trail.toString());
	}
	
	@Test
	void toStringWorksOnTrivialTrail4() {
		TestMap map = new TestMap();
		List<Route> routes = List.of( map.A, map.B);
		Trail trail = Trail.longest(routes); 
		assertEquals("Yverdon - Soleure (6)", trail.toString());
	}
	
	
	@Test
	void toStringWorksWithNullTrail() {
		Trail nullTrail = Trail.longest(null);
		assertEquals("", nullTrail.toString());

	}
	
	@Test
	void lenghtWorksWithNullTrail() {
		Trail nullTrail = Trail.longest(null);
		assertEquals(0, nullTrail.length());

	}
	
	@Test
	void station1WorksWithNullTrail() {
		Trail nullTrail = Trail.longest(null);
		assertEquals(null, nullTrail.station1());

	}
	
	@Test
	void station2WorksWithNullTrail() {
		Trail nullTrail = Trail.longest(null);
		assertEquals(null, nullTrail.station2());

	}
	
	
	
	

	 private static final class TestMap {
		 
		    // Stations - cities
		    private static final Station BAD = new Station(0, "Baden");
		    private static final Station BAL = new Station(1, "Bâle");
		    private static final Station BEL = new Station(2, "Bellinzone");
		    private static final Station BER = new Station(3, "Berne");
		    private static final Station BRI = new Station(4, "Brigue");
		    private static final Station BRU = new Station(5, "Brusio");
		    private static final Station COI = new Station(6, "Coire");
		    private static final Station DAV = new Station(7, "Davos");
		    private static final Station DEL = new Station(8, "Delémont");
		    private static final Station FRI = new Station(9, "Fribourg");
		    private static final Station GEN = new Station(10, "Genève");
		    private static final Station INT = new Station(11, "Interlaken");
		    private static final Station KRE = new Station(12, "Kreuzlingen");
		    private static final Station LAU = new Station(13, "Lausanne");
		    private static final Station LCF = new Station(14, "La Chaux-de-Fonds");
		    private static final Station LOC = new Station(15, "Locarno");
		    private static final Station LUC = new Station(16, "Lucerne");
		    private static final Station LUG = new Station(17, "Lugano");
		    private static final Station MAR = new Station(18, "Martigny");
		    private static final Station NEU = new Station(19, "Neuchâtel");
		    private static final Station OLT = new Station(20, "Olten");
		    private static final Station PFA = new Station(21, "Pfäffikon");
		    private static final Station SAR = new Station(22, "Sargans");
		    private static final Station SCE = new Station(23, "Schaffhouse");
		    private static final Station SCZ = new Station(24, "Schwyz");
		    private static final Station SIO = new Station(25, "Sion");
		    private static final Station SOL = new Station(26, "Soleure");
		    private static final Station STG = new Station(27, "Saint-Gall");
		    private static final Station VAD = new Station(28, "Vaduz");
		    private static final Station WAS = new Station(29, "Wassen");
		    private static final Station WIN = new Station(30, "Winterthour");
		    private static final Station YVE = new Station(31, "Yverdon");
		    private static final Station ZOU = new Station(32, "Zoug");
		    private static final Station ZUR = new Station(33, "Zürich");
		    
		    
		    //Routes
		    private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK);
		    private static final Route B = new Route("NEU_SOL_1", NEU, SOL, 4, Level.OVERGROUND, Color.GREEN);
		    private static final Route C = new Route("BER_NEU_1", BER, NEU, 2, Level.OVERGROUND, Color.RED);
		    private static final Route D = new Route("BER_SOL_1", BER, SOL, 2, Level.OVERGROUND, Color.BLACK);
		    private static final Route E = new Route("BER_LUC_1", BER, LUC, 4, Level.OVERGROUND, null);
		    private static final Route F = new Route("BER_FRI_1", BER, FRI, 1, Level.OVERGROUND, Color.ORANGE);
		    private static final Route G = new Route("OLT_SOL_1", OLT, SOL, 1, Level.OVERGROUND, Color.BLUE);
		    
		    
		    //Trails

	 }

}
