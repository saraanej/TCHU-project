package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * Modelizes a Tchu player
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */

public interface Player {

    /**
     * represents the three types of actions that a player can make in a Tchu play
     */
     public enum TurnKind{

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        public final static List<TurnKind> ALL = List.of(TurnKind.values());
     }


    /**
     *
     * @param ownId (PlayerId) : the identity of the player
     * @param playerNames (Map<PlayerId, String>) : the names of all the players
     */
    abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     *
     * @param info (String) : The information that must be communicated to the player
     */
    abstract void receiveInfo(String info);

    /**
     *
     * @param newState (PublicGameState) : the new state of the game
     * @param ownState (PlayerState) : the current state of this player
     */
    abstract void updateState(PublicGameState newState, PlayerState ownState);

    /**
     *
     * @param tickets (SortedBag<Ticket>) : the five tickets being distributed to the player
     */
    abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);


    /**
     *
     * @return (SortedBag<Ticket>) the tickets the player will choose between to keep them
     */
    abstract SortedBag<Ticket> chooseInitialTickets();

    /**
     *
     * @return (TurnKind) the type of action the player wishes to do
     */
    abstract TurnKind nextTurn();

    /**
     *
     * @param options (SortedBag<Ticket>) : the tickets the player has to choose between
     * @return (SortedBag<Ticket>) the tickets the player will keep
     */
    abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     *
     * @return (int) the emplacement where the player wishes to draw his cards
     */
    abstract int drawSlot();

    /**
     *
     * @return (Route) : the route the player decided or tried to take over
     */
    abstract Route claimedRoute();

    /**
     *
     * @return (SortedBag<Card>) : the cards the player wishes to play to take over a route
     */
    abstract SortedBag<Card> initialClaimCards();

    /**
     *
     * @param options (List<SortedBag<Card>>) : the necessary cards the player has to choose between to take over a tunnel route
     * @return (SortedBag<Card>) the cards the player chose
     */
    abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
