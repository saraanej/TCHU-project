package ch.epfl.tchu.net;

import ch.epfl.tchu.game.PlayerId;

import java.util.List;

public enum MessageId {

    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS;


    //NOT SPECIFIED IN THE SUBJET
    public final static List<MessageId> ALL = List.of(MessageId.values());

}
