package ch.epfl.tchu.gui;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

import static ch.epfl.tchu.gui.GuiConstants.COLORS_SS;

final class MenuViewCreator {

    private MenuViewCreator() {
    }

    public static Node createMenuView(){
        HBox handView = new HBox();
        handView.getStylesheets().addAll("menu.css", COLORS_SS);




        return null;
    }

}
