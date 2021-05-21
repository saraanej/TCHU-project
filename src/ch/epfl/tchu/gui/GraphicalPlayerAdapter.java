package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer player;
    private final BlockingQueue<SortedBag<Ticket>> queueTickets;
    private final BlockingQueue<SortedBag<Card>> queueCards;
    private final BlockingQueue<TurnKind> queueTurn;
    private final BlockingQueue<Integer> queueSlots;
    private final BlockingQueue<Route> queueRoute;


    public GraphicalPlayerAdapter(){
        queueTickets = new ArrayBlockingQueue<>(1);
        queueCards = new ArrayBlockingQueue<>(1);
        queueTurn = new ArrayBlockingQueue<>(1);
        queueSlots = new ArrayBlockingQueue<>(1);
        queueRoute = new ArrayBlockingQueue<>(1);
    }


    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        BlockingQueue<GraphicalPlayer> queuePlayer = new ArrayBlockingQueue<>(1);
        runLater(() -> queuePlayer.add(new GraphicalPlayer(ownId, playerNames)));
        player = take(queuePlayer);
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> player.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> player.setState(newState,ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> player.chooseTickets(tickets,t -> queueTickets.add(t)));
    }

    @Override
    public int drawSlot() {
        if(!queueSlots.isEmpty()) return queueSlots.remove();
        else {
            player.drawCard(i -> queueSlots.add(i));
            return take(queueSlots);
        }
    }

    @Override
    public TurnKind nextTurn() {
        runLater(() -> { player.startTurn(() -> queueTurn.add(TurnKind.DRAW_TICKETS),

                i -> { queueTurn.add(TurnKind.DRAW_CARDS);
                queueSlots.add(i); },

                (r,c) -> { queueTurn.add(TurnKind.CLAIM_ROUTE);
                queueCards.add(c);
                queueRoute.add(r);
                });
        });
        return take(queueTurn);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        runLater(() -> player.chooseTickets(options,t -> queueTickets.add(t)));
        return take(queueTickets);
    }

    //todo mettre try catch en methode private
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return take(queueTickets);
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return take(queueCards);
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> player.chooseAdditionalCards(options,s -> queueCards.add(s)));
        return take(queueCards);
    }

    @Override
    public Route claimedRoute() {
        return take(queueRoute);
    }


    private <E> E take(BlockingQueue<E> queue){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
}
