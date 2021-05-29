package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Client2 extends Application{

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
        port = (parameters.size() > 1) ? Integer.parseInt(parameters.get(1)) : PORT_DEFAULT + 2;
        RemotePlayerClient distantPlayer = new RemotePlayerClient(
                new GraphicalPlayerAdapter(), name, port);
        new Thread(distantPlayer::run).start();
    }
}
