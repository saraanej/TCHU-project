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
 * represents a remote player proxy. It implements the Player interface and can thus play the role of a player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class RemotePlayerProxy implements Player {

    private final Socket socket;

    public RemotePlayerProxy(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @param message (String) the serialized message to send to the client
     */
    private void sendMessage(MessageId Id, String message){
        try(BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII))){
            String send = String.join(" ", Id.name(), message, "\n");
            writer.write(send);
            writer.flush();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private String receiveMessage(){
        try(BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII))) {
            return reader.readLine();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }


    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String message = String.join(" ", Serdes.PLAYER_ID.serialize(ownId),
                Serdes.LIST_STRING.serialize(playerNames.values().stream().collect(toList())));
        sendMessage(MessageId.INIT_PLAYERS,message);
    }

    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO, Serdes.STRING.serialize(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String message = String.join(" ",Serdes.PUBLICGAMESTATE.serialize(newState),
                Serdes.PLAYERSTATE.serialize(ownState));
        sendMessage(MessageId.UPDATE_STATE, message);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS, Serdes.SORTED_TICKET.serialize(tickets));
    }

    //NOTE POUR LES TESTS, SI ERREUR POSSIBLE QUE CE SOIT A CAUSE DE message = ""
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, "");
        return Serdes.INTEGER.deserialize(receiveMessage());
    }

    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN, "");
        return Serdes.TURN_KIND.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS,Serdes.SORTED_TICKET.serialize(options));
        return Serdes.SORTED_TICKET.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS, "");
        return Serdes.SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * @return (SortedBag<Card>) : The cards the player wishes to play to take over a route.
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS,"");
        return Serdes.SORTED_CARD.deserialize(receiveMessage());
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS,Serdes.LIST_SORTED_CARD.serialize(options));
        return Serdes.SORTED_CARD.deserialize(receiveMessage());
    }

    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE, "");
        return Serdes.ROUTE.deserialize(receiveMessage());
    }
}
