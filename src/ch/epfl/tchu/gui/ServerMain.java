package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;

/**
 * The public ServerMain class contains
 * the main program for the tCHu server.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class ServerMain extends Application {

    private static final int SOCKET_PORT = 5108;
    private static final List<String> PLAYER_NAMES = List.of("Ada","Charles","Alice","Bob");
    private static final int DEFAULT_NUMBER_PLAYERS = 2;


    /**
     * @see ServerMain#start(Stage);
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @see Application#start(Stage);
     */
    @Override
    public void start(Stage primaryStage) {
        try {


            List<String> raw = getParameters().getRaw();

            Map<PlayerId, String> names = new EnumMap<>(PlayerId.class);
            Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);

            PlayerId.setNumberPlayers(raw.size() > 0 ? Integer.parseInt(raw.get(0)) : DEFAULT_NUMBER_PLAYERS);
            // todo mettre un seul serversocket

            for (PlayerId id : PlayerId.all()) {
                int indexId = id.ordinal();
                names.put(id, (raw.size() > indexId + 1) ? raw.get(indexId + 1) : PLAYER_NAMES.get(indexId));
                players.put(id, id.equals(PLAYER_1) ? new GraphicalPlayerAdapter() :
                        new RemotePlayerProxy(new ServerSocket(SOCKET_PORT + indexId).accept()));
            }

            SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
            Random rng = new Random();
            new Thread(() -> Game.play(players, names, tickets, rng)).start();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
