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
import static ch.epfl.tchu.gui.GuiConstants.*;

/**
 * InfoViewCreator is a private and non-instantiable class.
 * It contains a single public method used to create the information view.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
final class InfoViewCreator {

    private static final int LAST_SEPARATOR_WHEN_ID_IS_LAST = 2;
    private static final int LAST_SEPARATOR = 1;


    private InfoViewCreator(){}


    /**
     * @param id The identity of the player to which the interface corresponds.
     * @param playerNames The associative table of the players' names.
     * @param gameState The observable game state.
     * @param infos A list containing information on the progress of the game.
     * @return The informations' view.
     */
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
       infoView.getStylesheets().addAll(INFO_SS,COLORS_SS);

       VBox playerStats = new VBox();
       playerStats.setId(PLAYER_STATS_ID);

       Separator separator = new Separator();

       for(PlayerId player : players){
           TextFlow nPlayer = new TextFlow();
           nPlayer.getStyleClass().add(player.name());

           Circle circle = new Circle(CIRCLE_RADIUS_INFO);
           circle.getStyleClass().add(FILLED_SC);

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
       gameInfo.setId(GAME_INFO_ID);
       for(Text info : infos) gameInfo.getChildren().add(info);
       Bindings.bindContent(gameInfo.getChildren(), infos);
       infoView.getChildren().addAll(playerStats, separator, gameInfo);

    return infoView;
    }
}