package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * The GameState class, from the ch.epfl.tchu.game package, public, final, and immutable.
 * Modelizes the state of a Tchu's play
 * It inherits from PublicGameState and does not offer a public constructor,
 * but a public and static construction method.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class GameState extends PublicGameState{

    private final static int CAR_COUNT_FOR_LAST_TURN = 2;

    private final Deck<Ticket> ticketDeck;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

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
        Preconditions.checkArgument(tickets != null && !tickets.isEmpty());
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

    /**
     * Private constructor of a GameState.
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
        Preconditions.checkArgument(cardState != null);
        this.ticketDeck = ticketDeck;
        this.cardState = cardState;
        this.playerState = Map.copyOf(playerState);
    }


    /**
     * Returns the top deck card of this gameState's cardState.
     *
     * @return (Card) the drawn card on the top of the deck
     * @throws IllegalArgumentException
     *                       if the card's deck is empty
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     * Returns the count top Tickets from the top of the ticket's deck.
     *
     * @param count (int) : the number of tickets to draw from this ticket's deck.
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
     * Returns an identical state to the receiver (this), but without the count top Tickets of the ticket's deck.
     *
     * @param count (int) : the number of drawn tickets to remove from this ticket's deck
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
     * Returns an identical state to the receiver (this), but without the top deck card of the this gameState's cardState.
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
     * Returns an identical state to the receiver (this) but with the given cards added to the discard pile.
     *
     * @param discardedCards (SortedBag<Card>) : the cards that will be added to the discard pile.
     * @return (GameState) this state with the discardedCards added to the discard pile.
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(discardedCards),
                             currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver unless the deck of cards is empty,
     * in which case it is recreated from the discard pile, shuffled using the given random generator.
     *
     * @param rng (Random) : a random generator used to shuffle the new deck.
     * @return (GameState) this state unless the cards' deck is empty,
     *                     in which case it will return the play's state with the cards' deck
     *                     composed of the shuffled discard pile.
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        return cardState.isDeckEmpty() ?
                new GameState(ticketDeck, cardState.withDeckRecreatedFromDiscards(rng),
                              currentPlayerId(), playerState, lastPlayer()) : this;
    }

    /**
     * Returns a state identical to the receiver but in which the given tickets have been added to the given player's hand.
     * The ticket's deck is not modified with this method.
     *
     * @param playerId (PlayerId) : the given player who chose the given tickets.
     * @param chosenTickets (SortedBag<Ticket>) : the tickets chosen by the player to add to his hand.
     * @return (GameState) this state with the chosenTickets added to the given player's hand
     * @throws IllegalArgumentException
     *                        if the given player has at least one ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(playerState(playerId).tickets().size() == 0);
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.put(playerId, currentPlayerState().withAddedTickets(chosenTickets));
        return new GameState(ticketDeck, cardState, currentPlayerId(),
                             newPlayerState, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver, but in which the current player has drawn the drawnTickets from the ticket's deck,
     * and chosen to keep those contained in chosenTicket.
     *
     * @param drawnTickets (SortedBag<Ticket>) : the drawn tickets to remove from this ticket's deck.
     * @param chosenTickets (SortedBag<Ticket>) : the tickets chosen by the player.
     * @return (GameState) this state with the chosenTickets added to the current player's hand
                           and the drawnTickets removed from the Ticket's deck.
     * @throws IllegalArgumentException
     *                        if the drawnTickets don't contain the chosenTickets.
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedTickets(chosenTickets));
        return new GameState(ticketDeck, cardState, currentPlayerId(), newPlayerState, lastPlayer())
                   .withoutTopTickets(drawnTickets.size());
    }

    /**
     * Returns an identical state to the receiver except that the face-up card at the slot-th location
     * has been placed in the current player's hand, and replaced by the one at the top of the card's deck.
     *
     * @param slot (int) : index of the visible card.
     * @return (GameState) this state with the face-up card in the slot-th index
     *                     added to the current player's hand and replaced
     *                     by the one at the top of the card's deck.
     * @throws IllegalArgumentException
     *                        if it's not possible to draw a card.
     */
    public GameState withDrawnFaceUpCard(int slot){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.faceUpCard(slot)));
        return new GameState(ticketDeck, cardState.withDrawnFaceUpCard(slot),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver except that the top card of card's deck
     * has been placed in the current player's hand.
     *
     * @return (GameState) this state with the deck's top card added to the current player's hand
     *                     and removed from the card's deck.
     * @throws IllegalArgumentException
     *                        if it's not possible to draw a card.
     */
    public GameState withBlindlyDrawnCard(){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.put(currentPlayerId(), currentPlayerState().withAddedCard(cardState.topDeckCard()));
        return new GameState(ticketDeck, cardState.withoutTopDeckCard(),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver but in which
     * the current player has seized the given route using the given cards.
     *
     * @param route (Route) : the route the current player wants to take over.
     * @param cards (SortedBag<Card>) : the cards used by the current player to get the route.
     * @return (GameState) this state with the given route added to the current player's list of taken routes.
                           and the cards used by the player added to the cardState's discard.
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.put(currentPlayerId(), currentPlayerState().withClaimedRoute(route, cards));
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(cards),
                             currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     * Ends the current player's turn,
     * ie returns an identical state to the receiver except that the next turn's current player
     * is the one following this current player. Furthermore, if lastTurnBegins returns true,
     * this current player becomes the last player of the next turn.
     *
     * @return (GameState) same GameState as this but with the currentPlayer changed to the next one,
                           furthermore, if the lastTurn should begin, the lastPlayer value is
                           set to this currentPlayer.
     */
    public GameState forNextTurn(){
        return lastTurnBegins() ? new GameState(ticketDeck, cardState, currentPlayerId().next(),
                                                playerState, currentPlayerId()) :
                                  new GameState(ticketDeck, cardState, currentPlayerId().next(),
                                                playerState, lastPlayer());
    }

    /**
     * Returns true iff the last turn begins,ie if the identity of the last player is currently
     * unknown but the current player has only two cars or less left.
     * This method should only be called at the end of a player's turn.
     *
     * @return (boolean) true if the identity of the last player is null and the current player has two cars left or less.
     *                   false if not.
     */
    public boolean lastTurnBegins(){
        return lastPlayer() == null && currentPlayerState().carCount() <= CAR_COUNT_FOR_LAST_TURN;
    }

    /**
     * Returns the given player's complete state and not just its public part.
     *
     * @param playerId (PlayerId) : a game player's identity.
     * @return (PlayerState) the complete state of the given player.
     */
    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * Returns the current player's complete state and not just its public part.
     *
     * @return (PlayerState) the complete state of the current player.
     */
    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());
    }

}
