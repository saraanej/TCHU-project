package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
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
    private final Map<Route,ObjectProperty<PlayerId>> routeOwner;

    //properties containing public state of the players
    private final Map<PlayerId,IntegerProperty> ticketsCount;
    private final Map<PlayerId,IntegerProperty> cardsCount;
    private final Map<PlayerId,IntegerProperty> carCount;
    private final Map<PlayerId,IntegerProperty> claimPoints;

    //properties containing private state of the players
    private final ObservableList<Ticket> ticketList;
    private final Map<Card,IntegerProperty> numberCardType;
    private final Map<Route,BooleanProperty> canClaimRoute;

    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
        leftTickets = new SimpleIntegerProperty(0);
        leftCards = new SimpleIntegerProperty(0);
        faceUpCards = createFaceUpCards();
        routeOwner = createRoutesIdentities();
        ticketsCount = initMapIdInteger();
        cardsCount = initMapIdInteger();
        carCount = initMapIdInteger();
        claimPoints = initMapIdInteger();
        ticketList = FXCollections.observableArrayList();
        numberCardType = createNumberCardType();
        canClaimRoute = createCanClaimRoute();
    }

    public void setState(PublicGameState gS, PlayerState playerState){
        gameState = gS;
        player= playerState;

        leftTickets.set((gameState.ticketsCount()*100/ChMap.tickets().size()));
        leftCards.set((gameState.cardState().deckSize()*100/Constants.TOTAL_CARDS_COUNT));
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = gameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        for (Route r : gameState.claimedRoutes()){
            routeOwner.get(r).set(player.routes().contains(r) ? playerId : playerId.next());
        }

        for(PlayerId id : PlayerId.values()){
            ticketsCount.get(id).set(gameState.playerState(id).ticketCount());
            cardsCount.get(id).set(gameState.playerState(id).cardCount());
            carCount.get(id).set(gameState.playerState(id).carCount());
            claimPoints.get(id).set(gameState.playerState(id).claimPoints());
        }

        ticketList.setAll(player.tickets().toList());

        for (Card c : Card.values()) {
            numberCardType.get(c).set(Collections.frequency(player.cards().toList(),c));
        }
        for (Route r : ChMap.routes()){
            if (playerId.equals(gameState.currentPlayerId())
                    && routeIsNotClaimed(r) && player.canClaimRoute(r))
            canClaimRoute.get(r).set(true);
        }
    }

    public ReadOnlyIntegerProperty getLeftTickets(){
        return leftTickets;
    }
    public ReadOnlyIntegerProperty getLeftCards(){
        return leftCards;
    }
    public ReadOnlyIntegerProperty numberCardsOfType(Card c){
        return numberCardType.get(c);
    }
    public ReadOnlyIntegerProperty playerTicketCount(PlayerId id){
        return ticketsCount.get(id);
    }
    public ReadOnlyIntegerProperty playerCardCount(PlayerId id){
        return cardsCount.get(id);
    }
    public ReadOnlyIntegerProperty playerCarCount(PlayerId id){
        return carCount.get(id);
    }
    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id){
        return claimPoints.get(id);
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot){
        return faceUpCards.get(slot);
    }
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route){
        return routeOwner.get(route);
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
    public ObservableList<Ticket> ticketList(){
        return FXCollections.unmodifiableObservableList(ticketList);
    }


    private boolean routeIsNotClaimed(Route r){
        List<List<Station>> stations = new ArrayList<>();
        for (Route route : gameState.claimedRoutes()) stations.add(route.stations());
        System.out.println(!stations.contains(r.stations()));
        return !stations.contains(r.stations());
    }
}
