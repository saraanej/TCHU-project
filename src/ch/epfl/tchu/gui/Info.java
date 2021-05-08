package ch.epfl.tchu.gui;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

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
	 * @param playerName (String) : The player's name.
	 */
	public Info(String playerName) {
		this.playerName = playerName;
	}


	/**
	 * @param card (Card) : The given card.
	 * @param count (int) : Value determining the plurality of the string.
	 * @return (String) The french name of the given card.
	 */
	public static String cardName(Card card, int count) {
		String plural = StringsFr.plural(count);
		switch(card) {
			case BLACK:
				return StringsFr.BLACK_CARD + plural;
			case BLUE:
				return StringsFr.BLUE_CARD + plural;
			case GREEN:
				return StringsFr.GREEN_CARD + plural;
			case ORANGE:
				return StringsFr.ORANGE_CARD + plural;
			case RED:
				return StringsFr.RED_CARD + plural;
			case VIOLET:
				return StringsFr.VIOLET_CARD + plural;
			case WHITE:
				return StringsFr.WHITE_CARD + plural;
			case YELLOW:
				return StringsFr.YELLOW_CARD + plural;
			default:
				return StringsFr.LOCOMOTIVE_CARD + plural;
		}
	}

	/**
	 * @param playerNames (String) : The players' names.
	 * @param points (int) : The points that each one of the players won.
	 * @return (String) The message declaring that the players have finished the game ex aequo
	 *         and that each one of them won the given points.
	 */
	public static String draw(List<String> playerNames, int points) {
		return String.format(StringsFr.DRAW,
				elementStringList(playerNames),
				points);
	}

	/**
	 * @return (String) The message declaring the first player to play.
	 */
	public String willPlayFirst() {
		return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
	}

	/**
	 * @param count (int) : The number of cards the player kept.
	 * @return (String) The message declaring that the player kept the number of cards given.
	 */
	public String keptTickets(int count) {
		return String.format(StringsFr.KEPT_N_TICKETS,
				playerName,
				count,
				StringsFr.plural(count));
	}

	/**
	 * @return (String) The message declaring that the player can play.
	 */
	public String canPlay() {
		return String.format(StringsFr.CAN_PLAY, playerName);
	}

	/**
	 * @param count (int) : Number of tickets the player took.
	 * @return (String) The message declaring that the player took the given number of tickets.
	 */
	public String drewTickets(int count) {
		return String.format(StringsFr.DREW_TICKETS,
				playerName,
				count,
				StringsFr.plural(count));
	}

	/**
	 * @return (String) The message declaring the player took a card from the deck.
	 */
	public String drewBlindCard() {
		return String.format(StringsFr.DREW_BLIND_CARD, playerName);
	}

	/**
	 * @return (String) the message declaring the player took the given visible card
	 */
	public String drewVisibleCard(Card card) {
		return String.format(StringsFr.DREW_VISIBLE_CARD,
				playerName,
				cardName(card, 1));
	}

	/**
	 * @param route (Route) : The route that the player took over.
	 * @param cards (SortedBag<Card>) : The cards the player played to take over the route.
	 * @return (String) The message declaring that the player took over the given route
	 *                  by playing the given cards.
	 */
	public String claimedRoute(Route route, SortedBag<Card> cards) {
		return String.format(StringsFr.CLAIMED_ROUTE,
				playerName,
				routeName(route),
				textCardList(cards));
	}

	/**
	 * @param route (Route) : The tunnel route the player wants to take over.
	 * @param initialCards (SortedBag<Card>) : The cards the player will play to get the tunnel route.
	 * @return (String) The message declaring that the player wants to take over the given tunnel route
	 *                  using the given cards.
	 */
	public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
		return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM,
				playerName,
				routeName(route),
				textCardList(initialCards));
	}

	/**
	 * @param drawnCards (SortedBag<Card>) : The additional cards that the player drew.
	 * @param additionalCost (int) : The additional cost for the given cards.
	 * @return (String) The message declaring that the player drew three additional cards
	 *                  and that they involve an additional cost of the given number of cards.
	 */
	public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
		String cost = String.format(StringsFr.ADDITIONAL_CARDS_ARE, textCardList(drawnCards));

		if(additionalCost == 0) {
			cost += StringsFr.NO_ADDITIONAL_COST;
		} else {
			cost += String.format(StringsFr.SOME_ADDITIONAL_COST,
					additionalCost,
					StringsFr.plural(additionalCost));
		}
		return cost;
	}

	/**
	 * @param route (Route) : The given tunnel route.
	 * @return (String) The message declaring that the player couldn't
	 *                  or didn't want to take over the given tunnel route.
	 */
	public String didNotClaimRoute(Route route) {
		return String.format(StringsFr.DID_NOT_CLAIM_ROUTE,
				playerName,
				routeName(route));
	}

	/**
	 * @param carCount (int) : The number of wagon cars the player has left.
	 * @return (String) The message declaring that the player has only the given number of wagon cars
	 *                  and that the last turn will begin.
	 */
	public String lastTurnBegins(int carCount) {
		return String.format(StringsFr.LAST_TURN_BEGINS,
				playerName,
				carCount,
				StringsFr.plural(carCount));
	}

	/**
	 * @param longestTrail (Trail) : The trail that made the player earn the final game bonus.
	 * @return (String) The message declaring that the player obtains the final game bonus
	 *                  thanks to the given trail which the longest or one of the longest.
	 */
	public String getsLongestTrailBonus(Trail longestTrail) {
		return String.format(StringsFr.GETS_BONUS,
				playerName,
				trailName(longestTrail));
	}

	/**
	 * @param points (int) : The points earned by the player in the game.
	 * @param loserPoints (int) : The points made by the player's opponent in the game.
	 * @return (String) The message declaring that the player won with the given points
	 *                  while his opponent lost with the given "loserPoints".
	 */
	public String won(int points, int loserPoints) {
		return String.format(StringsFr.WINS,
				playerName,
				points,
				StringsFr.plural(points),
				loserPoints,
				StringsFr.plural(loserPoints));
	}


	/**
	 * @param route (Route) : The given route.
	 * @return (String) The message declaring the details of the route.
	 */
	private static String routeName(Route route) {
		return String.format("%s%s%s" ,
				route.station1(),
				StringsFr.EN_DASH_SEPARATOR,
				route.station2());
	}

	/**
	 * @param trail (Trail) : The given trail.
	 * @return (String) The message declaring the details of the trail.
	 */
	private static String trailName(Trail trail){
		return String.format("%s%s%s" ,
				trail.station1(),
				StringsFr.EN_DASH_SEPARATOR,
				trail.station2());
	}

	/**
	 * @param cards (SortedBag<Card>) : The given list of cards.
	 * @return (String) The message containing all the specifics of the cards
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
			elementCardList = String.format("%s%s%s", joinedElements, StringsFr.AND_SEPARATOR, subElements.get(subElements.size() - 1));
			return elementCardList;
		}
	}

	/**
	 * @param playerNames (List<String>) : The given list of the players' names.
	 * @return (String) The message containing all the players' names
	 *                  contained in the given list.
	 */
	private static String elementStringList(List<String> playerNames) {
		return String.format("%s%s%s",
				playerNames.get(0),
				StringsFr.AND_SEPARATOR,
				playerNames.get(1));
	}}