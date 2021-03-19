package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Modelizes the public part of a Tchu's play state
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /**
     * Default constructor
     *
     * @param ticketsCount (int) : the size of the tickets' deck
     * @param cardState (PublicCardState) : the public state of the cars and locomotives
     * @param currentPlayerId (PlayerId) : the current player's identity
     * @param playerState (Map<PlayerId, PublicPlayerState>) : the public state of the players
     * @param lastPlayer (PlayerId) : the last player's identity. can be null
     * @throws IllegalArgumentException
     *                           if the size of the tickets' deck is negative
     *                           if playerState doesn't contain exactly two pairs
     *
     * @throws NullPointerException
     *                           if one of the arguments, except for lastPlayer, is null
     *
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer){

        Preconditions.checkArgument(ticketsCount >= 0
                                    && playerState.size() == 2
                                    && playerState != null); //  A REVOIR

        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Map.copyOf(playerState);
        this.lastPlayer = lastPlayer;
    }

    /**
     *
     * @return (int) the size of the tickets' deck
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     *
     * @return (boolean) true if the tickets'deck is not empty. false if not
     */
    public boolean canDrawTickets(){
        return ticketsCount != 0 ? true : false;
    }

    /**
     *
     * @return (PublicCardState) the public state of the cars and locomotives
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     *
     * @return (boolean) true if there are at least 5 cards in the cards' deck and the discard pile. false if not.
     */
    public boolean canDrawCards(){
        int cards = cardState.deckSize() + cardState.discardsSize();
        return cards >= 5 ? true : false;
    }

    /**
     *
     * @return (PlayerId) the identity of the current player
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     *
     * @param playerId (PlayerId) : a game player
     * @return (PublicPlayerState) the public part of the given player's state
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     *
     * @return (PublicPlayerState) the public part of the current player's state
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     *
     * @return (List<Routes>) all the routes that the players took over
     */
    public List<Route> claimedRoutes(){
        List<Route> routes = new ArrayList<>();
        for(Route CProads : playerState.get(currentPlayerId).routes()){
            routes.add(CProads);
        }
        if(lastPlayer != null){
            for(Route LProads : playerState.get(lastPlayer).routes()){
                routes.add(LProads);
            }
        }
        return routes;
    }

    /**
     *
     * @return (PlayerId) the last player's identity if known, null if not.
     */
    public PlayerId lastPlayer(){
        return lastPlayer != null ? lastPlayer : null;
    }
}
