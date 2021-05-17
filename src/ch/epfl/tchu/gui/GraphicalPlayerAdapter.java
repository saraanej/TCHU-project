package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer player; //final ou pas?
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
        runLater(() ->  queuePlayer.add(new GraphicalPlayer(ownId, playerNames)));

        try {
           player = queuePlayer.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
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
        if(!queueSlots.isEmpty()) return queueSlots.remove(); //TODO: USE .TAKE(): je dois bloquer ici?
        else {
            player.drawCard( i -> queueSlots.add(i));

            try {
                return queueSlots.take();
            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }
    }

    @Override
    public TurnKind nextTurn() {
        runLater(() ->{ player.startTurn(() -> queueTurn.add(TurnKind.DRAW_TICKETS),

                i -> { queueTurn.add(TurnKind.DRAW_CARDS);
                queueSlots.add(i); },

                (r,c) -> { queueTurn.add(TurnKind.CLAIM_ROUTE);
                queueCards.add(c);
                queueRoute.add(r);
                });
        });
        try {
            return queueTurn.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        runLater(() -> player.chooseTickets(options,t -> queueTickets.add(t)));
        try {
            return queueTickets.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return queueTickets.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //todo: lappel de retour doit bloquer ici ou pas?
    @Override
    public SortedBag<Card> initialClaimCards() {
        try {
            return queueCards.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> player.chooseAdditionalCards(options,s -> queueCards.add(s)));
        try {
            return queueCards.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //todo: lappel de retour doit bloquer ici ou pas?
    @Override
    public Route claimedRoute() {
        try {
            return queueRoute.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }
}
