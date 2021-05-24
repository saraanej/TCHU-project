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
import static ch.epfl.tchu.gui.GuiConstants.*;

/**
 * The DecksViewCreator is a private and non-instantiable class.
 * It contains two public methods that construct a scene graph
 * representing the player's hand and the decks.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
final class DecksViewCreator {

    private DecksViewCreator(){}


    /**
     * Method creates the view of the player's hand by graphically creating all its components.
     * @param observableGameState (ObservableGameState) : The observable state of the game.
     * @return (Node) The elements that compose the hand's view.
     */
    public static Node createHandView(ObservableGameState observableGameState){
        HBox handView = new HBox();
        handView.getStylesheets().addAll(DECK_SS,COLORS_SS);

        ListView<Ticket> listView = new ListView<>(observableGameState.ticketList());
        listView.setId(TICKETS_ID);

        HBox handPane = new HBox();
        handPane.setId(HAND_PANE_ID);

        for(Card card : Card.ALL){
            StackPane stackPane = new StackPane();
            ReadOnlyIntegerProperty count = observableGameState.numberCardsOfType(card);

            stackPane.visibleProperty().bind(Bindings.greaterThan(count,MINIMUM_VISIBLE_CARD));
            stackPane.getStyleClass().addAll(card == Card.LOCOMOTIVE ? NEUTRAL_SC : card.name(), CARD_SC);

            Text cardCounter = new Text();
            cardCounter.visibleProperty().bind(Bindings.greaterThan(count,MINIMUM_VISIBLE_TEXT));
            cardCounter.textProperty().bind(Bindings.convert(count));
            cardCounter.getStyleClass().add(COUNT_SC);

            createRectangles(stackPane);
            stackPane.getChildren().add(cardCounter);
            handPane.getChildren().add(stackPane);
        }

        handView.getChildren().addAll(listView, handPane);
        return handView;
    }

    /**
     * Method creates graphically the view of the decks in the game.
     * @param observableGameState (ObservableGameState) :  The observable state of the game.
     * @param ticketsHandler (ObjectProperty<ActionHandlers.DrawTicketsHandler>) :
     *                                              Action handler managing the ticket draw.
     * @param cardsHandler (ObjectProperty<ActionHandlers.DrawCardHandler>) :
     *                                              Action handler managing the card draw.
     * @return (Node) The view of the decks.
     */
    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketsHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardsHandler){
        VBox deckView = new VBox();
        deckView.getStylesheets().addAll(DECK_SS,COLORS_SS);
        deckView.setId(CARD_PANE_ID);

        Button ticketsDeck = new Button(StringsFr.TICKETS);
        ticketsDeck.disableProperty().bind(ticketsHandler.isNull());
        ticketsDeck.getStyleClass().add(GAUGED_SC);
        buttonGauge(ticketsDeck, observableGameState.getLeftTickets());
        ticketsDeck.setOnMouseClicked(e -> ticketsHandler.get().onDrawTickets());

        Button cardsDeck = new Button(StringsFr.CARDS);
        cardsDeck.disableProperty().bind(cardsHandler.isNull());
        cardsDeck.getStyleClass().add(GAUGED_SC);
        buttonGauge(cardsDeck, observableGameState.getLeftCards());
        cardsDeck.setOnMouseClicked(e -> cardsHandler.get().onDrawCard(Constants.DECK_SLOT));

        deckView.getChildren().add(ticketsDeck);

        for(Integer i : Constants.FACE_UP_CARD_SLOTS){
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(CARD_SC,"");
            observableGameState.faceUpCard(i).addListener((o,oV,nV) -> {
                if(nV != null)
                    stackPane.getStyleClass()
                            .set(1,nV == Card.LOCOMOTIVE ? NEUTRAL_SC : nV.name());
            });
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

        Rectangle background = new Rectangle(GAUGE_RECTANGLE_WIDTH, GAUGE_RECTANGLE_HEIGHT);
        background.getStyleClass().add(BACKGROUND_SC);
        Rectangle foreground = new Rectangle(GAUGE_RECTANGLE_WIDTH, GAUGE_RECTANGLE_HEIGHT);
        foreground.widthProperty().bind(percentage.multiply(GAUGE_RECTANGLE_WIDTH).divide(100));
        foreground.getStyleClass().add(FOREGROUND_SC);

        group.getChildren().addAll(background,foreground);
        button.setGraphic(group);
    }

    /**
     * Method creates the graphical representation of cards.
     * @param stackPane (StackPane) : The given pane.
     */
    private static void createRectangles(StackPane stackPane){
        Rectangle outside = new Rectangle(OUTSIDE_RECTANGLE_WIDTH, OUTSIDE_RECTANGLE_HEIGHT);
        outside.getStyleClass().add(OUTSIDE_SC);

        Rectangle filledInside = new Rectangle(INSIDE_RECTANGLE_WIDTH, INSIDE_RECTANGLE_HEIGHT);
        filledInside.getStyleClass().addAll(INSIDE_SC, FILLED_SC);

        Rectangle trainImage = new Rectangle(INSIDE_RECTANGLE_WIDTH, INSIDE_RECTANGLE_HEIGHT);
        trainImage.getStyleClass().add(TRAIN_IMAGE_SC);

        stackPane.getChildren().addAll(outside,filledInside,trainImage);
    }
}