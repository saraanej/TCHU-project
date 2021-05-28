package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * The ActionHandlers interface of the ch.epfl.tchu.gui package
 * is intended solely to contain five nested functional interfaces
 * representing different "action handlers"
 * (a piece of code to be executed when the player performs an action).
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public interface ActionHandlers {

    /**
     * The action handler for the action : draw tickets from the ticket's deck.
     */
    @FunctionalInterface
    interface DrawTicketsHandler {
        /**
         * Called when the player wishes to draw tickets.
         */
        void onDrawTickets();
    }

    /**
     * The action handler for the action : draw cards from the cards' deck.
     */
    @FunctionalInterface
    interface DrawCardHandler {
        /**
         * Called when the player wishes to draw a card from the given slot.
         *
         * @param slot the slot of the desired card ( from -1 to 4).
         */
        void onDrawCard(int slot);
    }

    /**
     * The action handler for the action : choose tickets.
     */
    @FunctionalInterface
    interface ChooseTicketsHandler {
        /**
         * Called when the player has chosen to keep the tickets given
         * following a ticket draw.
         *
         * @param tickets the chosen tickets.
         */
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    /**
     * The action handler for the action : claim a route from the ChMap.
     */
    @FunctionalInterface
    interface ClaimRouteHandler {
        /**
         * Called when the player wishes to seize the given route
         * by means of the given (initial) cards.
         *
         * @param route        the Route to claim.
         * @param initialCards the cards used to claim the route.
         */
        void onClaimRoute(Route route, SortedBag<Card> initialCards);
    }

    /**
     * The action handler for the action : choose cards.
     */
    @FunctionalInterface
    interface ChooseCardsHandler {
        /**
         * Called when the player has chosen to use the given cards as initial or additional cards
         * when taking possession of a route;
         * if they are additional cards, then the multiset may be empty,
         * which means that the player gives up taking the tunnel.
         *
         * @param options the possible initial or additional cards to choose from.
         */
        void onChooseCards(SortedBag<Card> options);
    }
}
