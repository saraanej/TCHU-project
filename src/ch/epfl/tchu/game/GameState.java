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

    /**
     * Static constructor of the initial state of a game of tCHu in which the ticket's deck contains the tickets given,
     * the cardState contains the cards of Constants.ALL_CARDS, without the ones above distributed to the players,
     * the decks are shuffled using the given random generator, also used to randomly choose the identity of the first player.
     *
     * @param tickets (SortedBag<Ticket>) : the tickets' deck contains these given tickets
     * @param rng (Random) : the given random generator
     * @return (GameState) the initial state of a Tchu's play
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        Deck<Card> deckCard = Deck.of(Constants.ALL_CARDS, rng);

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
        for (PlayerId playerId : PlayerId.values()) {
            playerState.put(playerId, PlayerState
                    .initial(deckCard.topCards(Constants.INITIAL_CARDS_COUNT)));
            deckCard = deckCard.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        }

        return new GameState(Deck.of(tickets, rng), CardState.of(deckCard),
                PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT)), playerState, null);
    }


    private final Deck<Ticket> ticketDeck;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

    /**
     * Private constructor of a GameState
     *
     * @param ticketDeck (Deck<Ticket>) : the ticket's deck
     * @param cardState (PublicCardState) : the public state of the cars and locomotives
     * @param currentPlayerId (PlayerId) : the current player's identity
     * @param playerState (Map<PlayerId, PlayerState>) : the public state of the players
     * @param lastPlayer (PlayerId) : the last player's identity. can be null
     *
     */
    private GameState(Deck<Ticket> ticketDeck, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer){
        super(ticketDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.ticketDeck = ticketDeck;
        this.cardState = cardState;
        this.playerState = Map.copyOf(playerState);
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
     * @param count (int) : the number of tickets
     * @return SortedBag<Ticket> the count tickets in the top of the deck
     * @throws IllegalArgumentException
     *                        if the number of tickets is negative or bigger than the deck's size
     */
    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument(count <= ticketDeck.size());
        return ticketDeck.topCards(count);
    }

    /**
     *
     * @param count (int) : the number of tickets
     * @return (GameState) this state without count tickets in the top of the deck
     * @throws IllegalArgumentException
     *                        if the number of tickets is negative or bigger than the deck's size
     */
    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument( count <= ticketDeck.size());
        return new GameState(ticketDeck.withoutTopCards(count), cardState,
                             currentPlayerId(), playerState, lastPlayer());
    }

    /**
     *
     * @return (GameState) : this state without the top deck card 
     * @throws IllegalArgumentException
     *                            if the card's deck is empty
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(ticketDeck, cardState.withoutTopDeckCard(),
                             currentPlayerId(), playerState, lastPlayer());
    }

    /**
     *
     * @param discardedCards (SortedBag<Card>) : the cards that will be added in the discard pile
     * @return (GameState) this state with the given cards added in the discard pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(discardedCards),
                             currentPlayerId(), playerState, lastPlayer());
    }

    /**
     *
     * @param rng (Random) : a random generator
     * @return (GameState) this state unless the cards' deck is empty,
     * in which case it will return the play's state with the cards' deck composed of the shuffled discard pile
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        return cardState.isDeckEmpty() ?
                new GameState(ticketDeck, cardState.withDeckRecreatedFromDiscards(rng),
                              currentPlayerId(), playerState, lastPlayer()) : this;

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
        return new GameState(ticketDeck, cardState, currentPlayerId(),
                             newPlayerState, lastPlayer());
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
        return new GameState(ticketDeck, cardState, currentPlayerId(), newPlayerState, lastPlayer())
                   .withoutTopTickets(drawnTickets.size());
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
        return new GameState(ticketDeck, cardState.withDrawnFaceUpCard(slot),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     *
     * @return (GmaeState) this state with the top cards' deck added to the current player's hand
     * @throws IllegalArgumentException
     *                        if it's not possible to draw a card
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.topDeckCard()));
        return new GameState(ticketDeck, cardState.withoutTopDeckCard(),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     *
     * @param route (Route) : the route the current player wants to take over
     * @param cards (SortedBag<Card>) : the cards used by the current player to get the route
     * @return (GameState) this state with the given route added to the current player's list of taken routes
                           and the cards used by the player added to the cardState's discard
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> newPlayerState = CopyPlayerState();
        newPlayerState.put(currentPlayerId(), currentPlayerState().withClaimedRoute(route, cards));
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(cards),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     *
     * @return (GameState) same GameState as this but with the currentPlayer changed to the next one,
                           furthermore, if the lastTurn should begin, the lastPlayer value is set to this currentPlayer
     */
    public GameState forNextTurn(){
        return lastTurnBegins() ? new GameState(ticketDeck, cardState, currentPlayerId().next(),
                                                playerState, currentPlayerId()) :
                                  new GameState(ticketDeck, cardState, currentPlayerId().next(),
                                                playerState, lastPlayer());
    }

    /**
     *
     * @return (boolean) true if the identity of the last player is null and the current player has two cars left or less.
     *                   false if not.
     */
    public boolean lastTurnBegins(){
        return lastPlayer() == null && currentPlayerState().carCount()<=2;
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


    /**
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

}
