package ch.epfl.tchu.gui;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;
import static ch.epfl.tchu.gui.StringsFr.*;


/**
 * The Info class, public, final and immutable, allows you to generate the texts describing the progress of the game.
 * Most of these messages describe the actions of a given player,
 * whose name is passed as an argument to the constructor.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public final class Info {

	private final String playerName;


	/**
	 * Default constructor of the messages linked to the player's name.
	 * @param playerName The player's name.
	 */
	public Info(String playerName) {
		this.playerName = playerName;
	}


	/**
	 * @param card The given card.
	 * @param count Value determining the plurality of the string.
	 * @return The french name of the given card.
	 */
	public static String cardName(Card card, int count) {
		String plural = plural(count);
		switch(card) {
			case BLACK:
				return BLACK_CARD + plural;
			case BLUE:
				return BLUE_CARD + plural;
			case GREEN:
				return GREEN_CARD + plural;
			case ORANGE:
				return ORANGE_CARD + plural;
			case RED:
				return RED_CARD + plural;
			case VIOLET:
				return VIOLET_CARD + plural;
			case WHITE:
				return WHITE_CARD + plural;
			case YELLOW:
				return YELLOW_CARD + plural;
			case LOCOMOTIVE:
				return LOCOMOTIVE_CARD + plural;
			default:
				throw new Error();
		}
	}

	/**
	 * @param playerNames The players' names.
	 * @param points The points that each one of the players won.
	 * @return The message declaring that the players have finished the game ex aequo
	 *         and that each one of them won the given points.
	 */
	public static String draw(List<String> playerNames, int points) {
		return String.format(DRAW,
				String.join("", playerNames.get(0),
						AND_SEPARATOR, playerNames.get(1)),
				points);
	}


	/**
	 * @return The message declaring the first player to play.
	 */
	public String willPlayFirst() {
		return String.format(WILL_PLAY_FIRST, playerName);
	}

	/**
	 * @param count The number of cards the player kept.
	 * @return The message declaring that the player kept the number of cards given.
	 */
	public String keptTickets(int count) {
		return String.format(KEPT_N_TICKETS,
				playerName,
				count,
				plural(count));
	}

	/**
	 * @return The message declaring that the player can play.
	 */
	public String canPlay() {
		return String.format(CAN_PLAY, playerName);
	}

	/**
	 * @param count Number of tickets the player took.
	 * @return The message declaring that the player took the given number of tickets.
	 */
	public String drewTickets(int count) {
		return String.format(DREW_TICKETS,
				playerName,
				count,
				plural(count));
	}

	/**
	 * @return The message declaring the player took a card from the deck.
	 */
	public String drewBlindCard() {
		return String.format(DREW_BLIND_CARD, playerName);
	}

	/**
	 * @return the message declaring the player took the given visible card
	 */
	public String drewVisibleCard(Card card) {
		return String.format(DREW_VISIBLE_CARD,
				playerName,
				cardName(card, 1));
	}

	/**
	 * @param route The route that the player took over.
	 * @param cards The cards the player played to take over the route.
	 * @return The message declaring that the player took over the given route
	 *                  by playing the given cards.
	 */
	public String claimedRoute(Route route, SortedBag<Card> cards) {
		return String.format(CLAIMED_ROUTE,
				playerName,
				routeName(route),
				textCardList(cards));
	}

	/**
	 * @param route The tunnel route the player wants to take over.
	 * @param initialCards The cards the player will play to get the tunnel route.
	 * @return The message declaring that the player wants to take over the given tunnel route
	 *                  using the given cards.
	 */
	public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
		return String.format(ATTEMPTS_TUNNEL_CLAIM,
				playerName,
				routeName(route),
				textCardList(initialCards));
	}

	/**
	 * @param drawnCards The additional cards that the player drew.
	 * @param additionalCost The additional cost for the given cards.
	 * @return The message declaring that the player drew three additional cards
	 *                  and that they involve an additional cost of the given number of cards.
	 */
	public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
		String cost = String.format(ADDITIONAL_CARDS_ARE, textCardList(drawnCards));

		if(additionalCost == 0) {
			cost += NO_ADDITIONAL_COST;
		} else {
			cost += String.format(SOME_ADDITIONAL_COST,
					additionalCost,
					plural(additionalCost));
		}
		return cost;
	}

	/**
	 * @param route The given tunnel route.
	 * @return The message declaring that the player couldn't
	 *                  or didn't want to take over the given tunnel route.
	 */
	public String didNotClaimRoute(Route route) {
		return String.format(DID_NOT_CLAIM_ROUTE,
				playerName,
				routeName(route));
	}

	/**
	 * @param carCount The number of wagon cars the player has left.
	 * @return The message declaring that the player has only the given number of wagon cars
	 *                  and that the last turn will begin.
	 */
	public String lastTurnBegins(int carCount) {
		return String.format(LAST_TURN_BEGINS,
				playerName,
				carCount,
				plural(carCount));
	}

	/**
	 * @param longestTrail The trail that made the player earn the final game bonus.
	 * @return The message declaring that the player obtains the final game bonus
	 *                  thanks to the given trail which the longest or one of the longest.
	 */
	public String getsLongestTrailBonus(Trail longestTrail) {
		return String.format(GETS_BONUS,
				playerName,
				String.join("" ,
						longestTrail.station1().name(),
						EN_DASH_SEPARATOR,
						longestTrail.station2().name()));
	}

	/**
	 * @param longestTrail The trail that made the player earn the final game bonus.
	 * @return The message declaring the player who got the longest or one of the longest trails.
	 */
	public String winsLongestTrail(Trail longestTrail) {
		return String.format(LONGEST_TRAIL,
				playerName,
				String.join("" ,
						longestTrail.station1().name(),
						EN_DASH_SEPARATOR,
						longestTrail.station2().name()));
	}

	/**
	 * @param points The points earned by the player in the game.
	 * @param loserPoints The points made by the player's opponent in the game.
	 * @return The message declaring that the player won with the given points
	 *                  while his opponent lost with the given "loserPoints".
	 */
	public String won(int points, int loserPoints) {
		return String.format(WINS,
				playerName,
				points,
				plural(points),
				loserPoints,
				plural(loserPoints));
	}

	/**
	 * @param points The points earned by the player in the game.
	 * @return The message declaring that the player won with the given points.
	 */
	public String winsMenu(int points){
		return String.format(WINS_MENU,
				playerName,
				points,
				plural(points));
	}

	/**
	 * @param points The points earned by the player in the game.
	 * @return The message declaring that the player won the given points at the end of the game.
	 */
	public String endGamePlayerStats(int points){
		return String.format(CURRENT_PLAYER_POINTS,
				points,
				plural(points));
	}

	/**
	 *
	 * @param names Names of all the players of the game.
	 * @param points The maximal points earned.
	 * @return The message declaring that all the players won with the same points.
	 */
	public String allPlayersWinPoints(String names, int points){
		return String.format(ALL_WINNERS_POINTS,
				names,
				points,
				plural(points));
	}

	/**
	 *
	 * @param names The name of the players who got the longest trail.
	 * @return The message declaring all the players who got the longest trail.
	 */
	public String allPlayersWinTrail(String names){
		return String.format(ALL_WINNERS_LONGEST_TRAIL, // est appelée si size >= 2
				names);
	}

	/**
	 *
	 * @param names The name of the player who got the longest trail.
	 * @return The message declaring the player who got the longest trail.
	 */
	public String oneWinnerTrail(String names){
		return String.format(ONE_WINNER_TRAIL_END, // est appelée si size == 1
				names);
	}

	/**
	 * @param cards The given list of cards.
	 * @return The message containing all the specifics of the cards
	 *                  contained in the given list.
	 */
	public static String textCardList(SortedBag<Card> cards) {
		List<String> elements = new ArrayList<>();
		List<String> subElements = new ArrayList<>();
		String joinedElements;
		String elementCardList;

		for (Card card: cards.toSet()) {
			int n = cards.countOf(card);
			subElements.add(String.format("%s %s",
					n, cardName(card, n)));
		}
		if(subElements.size() == 1){
			return subElements.get(0);
		} else {
			for (int j = 0; j < subElements.size() - 1; ++j) {
				elements.add(subElements.get(j));
			}
			joinedElements = String.join(", ", elements);
			elementCardList = String.join("", joinedElements, AND_SEPARATOR, subElements.get(subElements.size() - 1));
			return elementCardList;
		}
	}


	/**
	 * @param route The given route.
	 * @return The message declaring the details of the route.
	 */
	private static String routeName(Route route) {
		return String.join("" ,
				route.station1().name(),
				EN_DASH_SEPARATOR,
				route.station2().name());
	}
}