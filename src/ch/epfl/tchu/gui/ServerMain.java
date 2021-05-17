package ch.epfl.tchu.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

    private static final String PLAYER_1_DEFAULT = "Ada";
    private static final String PLAYER_2_DEFAULT = "Charles";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String player1;
        String player2;
        if(getParameters().getRaw().size() == 2) {

        }


    }
}
