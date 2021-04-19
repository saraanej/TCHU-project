package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

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
     * @param ownId (PlayerId) : The identity of the player.
     * @param playerNames (Map<PlayerId, String>) : The names of all the players.
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * @param info (String) : The information that must be communicated to the player.
     */
    void receiveInfo(String info);

    /**
     * @param newState (PublicGameState) : The new state of the game.
     * @param ownState (PlayerState) : The current state of this player.
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * @param tickets (SortedBag<Ticket>) : The five tickets being distributed to the player.
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * @return (int) The emplacement where the player wishes to draw his cards.
     */
    int drawSlot();

    /**
     * @return (TurnKind) The type of action the player wishes to do.
     */
    TurnKind nextTurn();

    /**
     * @param options (SortedBag<Ticket>) : The tickets the player has to choose between.
     * @return (SortedBag<Ticket>) The tickets the player will keep.
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * @return (SortedBag<Ticket>) The tickets the player will choose between to keep them.
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * @return (SortedBag<Card>) : The cards the player wishes to play to take over a route.
     */
    SortedBag<Card> initialClaimCards();

    /**
     * @param options (List<SortedBag<Card>>) : The necessary cards the player has to choose between to take over a tunnel route.
     * @return (SortedBag<Card>) The cards the player chose.
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

    /**
     * @return (Route) : The route the player decided or tried to take over.
     */
    Route claimedRoute();
}