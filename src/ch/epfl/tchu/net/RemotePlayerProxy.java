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

    /**
     * @param ownId (PlayerId) : The identity of the player.
     * @param playerNames (Map<PlayerId, String>) : The names of all the players.
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String message = String.join(" ", Serdes.PLAYER_ID.serialize(ownId),
                Serdes.LIST_STRING.serialize(playerNames.values().stream().collect(toList())));
        sendMessage(MessageId.INIT_PLAYERS,message);
    }

    /**
     * @param info (String) : The information that must be communicated to the player.
     */
    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO, Serdes.STRING.serialize(info));
    }

    /**
     * @param newState (PublicGameState) : The new state of the game.
     * @param ownState (PlayerState) : The current state of this player.
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String message = String.join(" ",Serdes.PUBLICGAMESTATE.serialize(newState),
                Serdes.PLAYERSTATE.serialize(ownState));
        sendMessage(MessageId.UPDATE_STATE, message);
    }

    /**
     * @param tickets (SortedBag<Ticket>) : The five tickets being distributed to the player.
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS, Serdes.SORTED_TICKET.serialize(tickets));
    }

    //NOTE POUR LES TESTS, SI ERREUR POSSIBLE QUE CE SOIT A CAUSE DE message = ""
    /**
     * @return (int) The emplacement where the player wishes to draw his cards.
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT, "");
        return Serdes.INTEGER.deserialize(receiveMessage());
    }

    /**
     * @return (TurnKind) The type of action the player wishes to do.
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN, "");
        return Serdes.TURN_KIND.deserialize(receiveMessage());
    }

    /**
     * @param options (SortedBag<Ticket>) : The tickets the player has to choose between.
     * @return (SortedBag<Ticket>) The tickets the player will keep.
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS,Serdes.SORTED_TICKET.serialize(options));
        return Serdes.SORTED_TICKET.deserialize(receiveMessage());
    }

    /**
     * @return (SortedBag<Ticket>) The tickets the player will choose between to keep them.
     */
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

    /**
     * @param options (List<SortedBag<Card>>) : The necessary cards the player has to choose between to take over a tunnel route.
     * @return (SortedBag<Card>) The cards the player chose.
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS,Serdes.LIST_SORTED_CARD.serialize(options));
        return Serdes.SORTED_CARD.deserialize(receiveMessage());
    }

    /**
     * @return (Route) : The route the player decided or tried to take over.
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE, "");
        return Serdes.ROUTE.deserialize(receiveMessage());
    }
}
