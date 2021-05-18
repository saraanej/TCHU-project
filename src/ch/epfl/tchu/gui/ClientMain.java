package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private final static String NAME_DEFAULT = "localhost";
    private final static int PORT_DEFAULT = 5108;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String name;
        int port;
        name = (getParameters().getRaw().size() >= 1) ? getParameters().getRaw().get(0) : NAME_DEFAULT;
        port = (getParameters().getRaw().size() >= 2) ? Integer.getInteger(
                                                        getParameters().getRaw().get(1)) : PORT_DEFAULT;
        RemotePlayerClient distantPlayer =
                new RemotePlayerClient(
                        new GraphicalPlayerAdapter(),name,port);
        new Thread(() -> distantPlayer.run()).start();
    }
}
