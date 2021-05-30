package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * The instantiable ObservableGameState class of the ch.epfl.tchu.gui package
 * represents the observable state of a tCHu's play.
 * It combines the public part of the state of the game, i.e. the information contained
 * in an instance of PublicGameState, and the entire state of a given player, that is,
 * the information contained in an instance of PlayerState.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class ObservableGameState {

    //private static methods used to initialize the properties to its default values (null, 0 and false)
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUp = new ArrayList<>();
        for (int slot : Constants.FACE_UP_CARD_SLOTS) faceUp.add(new SimpleObjectProperty<>(null));
        return faceUp;
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutesIdentities() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        for (Route r : ChMap.routes()) {
            map.put(r, new SimpleObjectProperty<>(null));
        }
        return map;
    }

    private static Map<PlayerId, IntegerProperty> initMapIdInteger() {
        Map<PlayerId, IntegerProperty> map = new EnumMap<>(PlayerId.class);
        for (PlayerId p : PlayerId.values()) {
            map.put(p, new SimpleIntegerProperty(0));
        }
        return map;
    }

    private static Map<Card, IntegerProperty> createNumberCardType() {
        Map<Card, IntegerProperty> map = new EnumMap<>(Card.class);
        for (Card c : Card.values()) {
            map.put(c, new SimpleIntegerProperty(0));
        }
        return map;
    }

    private static Map<Route, BooleanProperty> createCanClaimRoute() {
        Map<Route, BooleanProperty> map = new HashMap<>();
        for (Route r : ChMap.routes()) {
            map.put(r, new SimpleBooleanProperty(false));
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
    private final Map<Route, ObjectProperty<PlayerId>> routeOwner;

    //properties containing public state of the players
    private final Map<PlayerId, IntegerProperty> ticketsCount;
    private final Map<PlayerId, IntegerProperty> cardsCount;
    private final Map<PlayerId, IntegerProperty> carCount;
    private final Map<PlayerId, IntegerProperty> claimPoints;

    //properties containing private state of the players
    private final ObservableList<Ticket> ticketList;
    private final Map<Card, IntegerProperty> numberCardType;
    private final Map<Route, BooleanProperty> canClaimRoute;

    /**
     * Public default constructor.
     *
     * @param playerId the id of the player's linked to this observableGameState.
     */
    public ObservableGameState(PlayerId playerId) {
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

    /**
     * Updates the gameState by modifying its proprieties's values according to the given PlayerState ad PublicGameState.
     *
     * @param gS          the updated PublicGameState.
     * @param playerState the updated complete PlayerState of this observableGameState's player.
     */
    public void setState(PublicGameState gS, PlayerState playerState) {
        gameState = gS;
        player = playerState;

        leftTickets.set((gameState.ticketsCount() * 100 / ChMap.tickets().size()));
        leftCards.set((gameState.cardState().deckSize() * 100 / Constants.TOTAL_CARDS_COUNT));
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = gameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        for (Route r : gameState.claimedRoutes()) {
            for (PlayerId id : PlayerId.ALL)
                if (gameState.playerState(id).routes().contains(r)) routeOwner.get(r).set(id);
        }

        for (PlayerId id : PlayerId.values()) {
            ticketsCount.get(id).set(gameState.playerState(id).ticketCount());
            cardsCount.get(id).set(gameState.playerState(id).cardCount());
            carCount.get(id).set(gameState.playerState(id).carCount());
            claimPoints.get(id).set(gameState.playerState(id).claimPoints());
        }

        ticketList.setAll(player.tickets().toList());

        for (Card c : Card.values())
            numberCardType.get(c).set(Collections.frequency(player.cards().toList(), c));

        for (Route r : ChMap.routes())
            canClaimRoute.get(r).set(playerId.equals(gameState.currentPlayerId())
                    && routeIsNotClaimed(r) && player.canClaimRoute(r));
    }

    /**
     * @return the property containing the percentage of left Tickets in the deck
     */
    public ReadOnlyIntegerProperty getLeftTickets() {
        return leftTickets;
    }

    /**
     * @return the property containing the percentage of left Cards in the deck
     */
    public ReadOnlyIntegerProperty getLeftCards() {
        return leftCards;
    }

    /**
     * @param c the type of the card.
     * @return the number of cards of type c that the player has in hand.
     */
    public ReadOnlyIntegerProperty numberCardsOfType(Card c) {
        return numberCardType.get(c);
    }

    /**
     * @param id the id of the player of interest.
     * @return the number of tickets owned by the player id.
     */
    public ReadOnlyIntegerProperty playerTicketCount(PlayerId id) {
        return ticketsCount.get(id);
    }

    /**
     * @param id the id of the player of interest.
     * @return the number of cards owned by the player id.
     */
    public ReadOnlyIntegerProperty playerCardCount(PlayerId id) {
        return cardsCount.get(id);
    }

    /**
     * @param id the id of the player of interest.
     * @return the number of wagon cars owned by the player id.
     */
    public ReadOnlyIntegerProperty playerCarCount(PlayerId id) {
        return carCount.get(id);
    }

    /**
     * @param id the id of the player of interest.
     * @return the claim points owned by the player id.
     */
    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id) {
        return claimPoints.get(id);
    }

    /**
     * @param slot the slot of the faceUpCard to get (from 0 to 4).
     * @return the face up card at the given slot.
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * @param route the Route of interest.
     * @return the id of owner of the given route, null if it belongs to no player.
     */
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routeOwner.get(route);
    }

    /**
     * @param route the Route of interest.
     * @return if the player linked to this state can claim the given route.
     */
    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return canClaimRoute.get(route);
    }

    /**
     * @return if the player linked to this state can draw tickets from the ticket's deck.
     */
    public boolean canDrawTickets() {
        return gameState.canDrawTickets();
    }

    /**
     * @return if the player linked to this state can draw cards from the card's deck.
     */
    public boolean canDrawCards() {
        return gameState.canDrawCards();
    }

    /**
     * @param route the Route of interest.
     * @return the possible cards this' player can use to claim the given route.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return player.possibleClaimCards(route);
    }

    /**
     * @return the list of tickets owned by this' player.
     */
    public ObservableList<Ticket> ticketList() {
        return FXCollections.unmodifiableObservableList(ticketList);
    }


    /**
     * @param r the Route of interest.
     * @return if the given route is already claimed or not.
     */
    private boolean routeIsNotClaimed(Route r) {
//        CASE WITH ONLY 2 PLAYERS
//        List<List<Station>> stations = new ArrayList<>();
//        for (Route route : gameState.claimedRoutes()) stations.add(route.stations());
//        return !stations.contains(r.stations());
        //case with more than 2 players
        return !gameState.claimedRoutes().contains(r);
    }
}
