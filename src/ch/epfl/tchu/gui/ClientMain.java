package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * The public ClientMain class contains
 * the main program for the tCHu client.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class ClientMain extends Application {

    private final static int PORT_DEFAULT = 5108;
    private final static String NAME_DEFAULT = "localhost";


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
        List<String> parameters = getParameters().getRaw();

        name = (parameters.size() > 0) ? parameters.get(0) : NAME_DEFAULT;
        port = (parameters.size() > 1) ? Integer.parseInt(parameters.get(1)) : PORT_DEFAULT;
        RemotePlayerClient distantPlayer = new RemotePlayerClient(
                new GraphicalPlayerAdapter(), name, port);
        new Thread(distantPlayer::run).start();
    }
}
