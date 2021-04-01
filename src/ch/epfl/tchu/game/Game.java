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
public class Game {

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

        // PROBLEME LE GAMESTATE CHANGE JAMS DU COUP LE CURRENTPLAYER NON PLUS

        //Faut creer la methode private de receiveinfo apres

        // NOTE POUR RECEIVEINFO TROUVER LE MOYEN QU ELLE NE DEPENDE QUE DE PLAYER 1 OU 2 CA FACILITERA AU LIEU DE REMTTRE A JOUR A CHAQUE FOIS

        GameState gameState = GameState.initial(tickets,rng);
        //  PublicGameState publicGameState =


        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        Info infoPlayer1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info infoPlayer2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        Player currentPlayer = players.get(gameState.currentPlayerId());
        Player otherPlayer = players.get(gameState.currentPlayerId().next());

       //Le currentplayer change c est pas une valeur fixe pour initiliser les noms avec (fait plus haut avec playerID1 et 2 directemnt)

        Info infoCurrentPlayer = new Info(playerNames.get(gameState.currentPlayerId()));
        Info infoOtherPlayer = new Info(playerNames.get(gameState.currentPlayerId().next()));

        //DEBUT DE PARTIE

        players.forEach((id,player) -> { player.initPlayers(id,playerNames); });
        players.forEach((id,player) -> { player.receiveInfo(infoCurrentPlayer.willPlayFirst()); });

        //Player1 chooses tickets
        SortedBag<Ticket> initialTickets = gameState.topTickets(5);
        gameState = gameState.withoutTopTickets(5);
        player1.setInitialTicketChoice(initialTickets);

        //player 2 chooses tickets
        initialTickets = gameState.topTickets(5);
        gameState = gameState.withoutTopTickets(5);
        player2.setInitialTicketChoice(initialTickets);

    //    player1.updateState(,gameState.playerState(PlayerId.PLAYER_1)); BESOIN DE CREER UN PUBLICGAMESTATE POUR LAPPELER (DECLARATION NON FINIE PLUS HAUT)
        //tickets chosen added to player1
        SortedBag<Ticket> chosenTickets1 = player1.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, chosenTickets1);

        //tickets chosen added to player 2
        SortedBag<Ticket> chosenTickets2 = player2.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_2, chosenTickets2);

        player1.receiveInfo(infoPlayer1.keptTickets(chosenTickets1.size()));
        player2.receiveInfo(infoPlayer2.keptTickets(chosenTickets2.size()));


        // MILIEU DE PARTIE

        while(!gameState.lastTurnBegins()) {
           // currentPlayer.nextTurn(); //il est deja appele a la ligne 93
            gameState = gameState.forNextTurn(); // je suis pas sure si c est a appeler au debut ou a la fin de la boucle a voir apres avec les regles
            currentPlayer = players.get(gameState.currentPlayerId());

            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:
                    currentPlayer.chooseTickets(tickets);
                case DRAW_CARDS:
                    currentPlayer.drawSlot();
                    currentPlayer.drawSlot();
                case CLAIM_ROUTE:
                    Route route = currentPlayer.claimedRoute();
                    SortedBag<Card> claimCards = currentPlayer.initialClaimCards();
                    int nbAdditionalCards = route.additionalClaimCardsCount(claimCards, topCards(gameState,3));
                    if (route.equals(Route.Level.UNDERGROUND)
                            && nbAdditionalCards >= 1
                            && gameState.currentPlayerState().possibleAdditionalCards(nbAdditionalCards, claimCards,topCards(gameState,3)).contains(gameState.currentPlayerState().cards())
                             // A MODIFIER IL FAUT VERIFIER QUE LES CARTES DU JOUEUR CONTIENNENT UNE DES COMPO DE POSSIBLEADDITIONALCARDS
                    ) {
                        //currentPlayer.chooseAdditionalCards();
                    }
            }
        }

        // FIN DE PARTIE

      /*  if(gameState.lastTurnBegins()){
        pas besoin avec le while
        }*/



    }

    /**
     *
     * @param gameState (GameState) : the state of the actual play
     * @param n (int) : number of topCards wanted with n excluded
     * @return (SortedBag<Card>) the first n cards in the deck
     */
    private static SortedBag<Card> topCards(GameState gameState, int n){
        List<Card> topCards = new ArrayList<>();
        for(int i = 0; i < n; ++i){
            topCards.add(gameState.topCard());
        }
        return SortedBag.of(topCards);
    }
    

}
