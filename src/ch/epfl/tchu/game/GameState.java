package ch.epfl.tchu.game;


import ch.epfl.tchu.SortedBag;

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
