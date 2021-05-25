package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The public ServerMain class contains
 * the main program for the tCHu server.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class ServerMain extends Application {

    private static final int SOCKET_PORT = 5108;
    private static final String PLAYER_1_DEFAULT = "Yas";
    private static final String PLAYER_2_DEFAULT = "Ali";


    /**
     * @see ServerMain#start(Stage);
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * @see Application#start(Stage);
     * @throws UncheckedIOException if an IOException is thrown.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ServerSocket socket = new ServerSocket(SOCKET_PORT);
            List<String> parameters = getParameters().getRaw();
            String player1, player2;

            player1 = (parameters.size() > 0) ? parameters.get(0) : PLAYER_1_DEFAULT;
            player2 = (parameters.size() > 1) ? parameters.get(1) : PLAYER_2_DEFAULT;
            Random rng = new Random();
            SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
            Map<PlayerId, String> names =
                    Map.of(PlayerId.PLAYER_1, player1, PlayerId.PLAYER_2, player2);
            Map<PlayerId, Player> players =
                    Map.of(PlayerId.PLAYER_1, new GraphicalPlayerAdapter(),
                           PlayerId.PLAYER_2, new RemotePlayerProxy(socket.accept()));

            new Thread(() -> Game.play(players, names, tickets, rng)).start();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
