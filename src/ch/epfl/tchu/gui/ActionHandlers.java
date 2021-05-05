package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;

public interface ActionHandlers {

    interface ClaimRouteHandler {
        void onClaimRoute (Route route, SortedBag<Card> initialCards);
    }
}
