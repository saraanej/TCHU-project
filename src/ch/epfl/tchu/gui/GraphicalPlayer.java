package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private final ObservableGameState gameState;
    private final ObjectProperty <ActionHandlers.DrawTicketsHandler> drawTicket;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCard;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute;

    public GraphicalPlayer(PlayerId id, Map<PlayerId,String> playerNames){
        assert isFxApplicationThread();
        gameState = new ObservableGameState(id);
        drawTicket = new SimpleObjectProperty<>(null);
        drawCard = new SimpleObjectProperty<>(null);
        claimRoute = new SimpleObjectProperty<>(null);
    }

    public void setState(PublicGameState gS, PlayerState pS){
        assert isFxApplicationThread();
        gameState.setState(gS,pS);
    }

    public void receiveInfo(String message){
        assert isFxApplicationThread();
        //ajoute message au bas des informations sur le déroulement de la partie,
        // qui sont présentées dans la partie inférieure de la vue des informations
        //contient 5 messages max
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler ticketHandler, ActionHandlers.DrawCardHandler cardHandler,
                          ActionHandlers.ClaimRouteHandler routeHandler) {
        assert isFxApplicationThread();
        //permet au joueur d'effectuer une des 3 actions
        claimRoute.set(routeHandler);
        if (gameState.canDrawTickets()) {
            drawTicket.set(ticketHandler);
            drawCard.set(null);
            claimRoute.set(null);
        }
        if (gameState.canDrawCards()) {
            drawCard.set(cardHandler);
            drawTicket.set(null);
            claimRoute.set(null);
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
        drawCard.set(cardHandler);
        drawTicket.set(null);
        claimRoute.set(null);
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








}
