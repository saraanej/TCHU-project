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

        //Faut creer la methode private de receiveinfo apres

        // NOTE POUR RECEIVEINFO TROUVER LE MOYEN QU ELLE NE DEPENDE QUE DE PLAYER 1 OU 2 CA FACILITERA AU LIEU DE REMTTRE A JOUR A CHAQUE FOIS

        GameState gameState = GameState.initial(tickets,rng);

        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        Info infoPlayer1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info infoPlayer2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        Player currentPlayer = players.get(gameState.currentPlayerId());
        Player otherPlayer = players.get(gameState.currentPlayerId().next());

        //Le currentplayer change c est pas une valeur fixe pour initiliser les noms avec (fait plus haut avec playerID1 et 2 directemnt)

        Info infoCurrentPlayer = new Info(playerNames.get(gameState.currentPlayerId()));
        Info infoOtherPlayer = new Info(playerNames.get(gameState.currentPlayerId().next()));

        /*Map<Player, Info> playersInfo = new HashMap<>();
        playersInfo.put(player1, infoPlayer1);
        playersInfo.put(player2, infoPlayer2);*/

        //DEBUT DE PARTIE

        players.forEach((id,player) -> { player.initPlayers(id,playerNames); });
        players.forEach((id,player) -> { player.receiveInfo(infoCurrentPlayer.willPlayFirst()); });

        //Player1 and 2 chooses tickets
        SortedBag<Ticket> initialTickets = gameState.topTickets(Constants.INITIAL_TICKETS_COUNT);
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        player1.setInitialTicketChoice(initialTickets);
        initialTickets = gameState.topTickets(Constants.INITIAL_TICKETS_COUNT);
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        player2.setInitialTicketChoice(initialTickets);

        updateState(players,gameState);
        //tickets chosen added to player1 and 2
        SortedBag<Ticket> chosenTickets1 = player1.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, chosenTickets1);
        SortedBag<Ticket> chosenTickets2 = player2.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_2, chosenTickets2);

        player1.receiveInfo(infoPlayer1.keptTickets(chosenTickets1.size()));
        player2.receiveInfo(infoPlayer2.keptTickets(chosenTickets2.size()));


        // MILIEU DE PARTIE

        SortedBag<Ticket> drawnTickets;
        SortedBag<Ticket> chosenTickets;
        while(!gameState.lastTurnBegins()) {
            //updateCurrentPlayerState(currentPlayer,gameState, gameState.currentPlayerState()); il faut update l'état des 2 joueurs
            updateState(players,gameState);
            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:
                    drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    gameState = gameState.withoutTopTickets(Constants.IN_GAME_TICKETS_COUNT);
                    chosenTickets = currentPlayer.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);
                    break;
                case DRAW_CARDS:
                    for(int i = 0 ; i < 2; ++i){
                        updateState(players,gameState);
                        int slot = currentPlayer.drawSlot();
                        if(slot == Constants.DECK_SLOT) gameState = gameState.withBlindlyDrawnCard();
                        else if( slot <= Constants.FACE_UP_CARDS_COUNT-1 && slot >= 0)
                            gameState = gameState.withDrawnFaceUpCard(slot);
                        }
                    break;
                case CLAIM_ROUTE:
                    Route route = currentPlayer.claimedRoute();
                    SortedBag<Card> claimCards = currentPlayer.initialClaimCards();
                    int nbAdditionalCards = route.additionalClaimCardsCount(claimCards, topCards(gameState,3,rng));
                    if (route.equals(Route.Level.UNDERGROUND)
                            && nbAdditionalCards >= 1
                            && gameState.currentPlayerState().possibleAdditionalCards(nbAdditionalCards, claimCards,topCards(gameState,3,rng)).contains(gameState.currentPlayerState().cards())
                             // A MODIFIER IL FAUT VERIFIER QUE LES CARTES DU JOUEUR CONTIENNENT UNE DES COMPO DE POSSIBLEADDITIONALCARDS
                    ) {
                        //currentPlayer.chooseAdditionalCards();
                    }
                    break;
            }
            gameState = gameState.forNextTurn();
            currentPlayer = players.get(gameState.currentPlayerId());
        }

        // FIN DE PARTIE

      /*  if(gameState.lastTurnBegins()){
        pas besoin avec le while
        }*/


        updateState(players,gameState);

    }


    private static void updateCurrentPlayerState(Player current, GameState gameState, PlayerState ownState){
        current.updateState(gameState, ownState);
    }

//ATTENTION VERIFIER SI FAUT TJRS UPDATE LES 2 JOUEURS OU JUSTE UN SEUL
    /**
     * update both players states
     * @param players
     * @param gameState
     */
    private static void updateState(Map<PlayerId, Player> players, GameState gameState){
        players.forEach((id, player) -> {player.updateState(gameState, gameState.playerState(id));});
    }

    /**
     *
     * @param gameState (GameState) : the state of the actual play
     * @param n (int) : number of topCards wanted with n excluded
     * @return (SortedBag<Card>) the first n cards in the deck
     */
    private static SortedBag<Card> topCards(GameState gameState, int n, Random rng){
       GameState game = gameState;
        List<Card> topCards = new ArrayList<>();
        for(int i = 0; i < n; ++i){
            game = game.withCardsDeckRecreatedIfNeeded(rng); //Faut faire attention avec topcards faut le but dappeler topCARD TT SEUL A CHAQUE FOIS c pour pouvoir appeler withrecreatedDeckfromdiscard
            topCards.add(game.topCard());
        }
        return SortedBag.of(topCards);
    }
    

}
