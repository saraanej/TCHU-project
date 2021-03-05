package ch.epfl.tchu.game.src.ch.epfl.tchu.gui;

import java.util.List;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

/**
 * Generates the texts describing the progress of the game
 * 
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public final class Info {
	
	private final String playerName;

	/**
	 * Default constructor of the messages linked to the player's name
	 * 
	 * @param (String) playerName : the player's name
	 */
	
	public Info(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * 
	 * @param (Card) card : the given card
	 * @param (int) count : value determining the plurality of the string
	 
	 * @return (String) the French name of the given card
	 */
	public static String cardName(Card card, int count) {
		
		String cardName;
		
		switch(card.color()) {
		case BLACK: cardName = StringsFr.BLACK_CARD;
		case BLUE: cardName =  StringsFr.BLUE_CARD;
		case GREEN: cardName = StringsFr.GREEN_CARD;
		case ORANGE: cardName = StringsFr.ORANGE_CARD;
		case RED: cardName = StringsFr.RED_CARD;
		case VIOLET: cardName = StringsFr.VIOLET_CARD;
		case WHITE: cardName = StringsFr.WHITE_CARD;
		case YELLOW: cardName = StringsFr.YELLOW_CARD;
		default : cardName = null;
		}
		
		cardName += StringsFr.plural(count);
	    return cardName;
	}
	
	/**
	 *  
	 * @param (String) playerNames : the players' names
	 * @param (int) points : the points that each one of the players won
	 
	 * @return (String) the message declaring that the players have finished the game ex aequo 
	 *         and that each one of them won the given points
	 */
	public static String draw(List<String> playerNames, int points) {
		//AFFICHER ELEMENTS LIST
		return String.format(StringsFr.DRAW, 
				             playerNames, 
				             points);
	}
	
	/**
	 * 
	 * @return (String) the message declaring the first player to play
	 */
	public String willPlayFirst() {
		return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
	}
	
	/**
	 * 
	 * @param (int) count : the number of cards the player kept
	 * 
	 * @return (String) the message declaring that the player kept the number of cards given
	 */
	public String keptTickets(int count) {
		return String.format(StringsFr.KEPT_N_TICKETS, 
				             playerName, 
				             count,
				             StringsFr.plural(count));
	}
	
	/**
	 * 
	 * @return (String) the message declaring that the player can play
	 */
	
	public String canPlay() {
		return String.format(StringsFr.CAN_PLAY, playerName);
	}
	
	/**
	 * 
	 * @param (int) count : number of tickets the player took
	 * @return (String) the message declaring that the player took the given number of tickets
	 */
	public String drewTickets(int count) {
		return String.format(StringsFr.DREW_TICKETS, 
				             playerName, 
				             count,
				             StringsFr.plural(count));
	}
	
	/**
	 * 
	 * @return (String) the message declaring the player took a card from the deck
	 */
	public String drewBlindCard() {
		return String.format(StringsFr.DREW_BLIND_CARD, playerName);
	}
	
	/**
	 * 
	 * @return (String) the message declaring the player took the given visible card
	 */
	public String drewVisibleCard(Card card) {
		//COMMENT AFFICHER CARD
		return String.format(StringsFr.DREW_VISIBLE_CARD, 
				             playerName, 
				             card);
	}
	
	/**
	 * 
	 * @param (Route) route : the route that the player took over 
	 * @param (SortedBag<Card>) cards : the cards the player played to take over the route
	 * 
	 * @return (String) the message declaring that the player took over the given route
	 *                  by playing the given cards
	 */
	public String claimedRoute(Route route, SortedBag<Card> cards) {
		return String.format(StringsFr.CLAIMED_ROUTE, playerName, 
				             routeName(route), 
				             elementList(cards));
	}
	
	/**
	 * 
	 * @param (Route) route : the tunnel route the player wants to take over
	 * @param (SortedBag<Card>) initialCards : the cards the player will play to get the tunnel route
	 * 
	 * @return (String) the message declaring that the player wants to take over the given tunnel route
	 *                  using the given cards
	 */
	public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
		return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM,
				             playerName, 
				             routeName(route), 
				             elementList(initialCards));
	}
	
	/**
	 * 
	 * @param (SortedBag<Card>) drawnCards : the additional cards that the player drew
	 * @param (int) additionalCost : the additional cost for the given cards
	 * 
	 * @return (String) the message declaring that the player drew three additional cards
	 *                  and that they involve an additional cost of the given number of cards 
	 */
	public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
	
		String cost = String.format(StringsFr.ADDITIONAL_CARDS_ARE, elementList(drawnCards));
		
		if(additionalCost==0) {
			cost += StringsFr.NO_ADDITIONAL_COST;

		} else {
			cost += String.format(StringsFr.SOME_ADDITIONAL_COST, 
					              additionalCost, 
					              StringsFr.plural(additionalCost));
		}
		
        return cost;	
     }
	
	
	/**
	 * 
	 * @param (Route) route : the given tunnel route
	 * 
	 * @return (String) the message declaring that the player couldn't 
	 *                  or didn't want to take over the given tunnel route
	 */
	public String didNotClaimRoute(Route route) {
		return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, 
				             playerName, 
				             routeName(route));
	}
	
	/**
	 * 
	 * @param (int) carCount : the number of wagon cards the player has left
	 * 
	 * @return (String) the message declaring that the player has only the given number of wagon cards
	 *                  and that the last tour will begin
	 */
	public String lastTurnBegins(int carCount) {
		//Faut il verifier que carCount<=2 ?
		return String.format(StringsFr.LAST_TURN_BEGINS,
				             playerName, 
				             carCount, 
				             StringsFr.plural(carCount));
	}
	
	/**
	 * 
	 * @param (Trail) longestTrail : the trail that made the player earn the final game bonus
	 * 
	 * @return (String) the message declaring that the player obtains the final game bonus
	 *                  thanks to the given trail which the longest or one of the longest
	 */
	public String getsLongestTrailBonus(Trail longestTrail) {
		String trail = String.format("%s%s%s",
				                     longestTrail.station1(), 
				                     StringsFr.EN_DASH_SEPARATOR, 
				                     longestTrail.station2());
		
		return String.format(StringsFr.GETS_BONUS, playerName, trail);
	}
	
	/**
	 * 
	 * @param (int) points : the points earned by the player in the game
	 * @param (int) loserPoints : the points made by the player's opponent in the game
	 * 
	 * @return (String) the message declaring that the player won with the given points
	 *                  while his opponent lost with the given "loserPoints"
	 */
	public String won(int points, int loserPoints) {
		return String.format(StringsFr.WINS,
				             playerName,
				             StringsFr.plural(points),
				             points, 
				             loserPoints,
				             StringsFr.plural(loserPoints));
	}
	
	/**
	 * 
	 * @param (Route) route : the given route
	 * 
	 * @return (String) the message declaring the details of the route
	 */
	private static String routeName(Route route) {

		String routeName = String.format("%s%s%s" ,
				                  route.station1(),
				                  StringsFr.EN_DASH_SEPARATOR,
				                  route.station2());
		return routeName;
	}
	
	/**
	 * 
	 * @param (SortedBag<Card>) cards : the given list of cards 
	 * 
	 * @return (String) the message containing all the specifics of the cards
	 *                  contained in the given list
	 */
	private static String elementList(SortedBag<Card> cards) {
		String elements = "";
		for(int i = 0; i < cards.size(); ++i) {
			
			Card card = cards.get(i);
			int number = cards.countOf(card);
			
			elements += String.format("%s %s", 
					                  number, 
					                  cardName(card, number));
			
			if(i == cards.size() - 2) {
				elements += StringsFr.AND_SEPARATOR;
			} 
			if(i != (cards.size() -2 ) && i != (cards.size() -1 )) {
				elements += ", ";
			}
		}
		return elements;
	}

}