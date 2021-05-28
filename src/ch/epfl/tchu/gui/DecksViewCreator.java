package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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

    private final static int MAXIMAL_GAUGE_WIDTH = 50;

    private DecksViewCreator() {
    }


    /**
     * Method creates the view of the player's hand by graphically creating all its components.
     *
     * @param observableGameState The observable state of the game.
     * @return The elements that compose the hand's view.
     */
    public static Node createHandView(ObservableGameState observableGameState) {
        HBox handView = new HBox();
        handView.getStylesheets().addAll(DECK_SS, COLORS_SS);

        ListView<Ticket> listView = new ListView<>(observableGameState.ticketList());
        listView.setId(TICKETS_ID);

        HBox handPane = new HBox();
        handPane.setId(HAND_PANE_ID);

        for (Card card : Card.ALL) {
            StackPane stackPane = new StackPane();
            ReadOnlyIntegerProperty count = observableGameState.numberCardsOfType(card);

            stackPane.visibleProperty().bind(Bindings.greaterThan(count, MINIMUM_VISIBLE_CARD));
            stackPane.getStyleClass().addAll(card == Card.LOCOMOTIVE ? NEUTRAL_SC : card.name(), CARD_SC);

            Text cardCounter = new Text();
            cardCounter.visibleProperty().bind(Bindings.greaterThan(count, MINIMUM_VISIBLE_TEXT));
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
     *
     * @param observableGameState The observable state of the game.
     * @param ticketsHandler      Action handler managing the ticket draw.
     * @param cardsHandler        Action handler managing the card draw.
     * @return The view of the decks.
     */
    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketsHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardsHandler) {
        VBox deckView = new VBox();
        deckView.getStylesheets().addAll(DECK_SS, COLORS_SS);
        deckView.setId(CARD_PANE_ID);

        Button ticketsDeck = buttonGauge(true, observableGameState, ticketsHandler, cardsHandler);
        Button cardsDeck = buttonGauge(false, observableGameState, ticketsHandler, cardsHandler);

        deckView.getChildren().add(ticketsDeck);

        for (Integer i : Constants.FACE_UP_CARD_SLOTS) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(CARD_SC, "");
            observableGameState.faceUpCard(i).addListener((o, oV, nV) -> {
                if (nV != null)
                    stackPane.getStyleClass()
                            .set(1, nV == Card.LOCOMOTIVE ? NEUTRAL_SC : nV.name());
            });
            createRectangles(stackPane);
            stackPane.disableProperty().bind(cardsHandler.isNull());
            stackPane.setOnMouseClicked(e -> cardsHandler.get().onDrawCard(i));
            deckView.getChildren().add(stackPane);
        }

        deckView.getChildren().add(cardsDeck);
        return deckView;
    }


    /**
     * Method creates the graphical representation of the gauge buttons.
     */
    private static Button buttonGauge(boolean isTicketsDeck, ObservableGameState observableGameState,
                                      ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketsHandler,
                                      ObjectProperty<ActionHandlers.DrawCardHandler> cardsHandler) {

        Button deck = new Button(isTicketsDeck ? StringsFr.TICKETS : StringsFr.CARDS);
        BooleanBinding handlerIsNull = isTicketsDeck ? ticketsHandler.isNull() : cardsHandler.isNull();
        deck.disableProperty().bind(handlerIsNull);
        deck.getStyleClass().add(GAUGED_SC);

        Group group = new Group();
        ReadOnlyIntegerProperty percentage = isTicketsDeck ? observableGameState.getLeftTickets() :
                observableGameState.getLeftCards();
        Rectangle background = new Rectangle(GAUGE_RECTANGLE_WIDTH, GAUGE_RECTANGLE_HEIGHT);
        background.getStyleClass().add(BACKGROUND_SC);
        Rectangle foreground = new Rectangle(GAUGE_RECTANGLE_WIDTH, GAUGE_RECTANGLE_HEIGHT);
        foreground.widthProperty().bind(percentage.multiply(MAXIMAL_GAUGE_WIDTH).divide(100));
        foreground.getStyleClass().add(FOREGROUND_SC);
        group.getChildren().addAll(background, foreground);
        deck.setGraphic(group);

        deck.setOnMouseClicked(isTicketsDeck ? (e -> ticketsHandler.get().onDrawTickets()) :
                (e -> cardsHandler.get().onDrawCard(Constants.DECK_SLOT)));
        return deck;
    }

    /**
     * Method creates the graphical representation of cards.
     */
    private static void createRectangles(StackPane stackPane) {
        Rectangle outside = new Rectangle(OUTSIDE_CARD_WIDTH, OUTSIDE_CARD_HEIGHT);
        outside.getStyleClass().add(OUTSIDE_SC);

        Rectangle filledInside = new Rectangle(INSIDE_CARD_WIDTH, INSIDE_CARD_HEIGHT);
        filledInside.getStyleClass().addAll(INSIDE_SC, FILLED_SC);

        Rectangle trainImage = new Rectangle(TRAIN_CARD_WIDTH, TRAIN_CARD_HEIGHT);
        trainImage.getStyleClass().add(TRAIN_IMAGE_SC);

        stackPane.getChildren().addAll(outside, filledInside, trainImage);
    }
}