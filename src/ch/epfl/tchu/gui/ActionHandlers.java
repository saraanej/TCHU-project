package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;


//TODO est ce que elle extends eVENT HANDLER?
public interface ActionHandlers {

    @FunctionalInterface
    interface DrawTicketsHandler {
        void onDrawTickets();
    }

    @FunctionalInterface
    interface DrawCardHandler {
        void onDrawCard(int slot);
    }

    @FunctionalInterface
    interface ChooseTicketsHandler {
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    @FunctionalInterface
    interface ClaimRouteHandler {
        void onClaimRoute(Route route, SortedBag<Card> initialCards);
    }

    @FunctionalInterface
    interface ChooseCardsHandler {
        void onChooseCards(SortedBag<Card> options);
    }


}
