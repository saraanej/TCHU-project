package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.net.MessageId.*;
import static ch.epfl.tchu.net.Serdes.*;

/**
 * The RemotePlayerProxy instantiable class from the package ch.epfl.tchu.net
 * represents a remote player proxy.
 * It implements the Player interface and can thus play the role of a player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class RemotePlayerProxy implements Player {

    private final static String SPACE = " ";
    private final static String EMPTY_STRING = "";

    private final BufferedReader reader;
    private final BufferedWriter writer;

    /**
     * Public default constructor of a RemotePlayerProxy, creates the BufferedWriters and the BufferedReader
     * to receive and send the messages through the socket's streams.
     *
     * @param socket the socket port of this player
     */
    public RemotePlayerProxy(Socket socket) {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program.
     *
     * @see Player#initPlayers(PlayerId, Map)
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> namesValues = new ArrayList<>();
        for (PlayerId p : PlayerId.all()) namesValues.add(playerNames.get(p));
        String message = String.join(SPACE, PLAYER_ID.serialize(ownId),
                LIST_STRING.serialize(namesValues));
        sendMessage(MessageId.INIT_PLAYERS, message);
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#receiveInfo(String)
     */
    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO, STRING.serialize(info));
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
  //  * @see Player#endGame(PlayerId, int, PlayerId, Trail)
     */
    @Override
    public void endGame(PlayerId winner, Map<PlayerId, Integer> points, PlayerId longestTrailWinner, Map<PlayerId, Trail> longestTrail){
        System.out.println("RemotePlayerProxy");
        System.out.println("winner = " + winner);
        System.out.println("points = " + points);
        System.out.println("longestTrailWinner = " + longestTrailWinner);
        System.out.println("longestTrail = " + longestTrail);;
        String message = String.join(SPACE,
                PLAYER_ID.serialize(winner),
                MAP_ID_INTEGER.serialize(points),
                PLAYER_ID.serialize(longestTrailWinner),
                MAP_ID_TRAIL.serialize(longestTrail));
        sendMessage(MessageId.END_GAME, message);
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#updateState(PublicGameState, PlayerState)
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String message = String.join(SPACE, PUBLIC_GAMESTATE.serialize(newState),
                PLAYERSTATE.serialize(ownState));
        sendMessage(MessageId.UPDATE_STATE, message);
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#setInitialTicketChoice(SortedBag<Ticket>)
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(SET_INITIAL_TICKETS, SORTED_TICKET.serialize(tickets));
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#drawSlot()
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, EMPTY_STRING);
        return INTEGER.deserialize(receiveMessage());
    }

    @Override
    public void showCard(Card c){
        sendMessage(SHOW_CARD,CARD.serialize(c));
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#nextTurn()
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(NEXT_TURN, EMPTY_STRING);
        return TURN_KIND.deserialize(receiveMessage());
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#chooseTickets(SortedBag<Ticket>)
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(CHOOSE_TICKETS, SORTED_TICKET.serialize(options));
        return SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#chooseInitialTickets()
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(CHOOSE_INITIAL_TICKETS, EMPTY_STRING);
        return SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#initialClaimCards()
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(CARDS, EMPTY_STRING);
        return SORTED_CARD.deserialize(receiveMessage());
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#chooseAdditionalCards(List<SortedBag<Card>>)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(CHOOSE_ADDITIONAL_CARDS, LIST_SORTED_CARD.serialize(options));
        return SORTED_CARD.deserialize(receiveMessage());
    }

    /**
     * Sends a message through the network to call the same method on the actual player hosted in another program
     *
     * @see Player#claimedRoute()
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE, EMPTY_STRING);
        return Serdes.ROUTE.deserialize(receiveMessage());
    }


    /**
     * Writes the message in the outputStream of the socket
     *
     * @param Id      the type of the message to send
     * @param message the serialized message to send to the client
     */
    private void sendMessage(MessageId Id, String message) {
        try {
            String send = String.join(SPACE, Id.name(), message);
            writer.write(String.format("%s\n", send));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @return the last message received from the inputStream of the socket
     */
    private String receiveMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }
}
