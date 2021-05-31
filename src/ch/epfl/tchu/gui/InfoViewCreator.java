package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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

    private InfoViewCreator() {
    }


    /**
     * @param id          The identity of the player to which the interface corresponds.
     * @param playerNames The associative table of the players' names.
     * @param gameState   The observable game state.
     * @param infos       A list containing information on the progress of the game.
     * @return The informations' view.
     */
    public static Node createInfoView(PlayerId id, Map<PlayerId, String> playerNames,
                                      ObservableGameState gameState,
                                      ObservableList<Text> infos) {
        VBox infoView = new VBox();
        infoView.getStylesheets().addAll(INFO_SS, COLORS_SS);

        VBox playerStats = new VBox();
        playerStats.setId(PLAYER_STATS_ID);

        for (PlayerId player : PlayerId.all())
            playersInfo(player, id, playerStats, playerNames, gameState);

        playerStats.getChildren().get(0);
        FXCollections.rotate(playerStats.getChildren(), -id.ordinal());
        TextFlow gameInfo = gameInfo(infos);

        infoView.getChildren().addAll(playerStats, new Separator(), gameInfo);

        return infoView;
    }


    private static void playersInfo(PlayerId player, PlayerId ownId, VBox playerStats,
                                    Map<PlayerId, String> playerNames, ObservableGameState gameState) {
        TextFlow nPlayer = new TextFlow();
        nPlayer.getStyleClass().add(player.name());

        Circle circle = new Circle(CIRCLE_RADIUS_PLAYER_INFO);
        circle.getStyleClass().add(FILLED_SC);

        Text text = new Text();
        text.textProperty().bind(player == ownId ? Bindings.format(StringsFr.PRIVATE_PLAYER_STATS,
                playerNames.get(player),
                gameState.playerTicketCount(player),
                gameState.playerCardCount(player),
                gameState.playerCarCount(player),
                gameState.playerClaimPoints(player),
                gameState.playerTicketPoints()) : Bindings.format(StringsFr.PUBLIC_PLAYER_STATS,
                playerNames.get(player),
                gameState.playerTicketCount(player),
                gameState.playerCardCount(player),
                gameState.playerCarCount(player),
                gameState.playerClaimPoints(player)));
        nPlayer.getChildren().addAll(circle, text);
        playerStats.getChildren().add(nPlayer);
    }

    private static TextFlow gameInfo(ObservableList<Text> infos) {
        TextFlow gameInfo = new TextFlow();
        gameInfo.setId(GAME_INFO_ID);
        for (Text info : infos) gameInfo.getChildren().add(info);
        Bindings.bindContent(gameInfo.getChildren(), infos);

        return gameInfo;
    }
}