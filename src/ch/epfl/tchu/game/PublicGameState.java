package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The PublicGameState class, public and immutable, represents the public part of the state of a part of tCHu.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId, lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;


    /**
     * Default constructor.
     *
     * @param ticketsCount The size of the tickets' deck.
     * @param cardState The public state of the cars and locomotives.
     * @param currentPlayerId The current player's identity.
     * @param playerState The public state of the players.
     * @param lastPlayer The last player's identity. Can be null.
     * @throws IllegalArgumentException
     *                           if the size of the tickets' deck is negative,
     *                           if playerState doesn't contain exactly two pairs.
     * @throws NullPointerException
     *                           if one of the arguments, except for lastPlayer, is null.
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer){
        Preconditions.checkArgument(ticketsCount >= 0);
        Preconditions.checkArgument(playerState.size() == PlayerId.COUNT);
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Objects.requireNonNull(Map.copyOf(playerState));
        this.ticketsCount = ticketsCount;
        this.lastPlayer = lastPlayer;
    }


    /**
     * @return true if the tickets' deck is not empty. false if not.
     */
    public boolean canDrawTickets(){
        return ticketsCount >= 3; //TODO CHECK IF IT IS A GOOD SOLUTION TO THE NUMBER PR TICKETS NO LONGER DIVISIBLE PAR 3
    }

    /**
     * @return true if there are at least 5 cards in the cards' deck and the discard pile. false if not.
     */
    public boolean canDrawCards(){
        int totalCards = cardState.deckSize() + cardState.discardsSize();
        return totalCards >= Constants.FACE_UP_CARDS_COUNT;
    }

    /**
     * @return The size of the tickets' deck.
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * @return The identity of the current player.
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * @return The last player's identity if known, null if not.
     */
    public PlayerId lastPlayer(){
        return lastPlayer;
    }

    /**
     * @return The public state of the cars and locomotives.
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * @param playerId A game player's identity.
     * @return The public part of the given player's state.
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * @return The public part of the current player's state.
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     * @return All the routes that the players took over.
     */
    public List<Route> claimedRoutes(){
        List<Route> routes = new ArrayList<>();
        for (PlayerId id : PlayerId.ALL) routes.addAll(playerState(id).routes());
//        routes.addAll(currentPlayerState().routes());
//        routes.addAll(playerState(currentPlayerId.next()).routes());
        return routes;
    }
}