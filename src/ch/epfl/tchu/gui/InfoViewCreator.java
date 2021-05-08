package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.Map;

public class InfoViewCreator {

    private InfoViewCreator(){}

    public static Node createInfoView(PlayerId id, Map<PlayerId,String> playerNames, ObservableGameState gameState,
                                      ObservableList<Text> infos){
        return null;
    }
}
