package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Route.Level;
import ch.epfl.tchu.game.src.ch.epfl.tchu.gui.Info;

public class InfoTest {
	
	 private static final Station NEU = new Station(19, "Neuchâtel");
	 private static final Station YVE = new Station(31, "Yverdon");
	 private static final Station BER = new Station(3, "Berne");
	 private static final Station LUC = new Station(16, "Lucerne");


	 private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK);
	 private static final Route E = new Route("BER_LUC_1", BER, LUC, 6, Level.UNDERGROUND, null);
	    
	 SortedBag<Card> claimcards = SortedBag.of(2, Card.LOCOMOTIVE);
	
	Info chris = new Info("Chris");
	
	@Test
	public void testCardName() {
		assertEquals("blanche", Info.cardName(Card.WHITE, 1));
	}
	
	@Test
	public void TestWillPlayFirst() {
		assertEquals("Chris jouera en premier.\n\n",
				     chris.willPlayFirst());
	}
	
	@Test 
	public void TestKeptTickets() {
		assertEquals("Chris a gardé 3 billets.\n",
				     chris.keptTickets(3));
	}
	
	@Test 
	public void Testdraw() {
		List<String> names = new ArrayList<>();
		names.add("James");
		names.add("Chris");
		assertEquals("\nJames et Chris sont ex æqo avec 45 points !\n",
				    Info.draw(names, 45));
	}
	
	@Test
	public void TestCanPlay() {
		assertEquals("\nC'est à Chris de jouer.\n",
				     chris.canPlay());
	}
	
	@Test
	public void TestDrewTickets() {
		assertEquals("Chris a tiré 4 billets...\n",
				      chris.drewTickets(4));
	}
	
	@Test
	public void TestDrewBlindCard() {
		assertEquals("Chris a tiré une carte de la pioche.\n",
				     chris.drewBlindCard());
	}
	
	@Test
	public void TestDrewVisibleCard() {
		assertEquals("Chris a tiré une carte jaune visible.\n",
				    chris.drewVisibleCard(Card.YELLOW));
	}
	
	@Test
	public void TestClaimedRoute() {
		assertEquals("Chris a pris possession de la route %s au moyen de %s.\n",
				     chris.claimedRoute(A, claimcards));
	}
	
	

}
