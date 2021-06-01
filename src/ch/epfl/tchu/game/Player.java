package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Player interface, public, represents a tCHu player.
 * The methods of this interface are intended to be called at different times during the game,
 * either to communicate certain information concerning its progress to the player,
 * or to obtain certain information from the latter.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public interface Player {

    /**
     * Represents the three types of actions that a player can make in a Tchu play.
     */
     enum TurnKind{

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        public final static List<TurnKind> ALL = List.of(TurnKind.values());
     }

    /**
     * @param ownId The identity of the player.
     * @param playerNames The names of all the players.
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * @param info The information that must be communicated to the player.
     */
    void receiveInfo(String info);

    /**
     * @param newState The new state of the game.
     * @param ownState The current state of this player.
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * @param tickets The five tickets being distributed to the player.
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * @param winner the winner of the Tchu's game with the maximal points.
     * @param longestTrail the player who got the longest trail.
     */
    void endGame(PlayerId winner, Map<PlayerId, Integer> points, PlayerId longestTrailWinner, Map<PlayerId, Trail> longestTrail);

    /**
     * @return The emplacement where the player wishes to draw his cards.
     */
    int drawSlot();

    /**
     * shows the drawn card to the player.
     */
    void showCard(Card c);

    /**
     * @return The type of action the player wishes to do.
     */
    TurnKind nextTurn();

    /**
     * @param options The tickets the player has to choose between.
     * @return The tickets the player will keep.
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * @return The tickets the player will choose between to keep them.
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * @return The cards the player wishes to play to take over a route.
     */
    SortedBag<Card> initialClaimCards();

    /**
     * @param options The necessary cards the player has to choose between to take over a tunnel route.
     * @return The cards the player chose.
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

    /**
     * @return The route the player decided or tried to take over.
     */
    Route claimedRoute();
}