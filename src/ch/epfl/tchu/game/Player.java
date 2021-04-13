package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * Modelizes a Tchu player
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

public interface Player {

    /**
     * Represents the three types of actions that a player can make in a Tchu play
     */
     public enum TurnKind{

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        public final static List<TurnKind> ALL = List.of(TurnKind.values());
     }

    /**
     * @param ownId (PlayerId) : The identity of the player
     * @param playerNames (Map<PlayerId, String>) : The names of all the players
     */
    abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * @param info (String) : The information that must be communicated to the player
     */
    abstract void receiveInfo(String info);

    /**
     * @param newState (PublicGameState) : The new state of the game
     * @param ownState (PlayerState) : The current state of this player
     */
    abstract void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * @param tickets (SortedBag<Ticket>) : The five tickets being distributed to the player
     */
    abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * @return (TurnKind) The type of action the player wishes to do
     */
    abstract TurnKind nextTurn();

    /**
     * @param options (SortedBag<Ticket>) : The tickets the player has to choose between
     * @return (SortedBag<Ticket>) The tickets the player will keep
     */
    abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * @return (SortedBag<Ticket>) The tickets the player will choose between to keep them
     */
    abstract SortedBag<Ticket> chooseInitialTickets();

    /**
     * @return (SortedBag<Card>) : The cards the player wishes to play to take over a route
     */
    abstract SortedBag<Card> initialClaimCards();

    /**
     * @param options (List<SortedBag<Card>>) : The necessary cards the player has to choose between to take over a tunnel route
     * @return (SortedBag<Card>) The cards the player chose
     */
    abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

    /**
     * @return (int) The emplacement where the player wishes to draw his cards
     */
    abstract int drawSlot();

    /**
     * @return (Route) : The route the player decided or tried to take over
     */
    abstract Route claimedRoute();
}
