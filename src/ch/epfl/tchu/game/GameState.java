package ch.epfl.tchu.game;


import ch.epfl.tchu.SortedBag;

import java.util.Map;
import java.util.Random;

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
     * Default constructor
     *
     * @param ticketsCount    (int) : the size of the tickets' deck
     * @param cardState       (PublicCardState) : the public state of the cars and locomotives
     * @param currentPlayerId (PlayerId) : the current player's identity
     * @param playerState     (Map<PlayerId, PublicPlayerState>) : the public state of the players
     * @param lastPlayer      (PlayerId) : the last player's identity. can be null
     * @throws IllegalArgumentException if the size of the tickets' deck is negative
     *                                  if playerState doesn't contain exactly two pairs
     * @throws NullPointerException     if one of the arguments, except for lastPlayer, is null
     */
    public GameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        super(ticketsCount, cardState, currentPlayerId, playerState, lastPlayer);
    }

    // EXTENDS PUBLICGAMESTATE
    /**
     * static constructor method
     *
     * @param tickets (SortedBag<Ticket>) : the tickets' deck contains these given tickets
     * @param rng (Random) : the given random generator
     * @return (GameState) the initial state of a Tchu's play
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
        return null;
    }




}
