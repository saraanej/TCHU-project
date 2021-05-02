package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.*;

public class ObservableGameState {

    private static List<ObjectProperty<Card>> createFaceUpCards(){
        List<ObjectProperty<Card>> faceUp = new ArrayList<>();
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            faceUp.add(new SimpleObjectProperty<>(null));
        }
        return faceUp;
    }
    private static Map<Route,ObjectProperty<PlayerId>> createRoutesIdentities(){
        Map<Route,ObjectProperty<PlayerId>> map = new HashMap<>();
        for (Route r : ChMap.routes()) {
            map.put(r,new SimpleObjectProperty<>(null));
        }
        return map;
    }
    private static Map<PlayerId,IntegerProperty> initMapIdInteger(){
        Map<PlayerId,IntegerProperty> map = new EnumMap<>(PlayerId.class);
        for (PlayerId p: PlayerId.values()) {
            map.put(p,new SimpleIntegerProperty(0));
        }
        return map;
    }
    private static Map<Card,IntegerProperty> createNumberCardType() {
        Map<Card,IntegerProperty> map = new EnumMap<>(Card.class);
        for (Card c: Card.values()) {
            map.put(c,new SimpleIntegerProperty(0));
        }
        return map;
    }
    private static Map<Route,BooleanProperty> createCanClaimRoute() {
        Map<Route,BooleanProperty> map = new HashMap<>();
        for (Route r : ChMap.routes()) {
            map.put(r,new SimpleBooleanProperty(false));
        }
        return map;
    }

    private PlayerState player;
    private PublicGameState gameState;
    private final PlayerId playerId;

    //properties containing public state of the game
    private final IntegerProperty leftTickets;
    private final IntegerProperty leftCards;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route,ObjectProperty<PlayerId>> routesIdentities;

    //properties containing public state of the players
    private final Map<PlayerId,IntegerProperty> ticketsCount;
    private final Map<PlayerId,IntegerProperty> cardsCount;
    private final Map<PlayerId,IntegerProperty> wagonCount;
    private final Map<PlayerId,IntegerProperty> claimPoints;

    //properties containing private state of the players
    private final ObjectProperty<ObservableList<Ticket>> ticketList;
    private final Map<Card,IntegerProperty> numberCardType;
    private final Map<Route,BooleanProperty> canClaimRoute;

    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
        leftTickets = new SimpleIntegerProperty(0);
        leftCards = new SimpleIntegerProperty(0);
        faceUpCards = createFaceUpCards();
        routesIdentities = createRoutesIdentities();
        ticketsCount = initMapIdInteger();
        cardsCount = initMapIdInteger();
        wagonCount = initMapIdInteger();
        claimPoints = initMapIdInteger();
        ticketList = new SimpleObjectProperty<>(null); //ask assistants of this the behavior expected at initialisation (null and not newObservableArray..)
        numberCardType = createNumberCardType();
        canClaimRoute = createCanClaimRoute();
    }

    public void setState(){

    }

    public ReadOnlyIntegerProperty getLeftTickets(){
        return leftTickets;
    }
    public ReadOnlyIntegerProperty getLeftCards(){
        return leftCards;
    }
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot){
        return faceUpCards.get(slot);
    }
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route){
        return routesIdentities.get(route);
    }

    public ReadOnlyIntegerProperty playerTicketCount(PlayerId id){
        return ticketsCount.get(id);
    }
    public ReadOnlyIntegerProperty playerCardCount(PlayerId id){
        return cardsCount.get(id);
    }
    public ReadOnlyIntegerProperty playerWagonCount(PlayerId id){
        return wagonCount.get(id);
    }
    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id){
        return claimPoints.get(id);
    }

    public ReadOnlyObjectProperty<ObservableList<Ticket>> ticketList(){
        return ticketList;
    }
    public ReadOnlyIntegerProperty numberCardsOfType(Card c){
        return numberCardType.get(c);
    }
    public ReadOnlyBooleanProperty canClaimRoute(Route route){
        return canClaimRoute.get(route);
    }

    public boolean canDrawTickets(){
        return gameState.canDrawTickets();
    }

    public boolean canDrawCards(){
        return gameState.canDrawCards();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return player.possibleClaimCards(route);
    }


















}
