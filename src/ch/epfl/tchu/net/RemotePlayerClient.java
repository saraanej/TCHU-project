package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class RemotePlayerClient {

    private final Player player;
    private final String name;
    private final int port;

    /**
     * Default constructor.
     * @param player (Player) :
     * @param name (String) :
     * @param port (int) :
     */
    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;
    }

    /**
     *
     */
    public void run(){
// on ne doit en sortir que lorsque readLine retourne null,
// car cela signifie que la connexion a été terminée (ce qui signifie que le serveur a planté ou a quitté).

        try(Socket socket = new Socket(name, port);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),
                                    US_ASCII))) {

            while(reader.readLine() != null){

                String[] split = (reader.readLine()).split(Pattern.quote(" "),-1);
                switch(MessageId.valueOf(split[0])){
                    case INIT_PLAYERS:
                        List<String> deserialized = Serdes.LIST_STRING.deserialize(split[2]);
                        Map<PlayerId, String> playerNames = new HashMap<>();
                        playerNames.put(PlayerId.PLAYER_1, deserialized.get(0));
                        playerNames.put(PlayerId.PLAYER_2, deserialized.get(1));
                        player.initPlayers(Serdes.PLAYER_ID.deserialize(split[1]), playerNames);
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(Serdes.STRING.deserialize(split[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(Serdes.PUBLICGAMESTATE.deserialize(split[1]), Serdes.PLAYERSTATE.deserialize(split[2]));
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
                        sendMessage(socket, Serdes.SORTED_TICKET.serialize(player.chooseTickets(Serdes.SORTED_TICKET.deserialize(split[1]))));
                        break;
                    case CHOOSE_INITIAL_TICKETS:
                        sendMessage(socket, Serdes.SORTED_TICKET.serialize(player.chooseInitialTickets()));
                        break;
                    case CARDS:
                        sendMessage(socket, Serdes.SORTED_CARD.serialize(player.initialClaimCards()));
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        sendMessage(socket, Serdes.SORTED_CARD.serialize(player.chooseAdditionalCards(Serdes.LIST_SORTED_CARD.deserialize(split[1]))));
                        break;
                    case ROUTE:
                        sendMessage(socket, Serdes.ROUTE.serialize(player.claimedRoute()));
                        break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @param message (String) The serialized message to send to the client.
     * @throws UncheckedIOException
     */
    private void sendMessage(Socket socket, String message){
        try(BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII))){
            String send = String.join(" ", message, "\n");
            writer.write(send);
            writer.flush();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}