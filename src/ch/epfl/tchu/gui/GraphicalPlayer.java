package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Map;

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private final Stage primaryStage;

    private final PlayerId id;
    private final Map<PlayerId,String> playerNames;
    private final ObservableGameState gameState;
    private final ObservableList<Text> infosList;

    private final ObjectProperty <ActionHandlers.DrawTicketsHandler> drawTickets;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCard;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute;

    public GraphicalPlayer(PlayerId id, Map<PlayerId,String> playerNames){
        assert isFxApplicationThread();
        gameState = new ObservableGameState(id);
        drawTickets = new SimpleObjectProperty<>(null);
        drawCard = new SimpleObjectProperty<>(null);
        claimRoute = new SimpleObjectProperty<>(null);
        infosList = FXCollections.observableArrayList();
        this.id = id;
        this.playerNames = Map.copyOf(playerNames);

        Node mapView = MapViewCreator
                .createMapView(gameState, claimRoute, (cards,handler) -> chooseClaimCards(cards,handler));
        Node cardsView = DecksViewCreator
                .createCardsView(gameState, drawTickets, drawCard);
        Node handView = DecksViewCreator
                .createHandView(gameState);
        Node infoView = InfoViewCreator
                .createInfoView(id,playerNames,gameState,infosList);

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

    public void receiveInfo(String message){//todo really don t see how for now check after
        assert isFxApplicationThread();
        //ajoute message au bas des informations sur le déroulement de la partie,
        // qui sont présentées dans la partie inférieure de la vue des informations
        //contient 5 messages max
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler ticketHandler, ActionHandlers.DrawCardHandler cardHandler,
                          ActionHandlers.ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();
        //permet au joueur d'effectuer une des 3 actions
        claimRoute.set((r,c) -> {
            routeHandler.onClaimRoute(r,c);
            drawCard.set(null);
            drawTickets.set(null);
        });
        if (gameState.canDrawTickets()) {
            drawTickets.set(() -> {
                ticketHandler.onDrawTickets();
                drawCard.set(null);
                claimRoute.set(null);
            });
        }
        if (gameState.canDrawCards()) {
            drawCard.set(i -> {
                cardHandler.onDrawCard(i);
                drawTickets.set(null);
                claimRoute.set(null);
            });
        }
    }

    public void chooseTickets(SortedBag<Ticket> options, ActionHandlers.ChooseTicketsHandler chooseTickets){
        assert isFxApplicationThread();
        //ouvre boite de dialogue pour le choix puis app handler avec le choix du client
        SortedBag<Ticket> chosenTickets = null;
        chooseTickets.onChooseTickets(chosenTickets);
    }

    public void drawCard(ActionHandlers.DrawCardHandler cardHandler){
        assert isFxApplicationThread();
        // methode a appeler apres le premier tirage de carte
        drawCard.set(i -> {
            cardHandler.onDrawCard(i);
            drawTickets.set(null);
            claimRoute.set(null);
        });
    }

    public void chooseClaimCards(List<SortedBag<Card>> possibleCLaimCard,
                                 ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        //methode destinee à être passee en arg a createMapView pour CardChooser
        //ouvre une fenêtre similaire à celle de la figure 5 permettant au joueur de faire son choix
        SortedBag<Card> claimCards = null;
        cardsHandler.onChooseCards(claimCards);
    }

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditional,
                                      ActionHandlers.ChooseCardsHandler cardsHandler){
        assert isFxApplicationThread();
        //ouvre une fenêtre similaire à celle de la figure 6 permettant au joueur de faire son choix
        SortedBag<Card> additionalCards = null;
        cardsHandler.onChooseCards(additionalCards);
    }

    private void createDialogStage(String title, String intro){
        Text textIntro = new Text(intro);
        TextFlow textFlow = new TextFlow(textIntro);

        Button chooseButton = new Button();



        VBox vBox = new VBox(textFlow, chooseButton);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality( Modality.WINDOW_MODAL);
        stage.setTitle(title);
    }
}
