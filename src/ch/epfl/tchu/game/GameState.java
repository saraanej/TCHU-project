package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * Modelizes the state of a Tchu's play
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public final class GameState extends PublicGameState{

    private final Deck<Ticket> gameDeck;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

    /**
     * private constructor
     *
     * @param gameDeck (Deck) : the ticket's deck
     * @param cardState (PublicCardState) : the public state of the cars and locomotives
     * @param currentPlayerId (PlayerId) : the current player's identity
     * @param playerState (Map<PlayerId, PlayerState>) : the public state of the players
     * @param lastPlayer (PlayerId) : the last player's identity. can be null
     *
     */
    private GameState(Deck gameDeck, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState,  PlayerId lastPlayer){
       super(gameDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.gameDeck = gameDeck;
        this.cardState = cardState;
        this.playerState = Map.copyOf(playerState);
    }

    /**
     * static constructor method
     *
     * @param tickets (SortedBag<Ticket>) : the tickets' deck contains these given tickets
     * @param rng (Random) : the given random generator
     * @return (GameState) the initial state of a Tchu's play
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        Deck<Card> deckCard = Deck.of(Constants.ALL_CARDS, rng);
        SortedBag<Card> initialCards1 = deckCard.topCards(4);
        SortedBag<Card> initialCards2 = deckCard.topCards(4);
        deckCard = deckCard.withoutTopCards(8);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
        playerState.put(PlayerId.PLAYER_1, PlayerState.initial(initialCards1));
        playerState.put(PlayerId.PLAYER_2, PlayerState.initial(initialCards2));
        return new GameState(Deck.of(tickets,rng),
                         CardState.of(deckCard),
                         PlayerId.ALL.get(rng.nextInt(2)),
                         playerState,
                         null);
    }

    /**
     *
     * @param playerId (PlayerId) : a game player
     * @return (PlayerState) the complete state of the given player
     */
    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     *
     * @return (PlayerState) the complete state of the current player
     */
    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());
    }

    /**
     *
     * @param count (int) : the number of tickets
     * @return SortedBag<Ticket> the count tickets in the top of the deck
     * @throws IllegalArgumentException
     *                        if the number of tickets is negative or bigger than the deck's size
     */
    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= gameDeck.size());
        return gameDeck.topCards(count);
    }

    /**
     *
     * @param count (int) : the number of tickets
     * @return (GameState) this state without count tickets in the top of the deck
     * @throws IllegalArgumentException
     *                        if the number of tickets is negative or bigger than the deck's size
     */
    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= gameDeck.size());
        return new GameState(gameDeck.withoutTopCards(count),
                             cardState,
                             currentPlayerId(),
                             playerState,
                             lastPlayer());
    }

    /**
     *
     * @return (Card) the card in the top of the deck
     * @throws IllegalArgumentException
     *                       if the card's deck is empty
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     *
     * @return (GameState) : this state without the top deck card
     * @throws IllegalArgumentException
     *                            if the card's deck is empty
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(gameDeck,
                             cardState.withoutTopDeckCard(),
                             currentPlayerId(),
                             playerState,
                             lastPlayer());
    }

    /**
     *
     * @param discardedCards (SortedBag<Card>) : the cards that will be added in the discard pile
     * @return (GameState) this state with the given cards added in the discrad pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(gameDeck,
                             cardState.withMoreDiscardedCards(discardedCards),
                             currentPlayerId(),
                             playerState,
                             lastPlayer());
    }

    /**
     *
     * @param rng (Random) : a random generator
     * @return (GameState) this state unless the cards' deck is empty,
     * in which case it will return the play's state with the cards' deck composed of the shuffled discard pile
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        return cardState.isDeckEmpty() ? new GameState(gameDeck,
                                                       cardState.withDeckRecreatedFromDiscards(rng),
                                                       currentPlayerId(),
                                                       playerState,
                                                       lastPlayer()) :
                                         this;

    }

    /**
     *
     * @param playerId (PlayerId) : the given player
     * @param chosenTickets (SortedBag<Ticket>) : the given tickets
     * @return (GameState) this state with the given tickets added to the given player's hand
     * @throws IllegalArgumentException
     *                        if the given player has at least one ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(playerState(playerId).tickets().size() == 0);
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(playerId, currentPlayerState().withAddedTickets(chosenTickets));
        return new GameState(gameDeck,
                             cardState,
                             currentPlayerId(),
                             newPlayerState,
                             lastPlayer());
    }

    /**
     *
     * @param drawnTickets (SortedBag<Ticket>) : the drawn tickets
     * @param chosenTickets (SortedBag<Ticket>) : the tickets chosen by the player
     * @return (GameState) this state with the chosenTickets added to the current player's hand
     * @throws IllegalArgumentException
     *                        if the drawnTickets don't contain the chosenTickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedTickets(chosenTickets));
        return new GameState(gameDeck,
                cardState,
                currentPlayerId(),
                newPlayerState,
                lastPlayer()).
                withoutTopTickets(drawnTickets.size());
    }

    /**
     *
     * @param slot (int) : index of the visible card
     * @return (GameState) this state with the visible card in the given index added to the current player's hand
     *                     and a drawn card from the card's deck will replace it
     * @throws IllegalArgumentException
     *                        if it's not possible to draw a card
     */
    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.faceUpCard(slot)));
        return new GameState(gameDeck,
                cardState.withDrawnFaceUpCard(slot),
                currentPlayerId(),
                newPlayerState,
                lastPlayer());
    }

    /**
     *
     * @return (GmaeState) this state witht the top cards' deck added to the current player's hand
     * @throws IllegalArgumentException
     *                        if it's not possible to draw a card
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.topDeckCard()));
        return new GameState(gameDeck,
                cardState.withoutTopDeckCard(),
                currentPlayerId(),
                newPlayerState,
                lastPlayer());
    }

    /**
     *
     * @param route (Route) : the route the current player wants to take over
     * @param cards (SortedBag<Card>) : the cards used by the current player to get the route
     * @return (GameState) this state with the given route added to the current player's list of taken routes
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withClaimedRoute(route, cards));
        return new GameState(gameDeck,
                             cardState.withMoreDiscardedCards(cards),
                             currentPlayerId(),
                             newPlayerState,
                             lastPlayer());
    }

    /**
     *
     * @return (boolean) true if the identity of the last player is null and the current player has two cars left or less. false if not.
     */
    public boolean lastTurnBegins(){
        return lastPlayer() == null && currentPlayerState().carCount()<=2;
    }

    /**
     *
     * @return (GameState) same GameState as this but with the currentPlayer changed to the next one,
                           furthermore, if the lastTurn should begin, the lastPlayer value is set to this currentPlayer
     */
    public GameState forNextTurn(){
        return lastTurnBegins() ? new GameState(gameDeck,
                                                cardState,
                                                currentPlayerId().next(),
                                                playerState,
                                                currentPlayerId()) :
                                  new GameState(gameDeck,
                                                cardState,
                                                currentPlayerId().next(),
                                                playerState,
                                                lastPlayer());

    }

    /**
     * copies the playerState immutable map into a mutable one
     * @return Map<PlayerId, PlayerState> a mutable version of the playerState
     */
    private Map<PlayerId, PlayerState> CopyPlayerState() {
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(PlayerId.class);
        newPlayerState.putAll(playerState);
        return newPlayerState;
    }

}
