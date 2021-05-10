package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;

final class InfoViewCreator {

    private static final int CIRCLE_RADIUS = 5;

    private InfoViewCreator(){}

    public static Node createInfoView(PlayerId id, Map<PlayerId,String> playerNames, ObservableGameState gameState,
                                      ObservableList<Text> infos){

       List<PlayerId> players = List.of(id, PlayerId.ALL.remove(id.ordinal()));
       int lastSep = id.ordinal() == PlayerId.COUNT - 1 ? 2 : 1;

       VBox infoView = new VBox();
       infoView.getStylesheets().addAll("info.css","colors.css");

       VBox playerStats = new VBox();
       playerStats.setId("player-stats");

       Separator separator = new Separator();

       for(PlayerId player : players){

           TextFlow nPlayer = new TextFlow();
           nPlayer.getStyleClass().add(player.name());

           Circle circle = new Circle(CIRCLE_RADIUS);
           circle.getStyleClass().add("filled");

           Text text = new Text();
           text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                   playerNames.get(player),
                   gameState.playerTicketCount(player),
                   gameState.playerCardCount(player),
                   gameState.playerCarCount(player),
                   gameState.playerClaimPoints(player)));

           nPlayer.getChildren().addAll(circle,text);
           playerStats.getChildren().add(nPlayer);

           if(lastSep == 2 && player == id) nPlayer.getChildren().add(separator);
           if(player.ordinal() < PlayerId.COUNT - lastSep) nPlayer.getChildren().add(separator);

       }

       TextFlow gameInfo = new TextFlow();
       gameInfo.setId("game-info");

       for(Text info : infos){
           gameInfo.getChildren().add(info);
       }
       Bindings.bindContent(gameInfo.getChildren(), infos);

       infoView.getChildren().addAll(playerStats, separator, gameInfo);
    return infoView;
    }
}