package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;


//TODO est ce que elle extends eVENT HANDLER?
public interface ActionHandlers {

    interface ClaimRouteHandler {
        void onClaimRoute (Route route, SortedBag<Card> initialCards);
    }

    interface ChooseCardsHandler {
        void onChooseCards (SortedBag<Card> options);
    }
}
