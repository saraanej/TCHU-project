package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class ClientMain extends Application {

    private final static int FIRST_ARG_INDEX = 0;
    private final static int SECOND_ARG_INDEX = 1;
    private final static int PORT_DEFAULT = 5108;
    private final static String NAME_DEFAULT = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String name;
        int port;
        List<String> parameters = getParameters().getRaw();

        name = (parameters.size() > FIRST_ARG_INDEX) ? parameters.get(FIRST_ARG_INDEX) : NAME_DEFAULT;
        port = (parameters.size() > SECOND_ARG_INDEX) ? Integer.getInteger(
                                                            parameters.get(SECOND_ARG_INDEX)) : PORT_DEFAULT;
        RemotePlayerClient distantPlayer = new RemotePlayerClient(
                                                  new GraphicalPlayerAdapter(),name,port);
        new Thread(distantPlayer::run).start();
    }
}
