package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private static final int MIN_CARDS_CHOICE = 1;




    private final Stage primaryStage;
    private Stage dialogStage;

    private final PlayerId id;
    private final Map<PlayerId,String> playerNames;
    private final ObservableGameState gameState;
    private final ObservableList<Text> infoList;

    private final ObjectProperty <ActionHandlers.DrawTicketsHandler> drawTickets;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCard;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute;

    public GraphicalPlayer(PlayerId id, Map<PlayerId,String> playerNames){
        assert isFxApplicationThread();
        gameState = new ObservableGameState(id);
        drawTickets = new SimpleObjectProperty<>(null);
        drawCard = new SimpleObjectProperty<>(null);
        claimRoute = new SimpleObjectProperty<>(null);
        infoList = FXCollections.observableArrayList();
        this.id = id;
        this.playerNames = Map.copyOf(playerNames);

        Node mapView = MapViewCreator
                .createMapView(gameState, claimRoute, (cards,handler) -> chooseClaimCards(cards,handler));
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

    public void setState(PublicGameState gS, PlayerState pS){
        assert isFxApplicationThread();
        gameState.setState(gS,pS);
    }

    public void receiveInfo(String message){
        assert isFxApplicationThread();
        if (infoList.size() == 5) infoList.remove(0);
        infoList.add(new Text(message));
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler ticketHandler, ActionHandlers.DrawCardHandler cardHandler,
                          ActionHandlers.ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();
        claimRoute.set((r,c) -> {
            routeHandler.onClaimRoute(r,c);
            drawCard.set(null);
            drawTickets.set(null);
            claimRoute.set(null); //maybe to change
        });
        if (gameState.canDrawTickets()) {
            drawTickets.set(() -> {
                ticketHandler.onDrawTickets();
                drawCard.set(null);
                claimRoute.set(null);
                drawTickets.set(null);
            });
        }
        if (gameState.canDrawCards()) {
            drawCard.set(i -> {
                cardHandler.onDrawCard(i);
                drawTickets.set(null);
                claimRoute.set(null);
                drawCard.set(null);
            });
        }
    }

    public void drawCard(ActionHandlers.DrawCardHandler cardHandler){
        assert isFxApplicationThread();
        drawCard.set(i -> {
            cardHandler.onDrawCard(i);
            drawTickets.set(null);
            claimRoute.set(null);
            drawCard.set(null);
        });
    }

    public void chooseTickets(SortedBag<Ticket> options, ActionHandlers.ChooseTicketsHandler chooseTickets){
        assert isFxApplicationThread();
        ListView<Ticket> listView = new ListView<>(FXCollections.observableArrayList(options.toList()));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final int MIN_TICKET_CHOICE = options.size() - 2;

        createDialogStage(MIN_TICKET_CHOICE,StringsFr.TICKETS_CHOICE,
                String.format(StringsFr.CHOOSE_TICKETS, MIN_TICKET_CHOICE, StringsFr.plural(MIN_TICKET_CHOICE)),
                listView, e -> { dialogStage.hide();
                chooseTickets.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()));
        });
    }

    public void chooseClaimCards(List<SortedBag<Card>> possibleClaimCards,
                                 ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableArrayList(possibleClaimCards));;
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        createDialogStage(MIN_CARDS_CHOICE,StringsFr.CARDS_CHOICE,StringsFr.CHOOSE_CARDS,listView, e -> {
            dialogStage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItems().get(0));
        });
    }

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditional,
                                      ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableArrayList(possibleAdditional));;
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        createDialogStage(MIN_CARDS_CHOICE,StringsFr.CARDS_CHOICE,StringsFr.CHOOSE_ADDITIONAL_CARDS, listView, e -> {
            dialogStage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItems().get(0));
        });
    }


    private <E> void  createDialogStage(int minItems, String title, String intro, ListView<E> listView, EventHandler<ActionEvent> handler){
        Text textIntro = new Text(intro);
        TextFlow textFlow = new TextFlow(textIntro);

        Button chooseButton = new Button(StringsFr.CHOOSE);

        IntegerBinding selected = Bindings.size(listView.getSelectionModel().getSelectedItems());
        chooseButton.disableProperty().bind(Bindings.lessThan(selected,minItems));
        chooseButton.setOnAction(handler);

        VBox vBox = new VBox(textFlow, listView, chooseButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");

        dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle(title);
        dialogStage.setOnCloseRequest(e -> e.consume());
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    public static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
        @Override
        public String toString(SortedBag<Card> object) {
            return Info.textCardList(object);
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }
}
