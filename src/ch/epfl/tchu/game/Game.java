package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

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

        GameState gameState = GameState.initial(tickets,rng);

        Player currentPlayer = players.get(gameState.currentPlayerId());
        Player otherPlayer = players.get(gameState.currentPlayerId().next());


        Info infoCurrentPlayer = new Info(playerNames.get(gameState.currentPlayerId()));
        Info infoOtherPlayer = new Info(playerNames.get(gameState.currentPlayerId().next()));

        //DEBUT DE PARTIE
        players.forEach((id,player) -> { player.initPlayers(id,playerNames); });
        currentPlayer.receiveInfo(infoCurrentPlayer.willPlayFirst());
        players.forEach((id,player) -> { player.setInitialTicketChoice(tickets); });
        players.forEach((id,player) -> { player.chooseInitialTickets(); });
        currentPlayer.receiveInfo(infoCurrentPlayer.keptTickets(currentPlayer.chooseTickets(tickets).size()));
        otherPlayer.receiveInfo(infoOtherPlayer.keptTickets(otherPlayer.chooseTickets(tickets).size()));


        // MILIEU DE PARTIE

        currentPlayer.nextTurn();

        switch(currentPlayer.nextTurn()){
            case DRAW_TICKETS:
                currentPlayer.chooseTickets(tickets);
            case DRAW_CARDS:
                currentPlayer.drawSlot();
                currentPlayer.drawSlot();
            case CLAIM_ROUTE:
                currentPlayer.claimedRoute();
                currentPlayer.initialClaimCards();
                if(currentPlayer.claimedRoute().equals(Route.Level.UNDERGROUND)

                ) {
                    //currentPlayer.chooseAdditionalCards();
                }
        }

        if(gameState.lastTurnBegins()){

        }



    }

    /**
     *
     * @param gameState (GameState) : the state of the actual play
     * @param n (int) : number of topCards wanted with n excluded
     * @return (SortedBag<Card>) the first n cards in the deck
     */
    private SortedBag<Card> topCards(GameState gameState, int n){
        List<Card> topCards = new ArrayList<>();
        for(int i = 0; i < n; ++i){
            topCards.add(gameState.topCard());
        }
        return SortedBag.of(topCards);
    }


}
