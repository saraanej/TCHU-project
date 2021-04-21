package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * The RemotePlayerProxy instantiable class in the ch.epfl.tchu.net package
 * represents a remote player proxy. It implements the Player interface and can thus play the role of a player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class RemotePlayerProxy implements Player {

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

    }

    @Override
    public void receiveInfo(String info) {

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }
}
