package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

/**
 * The RemotePlayerProxy instantiable class in the ch.epfl.tchu.net package
 * represents a remote player proxy.
 * It implements the Player interface and can thus play the role of a player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class RemotePlayerProxy implements Player {

    private static String SPACE = " ";
    private static String EMPTY_STRING = "";

    private final Socket socket;

    /**
     * Public default constructor of a RemotePlayerProxy
     * @param socket
     */
    public RemotePlayerProxy(Socket socket) {
        this.socket = socket;
    }

    /**
     * @see Player#initPlayers(PlayerId, Map) 
     * @throws UncheckedIOException if an error occurs when sending a message.
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String message = String.join(SPACE, Serdes.PLAYER_ID.serialize(ownId),
                Serdes.LIST_STRING.serialize(playerNames.values().stream().collect(toList())));
        sendMessage(MessageId.INIT_PLAYERS,message);
    }

    /**
     * @see Player#receiveInfo(String) 
     * @throws UncheckedIOException if an error occurs when sending a message.
     */
    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO, Serdes.STRING.serialize(info));
    }

    /**
     * @see Player#updateState(PublicGameState, PlayerState) 
     * @throws UncheckedIOException if an error occurs when sending a message.
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String message = String.join(SPACE,Serdes.PUBLIC_GAMESTATE.serialize(newState),
                Serdes.PLAYERSTATE.serialize(ownState));
        sendMessage(MessageId.UPDATE_STATE, message);
    }

    /**
     * @see Player#setInitialTicketChoice(SortedBag<Ticket>) 
     * @throws UncheckedIOException if an error occurs when sending a message.
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS, Serdes.SORTED_TICKET.serialize(tickets));
    }

    /**
     * @see Player#drawSlot()
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, EMPTY_STRING);
        return Serdes.INTEGER.deserialize(receiveMessage());
    }

    /**
     * @see Player#nextTurn()
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN, EMPTY_STRING);
        return Serdes.TURN_KIND.deserialize(receiveMessage());
    }

    /**
     * @see Player#chooseTickets(SortedBag<Ticket>)
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS,Serdes.SORTED_TICKET.serialize(options));
        return Serdes.SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * @see Player#chooseInitialTickets()
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS, EMPTY_STRING);
        return Serdes.SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * @see Player#initialClaimCards()
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS,EMPTY_STRING);
        return Serdes.SORTED_CARD.deserialize(receiveMessage());
    }

    /**
     * @see Player#chooseAdditionalCards(List<SortedBag<Card>>)
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS,Serdes.LIST_SORTED_CARD.serialize(options));
        return Serdes.SORTED_CARD.deserialize(receiveMessage());
    }

    /**
     * @see Player#claimedRoute()
     *
     * @throws UncheckedIOException if an error occurs when reading the received message or sending a message.
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE, EMPTY_STRING);
        return Serdes.ROUTE.deserialize(receiveMessage());
    }


    /**
     * Writes the message in the outputStream of the socket
     * @param Id the type of the message to send
     * @param message the serialized message to send to the client
     */
    private void sendMessage(MessageId Id, String message){
        try{
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
            String send = String.join(SPACE, Id.name(), message);
            writer.write(String.format("%s\n",send));
            writer.flush();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     *
     * @return the last message received from the inputStream of the socket
     */
    private String receiveMessage(){
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            return reader.readLine();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
