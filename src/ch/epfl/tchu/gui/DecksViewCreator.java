package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Non instanciable and priavte
 */

final class DecksViewCreator {

    // retourne la vue de la main
    public static Node createHandView(ObservableGameState observableGameState){
        HBox handView = new HBox();
        handView.getStylesheets().addAll("decks.css","colors.css");

        ListView<Ticket> listView = new ListView<>(observableGameState.ticketList().get());
        listView.setId("tickets");

        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        for(Card card : Card.ALL){
            StackPane stackPane = new StackPane();

            ReadOnlyIntegerProperty count = observableGameState.numberCardsOfType(card);

            stackPane.visibleProperty().bind(Bindings.greaterThan(count,0));
            stackPane.getStyleClass().addAll(card == Card.LOCOMOTIVE ? "NEUTRAL" : card.name(), "card");

            Text cardCounter = new Text();
            cardCounter.visibleProperty().bind(Bindings.greaterThan(count,1));
            cardCounter.textProperty().bind(Bindings.convert(count));
            cardCounter.getStyleClass().add("count");

            createRectangles(stackPane);
            stackPane.getChildren().add(cardCounter);
            handPane.getChildren().add(stackPane);
        }

        handView.getChildren().addAll(listView, handPane);

        return handView;
    }


    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketsHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardsHandler){
        VBox deckView = new VBox();
        deckView.getStylesheets().addAll("decks.css","colors.css");
        deckView.setId("card-pane");

        Button ticketsDeck = new Button();
        ticketsDeck.disableProperty().bind(ticketsHandler.isNull());
        ticketsDeck.getStyleClass().add("gauged");
        buttonGauge(ticketsDeck, observableGameState.getLeftTickets());

        ticketsDeck.setOnMouseClicked(e -> {
            ticketsHandler.get().onDrawTickets();
        });

        Button cardsDeck = new Button();
        cardsDeck.disableProperty().bind(cardsHandler.isNull());
        cardsDeck.getStyleClass().add("gauged");
        buttonGauge(cardsDeck, observableGameState.getLeftCards());

        cardsDeck.setOnMouseClicked(e -> {
            cardsHandler.get().onDrawCard(-1);
        });

        for(int i = 0; i < Constants.FACE_UP_CARDS_COUNT; ++i){
            StackPane stackPane = new StackPane();
            final int index = i; //TODO voir s'il y a une meilleure solution
            observableGameState.faceUpCard(i).addListener((o,oV, nV) -> {
                if(nV != null) stackPane.getStyleClass().addAll(nV.name(),"card");
            });
            createRectangles(stackPane);
            stackPane.setOnMouseClicked(e -> {
                cardsHandler.get().onDrawCard(index);
            });
            deckView.getChildren().add(stackPane);
        }

        deckView.getChildren().addAll(ticketsDeck,cardsDeck);

        return deckView;
    }

    private static void buttonGauge(Button button, ReadOnlyIntegerProperty percentage){
        Group group = new Group();

        Rectangle background = new Rectangle(50,5);
        background.getStyleClass().add("background");
        Rectangle foreground = new Rectangle(50,5);
        foreground.widthProperty().bind(percentage.multiply(50).divide(100));
        foreground.getStyleClass().add("foreground");

        group.getChildren().addAll(background,foreground);
        button.setGraphic(group);
    }

    private static void createRectangles(StackPane stackPane){
        Rectangle outside = new Rectangle(60,90);
        outside.getStyleClass().add("Outside");

        Rectangle filledInside = new Rectangle(40,70);
        filledInside.getStyleClass().addAll("filled", "inside");

        Rectangle trainImage = new Rectangle(40,70);
        trainImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(outside, filledInside,trainImage);
    }
    
}
