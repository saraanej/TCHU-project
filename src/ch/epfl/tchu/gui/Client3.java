package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Client3 extends Application{

    private final static int PORT_DEFAULT = 5108 + 3;
    private final static String NAME_DEFAULT = "localhost";
    private static final int DEFAULT_NUMBER_PLAYERS = 2;


    /**
     * @see ClientMain#start(Stage);
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * @see Application#start(Stage);
     */
    @Override
    public void start(Stage primaryStage) {
        String name;
        int port;
        List<String> raw = getParameters().getRaw();

        PlayerId.setNumberPlayers(raw.size() > 0 ? Integer.parseInt(raw.get(0)) : DEFAULT_NUMBER_PLAYERS);

        name = (raw.size() > 1) ? raw.get(1) : NAME_DEFAULT;
        port = (raw.size() > 2) ? Integer.parseInt(raw.get(2)) : PORT_DEFAULT;
        RemotePlayerClient distantPlayer = new RemotePlayerClient(
                new GraphicalPlayerAdapter(), name, port);
        new Thread(distantPlayer::run).start();
    }
}
