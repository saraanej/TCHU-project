package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
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
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("DeFonarts Bold.otf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Doland-Regular.otf")));

        } catch (IOException |FontFormatException e) {
        }
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

        name = (raw.size() > 0) ? raw.get(0) : NAME_DEFAULT;
        port = (raw.size() > 1) ? Integer.parseInt(raw.get(1)) : PORT_DEFAULT;
        RemotePlayerClient distantPlayer = new RemotePlayerClient(
                new GraphicalPlayerAdapter(), name, port);
        new Thread(distantPlayer::run).start();
    }
}
