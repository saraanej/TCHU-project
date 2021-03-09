package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Route.Level;
import ch.epfl.tchu.game.src.ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.game.src.ch.epfl.tchu.gui.StringsFr;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class InfoTest {
	
	 private static final Station NEU = new Station(19, "Neuchâtel");
	 private static final Station YVE = new Station(31, "Yverdon");
	 private static final Station BER = new Station(3, "Berne");
	 private static final Station LUC = new Station(16, "Lucerne");


	 private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK);
	 private static final Route E = new Route("BER_LUC_1", BER, LUC, 6, Level.UNDERGROUND, null);
	 private static final List<Route> routes = new ArrayList<>();

	    
	 SortedBag<Card> claimcards = SortedBag.of(2, Card.BLACK);

	Info chris = new Info("Chris");
	
	@Test
	public void testCardNameSingular() {
		assertEquals("blanche", Info.cardName(Card.WHITE, 1));
	}

	@Test
	public void testCardNamePlural() {
		assertEquals("locomotives", Info.cardName(Card.LOCOMOTIVE, 4));
	}
	
	@Test
	public void TestWillPlayFirst() {
		assertEquals("Chris jouera en premier.\n\n",
				     chris.willPlayFirst());
	}
	
	@Test 
	public void TestKeptTicketsPlural() {
		assertEquals("Chris a gardé 3 billets.\n",
				     chris.keptTickets(3));
	}

	@Test
	public void TestKeptTicketsSingular() {
		assertEquals("Chris a gardé 1 billet.\n",
				chris.keptTickets(1));
	}

	@Test
	public void Testdraw() {
		List<String> names = new ArrayList<>();
		names.add("James");
		names.add("Chris");
		assertEquals("\nJames et Chris sont ex æqo avec 46 points !\n",
				Info.draw(names, 46));
	}
	
	@Test
	public void TestCanPlay() {
		assertEquals("\nC'est à Chris de jouer.\n",
				     chris.canPlay());
	}
	
	@Test
	public void TestDrewTicketsPlural() {
		assertEquals("Chris a tiré 4 billets...\n",
				      chris.drewTickets(4));
	}

	@Test
	public void TestDrewTicketsSingular() {
		assertEquals("Chris a tiré 1 billet...\n",
				chris.drewTickets(1));
	}
	
	@Test
	public void TestDrewBlindCard() {
		assertEquals("Chris a tiré une carte de la pioche.\n",
				     chris.drewBlindCard());
	}
	
	@Test
	public void TestDrewVisibleCard() {
		assertEquals("Chris a tiré une carte locomotive visible.\n",
				    chris.drewVisibleCard(Card.LOCOMOTIVE));
	}
	
	@Test
	public void TestClaimedRouteWithSingleCard() {
		SortedBag<Card> claimCards = SortedBag.of(1, Card.WHITE);

		String equals = String.format("%s%s%s",
				"Chris a pris possession de la route Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 1 blanche.\n");


		assertEquals("Chris a pris possession de la route Neuchâtel – Yverdon au moyen de 1 blanche.\n",
				     chris.claimedRoute(A, claimCards));
	}

	@Test
	public void TestClaimedRouteWithTwoCards() {
		SortedBag<Card> claimCards = SortedBag.of(1, Card.WHITE, 4, Card.LOCOMOTIVE);

		String equals = String.format("%s%s%s",
				"Chris a pris possession de la route Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 1 blanche et 4 locomotives.\n");

		assertEquals(equals,
				chris.claimedRoute(A, claimCards));
	}

	@Test
	public void TestClaimedRouteWithMultipleCards() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(Card.BLUE);
		cards.add(Card.WHITE);
		cards.add(Card.LOCOMOTIVE);
		cards.add(Card.WHITE);

		SortedBag<Card> claimCards = SortedBag.of(cards);

		String equals = String.format("%s%s%s",
				"Chris a pris possession de la route Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 1 bleue, 2 blanches et 1 locomotive.\n");

		assertEquals(equals,
				chris.claimedRoute(A, claimCards));
	}

	@Test
	public void TestAttemptsTunnelClaimWitHOneCard(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.LOCOMOTIVE);

		String equals = String.format("%s%s%s",
				"Chris tente de s'emparer du tunnel Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 1 locomotive !\n");

		assertEquals(equals,
				    chris.attemptsTunnelClaim(A, claimcards));
	}

	@Test
	public void TestAttemptsTunnelClaimWitHTwoCards(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.LOCOMOTIVE, 3, Card.ORANGE);

		String equals = String.format("%s%s%s",
				"Chris tente de s'emparer du tunnel Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 3 oranges et 1 locomotive !\n");

		assertEquals(equals,
				chris.attemptsTunnelClaim(A, claimcards));
	}

	@Test
	public void TestAttemptsTunnelClaimWitHMultipleCards(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(Card.BLUE);
		cards.add(Card.YELLOW);
		cards.add(Card.LOCOMOTIVE);
		cards.add(Card.WHITE);
		cards.add(Card.YELLOW);

		SortedBag<Card> claimCards = SortedBag.of(cards);

		String equals = String.format("%s%s%s",
				"Chris tente de s'emparer du tunnel Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon au moyen de 1 bleue, 2 jaunes, 1 blanche et 1 locomotive !\n");


				assertEquals(equals,
				chris.attemptsTunnelClaim(A, claimCards));
	}

	@Test
	public void TestDrewAdditionalCardsWithOneCardNoAdditionalCost(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.RED);

		assertEquals("Les cartes supplémentaires sont 1 rouge. Elles n'impliquent aucun coût additionnel.\n",
				     chris.drewAdditionalCards(claimcards, 0));
	}

	@Test
	public void TestDrewAdditionalCardsWithOneCardWithAdditionalCost(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.GREEN);

		assertEquals("Les cartes supplémentaires sont 1 verte. Elles impliquent un coût additionnel de 1 carte.\n",
				chris.drewAdditionalCards(claimcards, 1));
	}

	@Test
	public void TestDrewAdditionalCardsWithTwoCardsNoAdditionalCost(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.RED, 3, Card.VIOLET);

		assertEquals("Les cartes supplémentaires sont 3 violettes et 1 rouge. Elles n'impliquent aucun coût additionnel.\n",
				chris.drewAdditionalCards(claimcards, 0));
	}

	@Test
	public void TestDrewAdditionalCardsWithTwoCardsWithAdditionalCost(){
		SortedBag<Card> claimcards = SortedBag.of(1, Card.LOCOMOTIVE, 3, Card.VIOLET);

		assertEquals("Les cartes supplémentaires sont 3 violettes et 1 locomotive. Elles impliquent un coût additionnel de 10 cartes.\n",
				chris.drewAdditionalCards(claimcards, 10));
	}

	@Test
	public void TestDrewAdditionalCardsWithMultipleCardsWithAdditionalCost(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(Card.RED);
		cards.add(Card.YELLOW);
		cards.add(Card.LOCOMOTIVE);
		cards.add(Card.WHITE);
		cards.add(Card.YELLOW);

		SortedBag<Card> claimCards = SortedBag.of(cards);

		assertEquals("Les cartes supplémentaires sont 2 jaunes, 1 rouge, 1 blanche et 1 locomotive. Elles impliquent un coût additionnel de 45 cartes.\n",
				chris.drewAdditionalCards(claimCards, 45));
	}

	@Test
	public void TestDrewAdditionalCardsWithMultipleCardsWithNoAdditionalCost(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(Card.RED);
		cards.add(Card.YELLOW);
		cards.add(Card.LOCOMOTIVE);
		cards.add(Card.WHITE);
		cards.add(Card.YELLOW);

		SortedBag<Card> claimCards = SortedBag.of(cards);

		assertEquals("Les cartes supplémentaires sont 2 jaunes, 1 rouge, 1 blanche et 1 locomotive. Elles n'impliquent aucun coût additionnel.\n",
				chris.drewAdditionalCards(claimCards, 0));
	}


	@Test
	public void TestDitNotClaimRoute(){
		String equals = String.format("%s%s%s",
				"Chris n'a pas pu (ou voulu) s'emparer de la route Neuchâtel",
				StringsFr.EN_DASH_SEPARATOR,
				"Yverdon.\n");

		assertEquals(equals,
				    chris.didNotClaimRoute(A));
	}

	@Test
	public void TestLastTurnBeginsWithPlural(){
		assertEquals("\nChris n'a plus que 3 wagons, le dernier tour commence !\n",
				         chris.lastTurnBegins(3));
	}

	@Test
	public void TestLastTurnBeginsSingular(){
		assertEquals("\nChris n'a plus que 1 wagon, le dernier tour commence !\n",
				chris.lastTurnBegins(1));
	}

	@Test
	public void TestGetsLongestTrailBonus(){

		routes.add(A);
		routes.add(E);

		String equals = String.format("%s%s%s",
				"\nChris reçoit un bonus de 10 points pour le plus long trajet (Berne",
				StringsFr.EN_DASH_SEPARATOR,
				"Lucerne).\n");
		assertEquals(equals,
		             chris.getsLongestTrailBonus(Trail.longest(routes)));
	}

	@Test
	public void TestWonPlural(){
		assertEquals("\nChris remporte la victoire avec 78 points, contre 54 points !\n",
				     chris.won(78, 54));
	}

	@Test
	public void TestWonSingular(){
		assertEquals("\nChris remporte la victoire avec 2 points, contre 1 point !\n",
				chris.won(2, 1));
	}

}