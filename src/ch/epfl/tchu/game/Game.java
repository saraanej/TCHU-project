package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Modelizes a Tchu's play
 * Non instanciable class
 *
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 *
 */
public class Game{

    /**
     * simulates a Tchu's play for the given players
     *
     * @param players (Map<PlayerId, Player>) : the players of the Tchu's play
     * @param playerNames (Map<PlayerId, String>) : the player's names of the Tchu's play
     * @param tickets (SortedBag<Ticket>) : the available tickets for the game
     * @param rng (Random) : a random generator
     * @throws IllegalArgumentException
     *                        if one of the maps' size is not equal to 2
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){
        Preconditions.checkArgument(players.size() == 2);
        Preconditions.checkArgument(playerNames.size() == 2);

        /*Info player1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info player2 = new Info(playerNames.get(PlayerId.PLAYER_2));*/


        // L'ordre est important !!

        players.forEach((key,value) -> { value.initPlayers(key,playerNames); });
        //methode premier joueur
        players.forEach((key,value) -> { value.setInitialTicketChoice(tickets); });
        players.forEach((key,value) -> { value.chooseInitialTickets(); });
        //methode receive info

        Player currentPlayer = players.get(PlayerId.PLAYER_1); // A CHANGER

        currentPlayer.nextTurn();

        switch(currentPlayer.nextTurn()){
            case DRAW_CARDS:
                currentPlayer.drawSlot();
                currentPlayer.drawSlot();
            case DRAW_TICKETS:
                currentPlayer.chooseTickets(tickets);
            case CLAIM_ROUTE:
                currentPlayer.claimedRoute();
                currentPlayer.initialClaimCards();
                if(currentPlayer.claimedRoute().equals(Route.Level.UNDERGROUND)
                ) {
                    //currentPlayer.chooseAdditionalCards();
                }
        }








    }


}
