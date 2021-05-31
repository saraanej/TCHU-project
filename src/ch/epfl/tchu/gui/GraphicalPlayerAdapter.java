package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 * The instantiable class GraphicalPlayerAdapter, from the ch.epfl.tchu.gui package,
 * aims to adapt (in the sense of the Adapter pattern) an instance of GraphicalPlayer into a value of type Player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer player;
    private final BlockingQueue<SortedBag<Ticket>> queueTickets;
    private final BlockingQueue<SortedBag<Card>> queueCards;
    private final BlockingQueue<TurnKind> queueTurn;
    private final BlockingQueue<Integer> queueSlots;
    private final BlockingQueue<Route> queueRoute;


    /**
     * Public default constructor initializes the queues attributes.
     */
    public GraphicalPlayerAdapter() {
        queueTickets = new ArrayBlockingQueue<>(1);
        queueCards = new ArrayBlockingQueue<>(1);
        queueTurn = new ArrayBlockingQueue<>(1);
        queueSlots = new ArrayBlockingQueue<>(1);
        queueRoute = new ArrayBlockingQueue<>(1);
    }


    /**
     * Initializes the graphicalPlayer in the javaFx thread and communicates it to the main thread using a queue.
     *
     * @see Player#initPlayers(PlayerId, Map)
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        BlockingQueue<GraphicalPlayer> queuePlayer = new ArrayBlockingQueue<>(1);
        runLater(() -> queuePlayer.add(new GraphicalPlayer(ownId, playerNames)));
        player = take(queuePlayer);
    }

    /**
     * Sends the message info to the graphical player through the javaFX thread.
     *
     * @see Player#receiveInfo(String)
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> player.receiveInfo(info));
    }

    /**
     * Calls the setState method of the graphical player through the javaFX thread.
     *
     * @see Player#updateState(PublicGameState, PlayerState)
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> player.setState(newState, ownState));
    }

    /**
     * Calls the chooseTickets method of the graphical player on the javaFX thread.
     * with a handler that adds the player's choice to the queue.
     *
     * @see Player#setInitialTicketChoice(SortedBag)
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> player.chooseTickets(tickets, queueTickets::add));
    }

    /**
     * Calls the endGame method of the graphical player on the javaFX thread.
     *
     * @see Player#endGame(Map.Entry, Map.Entry)
     */
    @Override
    public void endGame(Map.Entry<PlayerId, Integer> winner, Map.Entry<PlayerId, Trail> longestTrailWinner){
        runLater(() -> player.endGame(winner, longestTrailWinner));
    }

    /**
     * Takes the slot already put in the queue if it is called for the first time,
     * otherwise it calls the drawCard method on the javaFX thread before taking the slot from the queue.
     *
     * @see Player#drawSlot()
     */
    @Override
    public int drawSlot() {
        if (!queueSlots.isEmpty()) return queueSlots.remove();
        else {
            runLater(() -> player.drawCard(queueSlots::add));
            return take(queueSlots);
        }
    }

    /**
     * Calls the startTurn method of the graphical player through the javaFX thread with
     * the corresponding handlers which add the player's choice to a queue.
     *
     * @return the taken value from the corresponding queue.
     * @see Player#nextTurn()
     */
    @Override
    public TurnKind nextTurn() {
        runLater(() -> player.startTurn(() -> queueTurn.add(TurnKind.DRAW_TICKETS),

                i -> {
                    queueTurn.add(TurnKind.DRAW_CARDS);
                    queueSlots.add(i);
                },

                (r, c) -> {
                    queueTurn.add(TurnKind.CLAIM_ROUTE);
                    queueCards.add(c);
                    queueRoute.add(r);
                }));
        return take(queueTurn);
    }

    /**
     * Calls the chooseTickets method of the graphical player on the javaFX thread,
     * with a handler that adds the player's choice to the queue.
     *
     * @return the taken value from the corresponding queue.
     * @see Player#chooseTickets(SortedBag)
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        runLater(() -> player.chooseTickets(options, queueTickets::add));
        return take(queueTickets);
    }

    /**
     * Takes the value from the corresponding queue and returns it.
     *
     * @see Player#chooseInitialTickets()
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return take(queueTickets);
    }

    /**
     * Takes the value from the corresponding queue and returns it.
     *
     * @see Player#initialClaimCards()
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return take(queueCards);
    }

    /**
     * Calls the chooseAdditionalCards method of the graphical player on the javaFX thread,
     * with a handler that adds the player's choice to the queue.
     *
     * @return the taken value from the corresponding queue.
     * @see Player#chooseAdditionalCards(List)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> player.chooseAdditionalCards(options, queueCards::add));
        return take(queueCards);
    }

    /**
     * Takes the value from the corresponding queue and returns it.
     *
     * @see Player#claimedRoute()
     */
    @Override
    public Route claimedRoute() {
        return take(queueRoute);
    }


    private <E> E take(BlockingQueue<E> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
}
