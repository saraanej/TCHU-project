package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        RemotePlayerClient distantPlayer =
                new RemotePlayerClient(
                        new GraphicalPlayerAdapter(),
                        getParameters().getRaw().get(0),
                        Integer.getInteger(getParameters().getRaw().get(1)));
        new Thread(() -> distantPlayer.run()).start();
    }
}
