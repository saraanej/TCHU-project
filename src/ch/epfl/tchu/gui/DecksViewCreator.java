package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Non instanciable and priavte
 */

final class DecksViewCreator {

    //private static final ObservableProperty<Ticket> ticketProperty = new SimpleObjectProperty<>();
    //private final ObservableList<Card> cardProperty = new SimpleObjectProperty<>();


    // retourne la vue de la main
    public static Node createHandView(ObservableGameState observableGameState){
        HBox handView = new HBox();
        handView.getStylesheets().addAll("decks.css","colors.css");

        ListView<Ticket> listView = new ListView<Ticket>(observableGameState.ticketList().get());
        listView.setId("tickets");
        handView.getChildren().add(listView);

        handView.setId("hand-pane");

        StackPane v = new StackPane();
        v. getChildren().addAll();

        for(Card c : Card.ALL){

        }

        return handView;
    }

    //
    public void createCardsView(ObservableGameState observableGameState){

        HBox handsView = new HBox(8);
       // ListView billets =
       // handsView.getChildren().add()
    }



}
