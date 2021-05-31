package ch.epfl.tchu.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static ch.epfl.tchu.gui.GuiConstants.COLORS_SS;

final class EndViewCreator {

    private EndViewCreator() {
    }

    public static Node createEndView(PlayerId id){
        VBox handView = new VBox();
        handView.getStylesheets().addAll("menu.css", COLORS_SS);

        final Label topLabel = new Label("Haut");
        topLabel.setStyle("-fx-alignment: center; -fx-background-color: yellow;");
        topLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        topLabel.setMinHeight(50);

        final Label centerLabel = new Label("Centre");
        centerLabel.setStyle("-fx-alignment: center; -fx-background-color: white;");
        centerLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        final Label bottomLabel = new Label("Bas");
        bottomLabel.setStyle("-fx-alignment: center; -fx-background-color: limegreen;");
        bottomLabel.setMinHeight(50);
        bottomLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        final BorderPane root = new BorderPane();
        root.setTop(topLabel);
        root.setBottom(bottomLabel);
        root.setCenter(centerLabel);







        return null;
    }

}
