package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Random;

public class ServerMain extends Application {

    private static final String PLAYER_1_DEFAULT = "Ada";
    private static final String PLAYER_2_DEFAULT = "Charles";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String player1, player2;
        player1 = (getParameters().getRaw().size() >= 1) ? getParameters().getRaw().get(0) : PLAYER_1_DEFAULT;
        player2 = (getParameters().getRaw().size() >= 2) ? getParameters().getRaw().get(1) : PLAYER_2_DEFAULT;

        try {
            ServerSocket socket = new ServerSocket(5108);
            socket.accept();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }

        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Map<PlayerId, String> names =
                Map.of(PlayerId.PLAYER_1, player1, PlayerId.PLAYER_2, player2);
        Map<PlayerId, Player> players =
                Map.of(PlayerId.PLAYER_1, new GraphicalPlayerAdapter(),
                       PlayerId.PLAYER_2, new );


        new Thread(() -> Game.play(players, names, tickets, rng))
                .start();


    }
}
