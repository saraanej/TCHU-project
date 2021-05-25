package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * The RemotePlayerClient instantiable class
 * represents a remote player client.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class RemotePlayerClient {

    private final static String SPACE = " ";

    private final String name;
    private final int port;
    private final Player player;


    /**
     * Default constructor.
     * @param player The given player to which the constructor gives a distant access.
     * @param name The name to use to connect the proxy.
     * @param port The port to use to connect the proxy.
     */
    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;
    }

    /**
     * Depending on the type of message from the proxy, the method run deserializes the arguments
     * and calls the player's corresponding method;
     * If this method returns a result, the method run serializes it
     * to return it to the proxy in response.
     */
    public void run(){
        try(Socket socket = new Socket(name, port);
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),
                                    US_ASCII))) {

            String readLine = reader.readLine();
            while(readLine != null){
                String[] split = readLine.split(Pattern.quote(SPACE),-1);
                switch (MessageId.valueOf(split[0])) {
                    case INIT_PLAYERS:
                        List<String> deserialized = Serdes.LIST_STRING.deserialize(split[2]);
                        player.initPlayers(Serdes.PLAYER_ID.deserialize(split[1]),
                                           Map.of(PlayerId.PLAYER_1, deserialized.get(0),
                                                   PlayerId.PLAYER_2, deserialized.get(1)));
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(Serdes.STRING.deserialize(split[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(Serdes.PUBLIC_GAMESTATE.deserialize(split[1]),
                                           Serdes.PLAYERSTATE.deserialize(split[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(Serdes.SORTED_TICKET.deserialize(split[1]));
                        break;
                    case DRAW_SLOT:
                        sendMessage(socket,Serdes.INTEGER.serialize(player.drawSlot()));
                        break;
                    case NEXT_TURN:
                        sendMessage(socket, Serdes.TURN_KIND.serialize(player.nextTurn()));
                        break;
                    case CHOOSE_TICKETS:
                        sendMessage(socket, Serdes.SORTED_TICKET.serialize(
                                            player.chooseTickets(
                                            Serdes.SORTED_TICKET.deserialize(split[1]))));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        sendMessage(socket, Serdes.SORTED_TICKET.serialize(
                                            player.chooseInitialTickets()));
                        break;
                    case CARDS:
                        sendMessage(socket, Serdes.SORTED_CARD.serialize(player.initialClaimCards()));
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        sendMessage(socket, Serdes.SORTED_CARD.serialize(
                                            player.chooseAdditionalCards(
                                            Serdes.LIST_SORTED_CARD.deserialize(split[1]))));
                        break;
                    case ROUTE:
                        sendMessage(socket, Serdes.ROUTE.serialize(player.claimedRoute()));
                        break;
                }
                readLine= reader.readLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * @param socket the socket being used to exchange data
     *                          with the entity connected at the other end.
     * @param message The serialized message to send to the client.
     */
    private void sendMessage(Socket socket, String message){
        try{BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
            writer.write(String.format("%s\n",message));
            writer.flush();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}