package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class InfoViewCreator {

    private static final int LAST_SEPARATOR_WHEN_ID_IS_LAST = 2;
    private static final int LAST_SEPARATOR = 1;
    private static final int CIRCLE_RADIUS = 5;
    private static final String INFO = "info.css";
    private static final String COLORS = "colors.css";
    private static final String PLAYER_STATS = "player-stats";
    private static final String FILLED = "filled";
    private static final String GAME_INFO = "game-info";

    private InfoViewCreator(){}

    public static Node createInfoView(PlayerId id, Map<PlayerId,String> playerNames,
                                      ObservableGameState gameState,
                                      ObservableList<Text> infos){

       //Flexible for more than two players.
       List<PlayerId> players = new ArrayList<>();
       players.add(id);
       for(PlayerId player : PlayerId.ALL)
           if(player != id) players.add(player);

       int lastIndex = PlayerId.COUNT - 1;
       int lastSep = id.ordinal() == lastIndex ? LAST_SEPARATOR_WHEN_ID_IS_LAST : LAST_SEPARATOR;

       VBox infoView = new VBox();
       infoView.getStylesheets().addAll(INFO,COLORS);

       VBox playerStats = new VBox();
       playerStats.setId(PLAYER_STATS);

       Separator separator = new Separator();

       for(PlayerId player : players){

           TextFlow nPlayer = new TextFlow();
           nPlayer.getStyleClass().add(player.name());

           Circle circle = new Circle(CIRCLE_RADIUS);
           circle.getStyleClass().add(FILLED);

           Text text = new Text();
           text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                   playerNames.get(player),
                   gameState.playerTicketCount(player),
                   gameState.playerCardCount(player),
                   gameState.playerCarCount(player),
                   gameState.playerClaimPoints(player)));

           nPlayer.getChildren().addAll(circle,text);
           playerStats.getChildren().add(nPlayer);

           if(lastSep == LAST_SEPARATOR_WHEN_ID_IS_LAST && player == id)
               nPlayer.getChildren().add(separator);
           if(player.ordinal() < PlayerId.COUNT - lastSep) nPlayer.getChildren().add(separator);

       }

       TextFlow gameInfo = new TextFlow();
       gameInfo.setId(GAME_INFO);

       for(Text info : infos){
           gameInfo.getChildren().add(info);
       }
       Bindings.bindContent(gameInfo.getChildren(), infos);

       infoView.getChildren().addAll(playerStats, separator, gameInfo);
    return infoView;
    }
}
