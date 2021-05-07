package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
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
 * The DecksViewCreator is a private and non-instantiable class.
 * It contains two public methods that construct a scene graph representing maps.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */

final class DecksViewCreator {

    static final int MIN_FOR_CARD_VISIBILITY = 0;
    static final int MIN_FOR_TEXT_VISIBILITY = 1;
    static final int WIDTH_OUTSIDE_CARD = 60;
    static final int HEIGHT_OUTSIDE_CARD = 90;
    static final int WIDTH_BUTTON = 50;
    static final int HEIGHT_BUTTON = 5;
    static final int WIDTH_CARD = 40;
    static final int HEIGHT_CARD = 70;


    /**
     * Method creates the view of the player's hand by graphically creating all its components.
     * @param observableGameState (ObservableGameState) : The observable state of the game.
     * @return (Node) The elements that compose the hand's view.
     */
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

            stackPane.visibleProperty().bind(Bindings.greaterThan(count,MIN_FOR_CARD_VISIBILITY));
            stackPane.getStyleClass().addAll(card == Card.LOCOMOTIVE ? "NEUTRAL" : card.name(), "card");

            Text cardCounter = new Text();
            cardCounter.visibleProperty().bind(Bindings.greaterThan(count,MIN_FOR_TEXT_VISIBILITY));
            cardCounter.textProperty().bind(Bindings.convert(count));
            cardCounter.getStyleClass().add("count");

            createRectangles(stackPane);
            stackPane.getChildren().add(cardCounter);
            handPane.getChildren().add(stackPane);
        }

        handView.getChildren().addAll(handPane, listView);

        return handView;
    }


    /**
     * Method creates graphically the view of the decks in the game.
     * @param observableGameState (ObservableGameState) :  The observable state of the game.
     * @param ticketsHandler (ObjectProperty<ActionHandlers.DrawTicketsHandler>) : Action handler managing the ticket draw.
     * @param cardsHandler (ObjectProperty<ActionHandlers.DrawCardHandler>) : Action handler managing the card draw.
     * @return (Node) The view of the decks.
     */
    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketsHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardsHandler){
        VBox deckView = new VBox();
        deckView.getStylesheets().addAll("decks.css","colors.css");
        deckView.setId("card-pane");

        Button ticketsDeck = new Button("Billets");
        ticketsDeck.disableProperty().bind(ticketsHandler.isNull());
        ticketsDeck.getStyleClass().addAll("gauged");
        buttonGauge(ticketsDeck, observableGameState.getLeftTickets());
        ticketsDeck.setOnMouseClicked(e -> ticketsHandler.get().onDrawTickets());

        Button cardsDeck = new Button("Cartes");
        cardsDeck.disableProperty().bind(cardsHandler.isNull());
        cardsDeck.getStyleClass().add("gauged");
        buttonGauge(cardsDeck, observableGameState.getLeftCards());
        cardsDeck.setOnMouseClicked(e -> cardsHandler.get().onDrawCard(-1));

        deckView.getChildren().add(ticketsDeck);
        
        for(Integer i : Constants.FACE_UP_CARD_SLOTS){
            StackPane stackPane = new StackPane();
            observableGameState.faceUpCard(i).addListener((o,oV, nV) -> {
                if(nV != null) stackPane.getStyleClass().addAll(nV.name(),"card");});
            createRectangles(stackPane);
            stackPane.setOnMouseClicked(e -> cardsHandler.get().onDrawCard(i));
            deckView.getChildren().add(stackPane);
        }

        deckView.getChildren().add(cardsDeck);
        return deckView;
    }


    /**
     * Method creates the graphical representation of the gauge buttons.
     * @param button (Button) : The given button.
     * @param percentage (ReadOnlyInteger) : Percentage of elements in the deck.
     */
    private static void buttonGauge(Button button, ReadOnlyIntegerProperty percentage){
        Group group = new Group();

        Rectangle background = new Rectangle(WIDTH_BUTTON,HEIGHT_BUTTON);
        background.getStyleClass().add("background");
        Rectangle foreground = new Rectangle(WIDTH_BUTTON,HEIGHT_BUTTON);
        foreground.widthProperty().bind(percentage.multiply(WIDTH_BUTTON).divide(100));
        foreground.getStyleClass().add("foreground");

        group.getChildren().addAll(background,foreground);
        button.setGraphic(group);
    }

    /**
     * Method creates the graphical representation of cards.
     * @param stackPane (StackPane) : The given pane.
     */
    private static void createRectangles(StackPane stackPane){
        Rectangle outside = new Rectangle(WIDTH_OUTSIDE_CARD,HEIGHT_OUTSIDE_CARD);
        outside.getStyleClass().add("outside");

        Rectangle filledInside = new Rectangle(WIDTH_CARD,HEIGHT_CARD);
        filledInside.getStyleClass().addAll("inside", "filled");

        Rectangle trainImage = new Rectangle(WIDTH_CARD,HEIGHT_CARD);
        trainImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(outside,filledInside,trainImage);
    }

}
