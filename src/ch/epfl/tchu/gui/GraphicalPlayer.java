package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import javafx.beans.binding.Bindings;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Constants.DISCARDABLE_TICKETS_COUNT;
import static ch.epfl.tchu.gui.GuiConstants.CHOOSER_SS;
import static javafx.application.Platform.isFxApplicationThread;

/**
 * The instantiable GraphicalPlayer class from the ch.epfl.tchu.gui package
 * represents the graphical interface of a tCHu player.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class GraphicalPlayer {

    private static final int MIN_CLAIM_CARDS_CHOICE = 1;
    private static final int MIN_CARDS_CHOICE_ADDITIONAL = 0;
    private static final int INFO_MESSAGE_COUNT = 5;

    private final Stage primaryStage;

    private final ObservableGameState gameState;
    private final ObservableList<Text> infoList;

    private final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTickets;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCard;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute;

    /**
     * Constructs the graphical interface of the player.
     * @param id the id of the player's linked to this graphicalPlayer.
     * @param playerNames the player's names of the Tchu's play.
     */
    public GraphicalPlayer(PlayerId id, Map<PlayerId,String> playerNames){
        assert isFxApplicationThread();
        gameState = new ObservableGameState(id);
        drawTickets = new SimpleObjectProperty<>(null);
        drawCard = new SimpleObjectProperty<>(null);
        claimRoute = new SimpleObjectProperty<>(null);
        infoList = FXCollections.observableArrayList();

        Node mapView = MapViewCreator
                .createMapView(gameState, claimRoute, this::chooseClaimCards);
        Node cardsView = DecksViewCreator
                .createCardsView(gameState, drawTickets, drawCard);
        Node handView = DecksViewCreator
                .createHandView(gameState);
        Node infoView = InfoViewCreator
                .createInfoView(id,playerNames,gameState, infoList);

        BorderPane mainPane =
                new BorderPane(mapView, null, cardsView, handView, infoView);

        primaryStage = new Stage();
        primaryStage.setTitle(String.format("tChu - %s",playerNames.get(id)));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }

    /**
     * Calls the method setState of the ObservableGameState 
     * @see ObservableGameState#setState(PublicGameState, PlayerState) 
     * @param gS the updated PublicGameState.
     * @param pS the updated complete PlayerState of this observableGameState's player.
     */
    public void setState(PublicGameState gS, PlayerState pS){
        assert isFxApplicationThread();
        gameState.setState(gS,pS);
    }

    /**
     * Adds the message to the bottom of the game's progress' information. The section can only contain 5 messages at once.
     * @param message the message to add to the view.
     */
    public void receiveInfo(String message){
        assert isFxApplicationThread();
        if (infoList.size() == INFO_MESSAGE_COUNT) infoList.remove(0);
        infoList.add(new Text(message));
    }

    /**
     * Allows the player to do one of his three actions by abling and disabling its handlers.
     * @param ticketHandler the handler for the action : draw Tickets.
     * @param cardHandler the handler for the action : draw Cards.
     * @param routeHandler the handler for the action : claim Route.
     */
    public void startTurn(ActionHandlers.DrawTicketsHandler ticketHandler, ActionHandlers.DrawCardHandler cardHandler,
                          ActionHandlers.ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();
        claimRoute.set((r,c) -> {
            drawCard.set(null);
            drawTickets.set(null);
            routeHandler.onClaimRoute(r,c);
            claimRoute.set(null);
        });

        if (gameState.canDrawTickets())
            drawTickets.set(() -> {
                drawCard.set(null);
                claimRoute.set(null);
                ticketHandler.onDrawTickets();
                drawTickets.set(null);
            });

        if (gameState.canDrawCards())
            drawCard.set(i -> {
                drawTickets.set(null);
                claimRoute.set(null);
                cardHandler.onDrawCard(i);
                drawCard.set(null);
            });
    }

    /**
     * Allows the player to choose a card by calling the DrawCardHandler once his choice is made.
     * This method is intended to be called when the player has already drawn a first card and must now draw the second.
     * @param cardHandler the draw Card's handler to call with the choice of the player.
     */
    public void drawCard(ActionHandlers.DrawCardHandler cardHandler){
        assert isFxApplicationThread();
        drawCard.set(i -> {
            drawTickets.set(null);
            claimRoute.set(null);
            cardHandler.onDrawCard(i);
            drawCard.set(null);
        });
    }

    /**
     * Opens a window allowing the player to make his choice;
     * Once it is confirmed, the choice handler is called with this choice in argument.
     * @param options the Tickets the player has to choose from.
     * @param chooseTickets the choose Tickets' handler to call with the player's choice.
     */
    public void chooseTickets(SortedBag<Ticket> options, ActionHandlers.ChooseTicketsHandler chooseTickets){
        assert isFxApplicationThread();
        ListView<Ticket> listView = new ListView<>(FXCollections.observableArrayList(options.toList()));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        int minTicketsChoice = options.size() - DISCARDABLE_TICKETS_COUNT;

        createDialogStage(minTicketsChoice,StringsFr.TICKETS_CHOICE,
                String.format(StringsFr.CHOOSE_TICKETS, minTicketsChoice, StringsFr.plural(minTicketsChoice)),
                listView,
                e -> chooseTickets.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems())));
    }

    /**
     * Opens a window allowing the player to make his choice;
     * Once this has been done and confirmed, the choice handler is called with the player's choice as an argument;
     * This method is only intended to be passed as an argument to createMapView as a value of type CardChooser.
     * @param possibleClaimCards all the possible cards the player can use to claim the desired route.
     * @param cardsHandler the choose cards' handler to call with the choice of the player.
     */
    public void chooseClaimCards(List<SortedBag<Card>> possibleClaimCards,
                                 ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableArrayList(possibleClaimCards));
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        createDialogStage(MIN_CLAIM_CARDS_CHOICE,StringsFr.CARDS_CHOICE,StringsFr.CHOOSE_CARDS,listView,
                e -> cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem())
        );
    }

    /**
     * Opens a window allowing the player to make his choice;
     * Once this has been done and confirmed, the choice handler is called with the player's choice as an argument.
     * @param possibleAdditional the additional cards that can be used to seize a tunnel.
     * @param cardsHandler the choose cards' handler to call with the choice of the player.
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditional,
                                      ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableArrayList(possibleAdditional));
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        SortedBag<Card> selectedCards = listView.getSelectionModel().getSelectedItem();
        createDialogStage(MIN_CARDS_CHOICE_ADDITIONAL,StringsFr.CARDS_CHOICE,StringsFr.CHOOSE_ADDITIONAL_CARDS, listView,
                e -> cardsHandler.onChooseCards(selectedCards == null ? SortedBag.of() : selectedCards));
    }

    /**
     * Creates the dialog window to choose the cards or the tickets.
     * @param minItems the minimum choice allowed.
     * @param title the title of the window.
     * @param intro the introduction text of the window.
     * @param listView the list of choices.
     * @param handler the handler for the action after pressing the choice button.
     * @param <E> the type of the elements to choose from.
     */
    private <E> void  createDialogStage(int minItems, String title, String intro, ListView<E> listView, EventHandler<ActionEvent> handler){
        Stage dialogStage = new Stage(StageStyle.UTILITY);

        Text textIntro = new Text(intro);
        TextFlow textFlow = new TextFlow(textIntro);

        Button chooseButton = new Button(StringsFr.CHOOSE);

        IntegerBinding selected = Bindings.size(listView.getSelectionModel().getSelectedItems());
        chooseButton.disableProperty().bind(Bindings.lessThan(selected,minItems));
        chooseButton.setOnAction(e -> {
            dialogStage.hide();
            handler.handle(e);
        });

        VBox vBox = new VBox(textFlow, listView, chooseButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(CHOOSER_SS);


        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle(title);
        dialogStage.setOnCloseRequest(Event::consume);
        dialogStage.setScene(scene);
        dialogStage.show();
    }


    /**
     * @see StringConverter
     */
    public static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        /**
         * @see StringConverter#toString(Object)
         */
        @Override
        public String toString(SortedBag<Card> object) {
            return Info.textCardList(object); 
        }

        /**
         * This method should never be called.
         */
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }
}
