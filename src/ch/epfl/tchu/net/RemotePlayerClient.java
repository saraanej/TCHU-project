package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.net.Serdes.*;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * The RemotePlayerClient instantiable class from the package ch.epfl.tchu.net
 * represents a remote player client.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class RemotePlayerClient {

    private final static String SPACE = " ";

    private final String name;
    private final int port;
    private final Player player;
    private BufferedWriter writer;


    /**
     * Default constructor.
     *
     * @param player The given player to which the constructor gives a distant access.
     * @param name   The name to use to connect the proxy.
     * @param port   The port to use to connect the proxy.
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
    public void run() {
        try (Socket socket = new Socket(name, port);
             BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII))) {

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));

            String readLine = reader.readLine();
            while (readLine != null) {
                String[] split = readLine.split(Pattern.quote(SPACE), -1);
                switch (MessageId.valueOf(split[0])) {
                    case INIT_PLAYERS:
                        List<String> deserialized = LIST_STRING.deserialize(split[2]);
                        PlayerId.setNumberPlayers(deserialized.size());
                        Map<PlayerId,String> names = new EnumMap<>(PlayerId.class);
                        for (PlayerId id: PlayerId.all())
                            names.put(id,deserialized.get(id.ordinal()));
                        player.initPlayers(PLAYER_ID.deserialize(split[1]), names);
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(STRING.deserialize(split[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(PUBLIC_GAMESTATE.deserialize(split[1]),
                                PLAYERSTATE.deserialize(split[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(SORTED_TICKET.deserialize(split[1]));
                        break;
                    case END_GAME:
                        player.endGame(PLAYER_ID.deserialize(split[1]),
                                       MAP_ID_INTEGER.deserialize(split[2]),
                                       PLAYER_ID.deserialize(split[3]),
                                       MAP_ID_TRAIL.deserialize(split[4]));
                        break;
                    case DRAW_SLOT:
                        sendMessage(INTEGER.serialize(player.drawSlot()));
                        break;
                    case SHOW_CARD:
                        player.showCard(CARD.deserialize(split[1]));
                        break;
                    case NEXT_TURN:
                        sendMessage(TURN_KIND.serialize(player.nextTurn()));
                        break;
                    case CHOOSE_TICKETS:
                        sendMessage(SORTED_TICKET.serialize(
                                player.chooseTickets(
                                        SORTED_TICKET.deserialize(split[1]))));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        sendMessage(SORTED_TICKET.serialize(
                                player.chooseInitialTickets()));
                        break;
                    case CARDS:
                        sendMessage(SORTED_CARD.serialize(player.initialClaimCards()));
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        sendMessage(SORTED_CARD.serialize(
                                player.chooseAdditionalCards(
                                        LIST_SORTED_CARD.deserialize(split[1]))));
                        break;
                    case ROUTE:
                        sendMessage(ROUTE.serialize(player.claimedRoute()));
                        break;
                }
                readLine = reader.readLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * @param message The serialized message to send to the client.
     */
    private void sendMessage(String message) {
        try {
            writer.write(String.format("%s\n", message));
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}