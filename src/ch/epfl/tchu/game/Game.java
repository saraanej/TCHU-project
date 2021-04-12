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

    private static final int DRAWN_CARDS_COUNT = 2;

    private Game(){
        throw new UnsupportedOperationException();
    }


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

        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);  // a enlever qd on finit
        Player currentPlayer = players.get(gameState.currentPlayerId());

        Info infoPlayer1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info infoPlayer2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        Map<PlayerId, Info> playersInfoId = new HashMap<>();
        playersInfoId.put(PlayerId.PLAYER_1, infoPlayer1);
        playersInfoId.put(PlayerId.PLAYER_2, infoPlayer2);

        //DEBUT DE PARTIE
        gameState = Begin(playerNames,playersInfoId,players,gameState);


        Map<Player, Info> playersInfo = new HashMap<>();
        playersInfo.put(players.get(PlayerId.PLAYER_1), infoPlayer1);
        playersInfo.put(players.get(PlayerId.PLAYER_2), infoPlayer2);


        // MILIEU DE PARTIE
        boolean lastTurnPlayed = false;
        while(!lastTurnPlayed) {

            receiveInfo(players, playersInfo.get(currentPlayer).canPlay());

            updateState(players,gameState);
            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:

                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    receiveInfo(players, playersInfo.get(currentPlayer).drewTickets(Constants.IN_GAME_TICKETS_COUNT));

                   // gameState = gameState.withoutTopTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);

                    receiveInfo(players, playersInfo.get(currentPlayer).keptTickets(chosenTickets.size()));
                    break;
                case DRAW_CARDS:
                    for(int i = 0 ; i < DRAWN_CARDS_COUNT; ++i){
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        updateState(players,gameState);

                        int slot = currentPlayer.drawSlot();
                        if(slot == Constants.DECK_SLOT) {
                            gameState = gameState.withBlindlyDrawnCard();
                            receiveInfo(players, playersInfo.get(currentPlayer).drewBlindCard());
                        }
                        else if( slot < Constants.FACE_UP_CARDS_COUNT && slot > Constants.DECK_SLOT) {
                            receiveInfo(players, playersInfo.get(currentPlayer)
                                    .drewVisibleCard(gameState.cardState().faceUpCard(slot)));
                            gameState = gameState.withDrawnFaceUpCard(slot);
                        }
                    }
                    break;
                case CLAIM_ROUTE:
                    Route route = currentPlayer.claimedRoute();
                    if(gameState.currentPlayerState().canClaimRoute(route)) {
                        SortedBag<Card> claimCards = currentPlayer.initialClaimCards();

                        if (route.level().equals(Route.Level.UNDERGROUND)){
                            receiveInfo(players, playersInfo.get(currentPlayer).attemptsTunnelClaim(route, claimCards));

                            List<Card> drawn = new ArrayList<>();
                            for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                drawn.add(gameState.topCard());
                                gameState = gameState.withoutTopCard();
                            }
                            SortedBag<Card> drawnCards = SortedBag.of(drawn);
                            gameState = gameState.withMoreDiscardedCards(drawnCards);

                            int nbAdditionalCards = route.additionalClaimCardsCount(claimCards, drawnCards);
                            receiveInfo(players, playersInfo.get(currentPlayer).drewAdditionalCards(drawnCards, nbAdditionalCards));


                            if(nbAdditionalCards > 0) {

                                List<SortedBag<Card>> options = gameState
                                                                .currentPlayerState()
                                                                .possibleAdditionalCards(nbAdditionalCards,claimCards,drawnCards);
                                if(!options.isEmpty()){
                                    SortedBag<Card> additional = currentPlayer.chooseAdditionalCards(options);
                                    if(!additional.isEmpty()) {
                                        gameState = gameState.withClaimedRoute(route, claimCards.union(additional)); 
                                        receiveInfo(players, playersInfo.get(currentPlayer)
                                                             .claimedRoute(route, claimCards.union(additional)));
                                    } else {
                                        receiveInfo(players, playersInfo.get(currentPlayer).didNotClaimRoute(route));
                                    }
                                } else {
                                    receiveInfo(players, playersInfo.get(currentPlayer).didNotClaimRoute(route));
                                }
                                break;
                            }
                        }
                        gameState = gameState.withClaimedRoute(route,claimCards);
                        receiveInfo(players, playersInfo.get(currentPlayer).claimedRoute(route, claimCards));
                    }
                    break;
            }

            if(gameState.lastTurnBegins()){
                receiveInfo(players, playersInfo.get(currentPlayer).lastTurnBegins(gameState.currentPlayerState().carCount()));
            }

            if (gameState.lastPlayer() != null && gameState.currentPlayerId() == gameState.lastPlayer()) lastTurnPlayed = true;

            gameState = gameState.forNextTurn();
            currentPlayer = players.get(gameState.currentPlayerId());
            
            if (gameState.lastPlayer() != null) {
                System.out.println(gameState.playerState(gameState.lastPlayer()).carCount());
                System.out.println(playerNames.get(gameState.lastPlayer()));
            }
        }

        // FIN DE PARTIE
        updateState(players,gameState);

        int pointsPlayer1 = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int pointsPlayer2 = gameState.playerState(PlayerId.PLAYER_2).finalPoints();

        List<Route> routes1 = gameState.playerState(PlayerId.PLAYER_1).routes();
        List<Route> routes2 = gameState.playerState(PlayerId.PLAYER_2).routes();

        Trail P1 = Trail.longest(routes1);
        Trail P2 = Trail.longest(routes2);

        switch (longest(P1,P2)){
            case 1 : pointsPlayer1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
                receiveInfo(players, playersInfo.get(player1).getsLongestTrailBonus(P1));
                break;
            case 2 : pointsPlayer2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
                receiveInfo(players, playersInfo.get(player2).getsLongestTrailBonus(P2));
                break;
            case 0 : pointsPlayer1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
                     pointsPlayer2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
                receiveInfo(players, playersInfo.get(player1).getsLongestTrailBonus(P1));
                receiveInfo(players, playersInfo.get(player2).getsLongestTrailBonus(P2));
                break;
        }

        Player winner = pointsPlayer1 > pointsPlayer2 ? player1 :
                        pointsPlayer1 < pointsPlayer2 ? player2 : null;

        int maxPoints = pointsPlayer1 > pointsPlayer2 ? pointsPlayer1 : pointsPlayer2;
        int loserPoints = pointsPlayer1 > pointsPlayer2 ? pointsPlayer2 : pointsPlayer1;

        List<String> names = new ArrayList<>();
        playerNames.forEach((id,name) -> { names.add(name);});
        if(winner == null) receiveInfo(players, Info.draw(names, pointsPlayer1));
        else receiveInfo(players, playersInfo.get(winner).won(maxPoints,loserPoints));
    }

    private static GameState Begin(Map<PlayerId, String> playerNames, Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState){
        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        players.forEach((id,player) -> { player.initPlayers(id,playerNames);});
        receiveInfo(players, playersInfo.get(gameState.currentPlayerId()).willPlayFirst());

        SortedBag<Ticket> initialTickets = gameState.topTickets(Constants.INITIAL_TICKETS_COUNT);
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        player1.setInitialTicketChoice(initialTickets);
        initialTickets = gameState.topTickets(Constants.INITIAL_TICKETS_COUNT);
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        player2.setInitialTicketChoice(initialTickets);

        updateState(players,gameState);

        SortedBag<Ticket> chosenTickets1 = player1.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, chosenTickets1);
        SortedBag<Ticket> chosenTickets2 = player2.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_2, chosenTickets2);

        receiveInfo(players, playersInfo.get(PlayerId.PLAYER_1).keptTickets(chosenTickets1.size()));
        receiveInfo(players, playersInfo.get(PlayerId.PLAYER_2).keptTickets(chosenTickets2.size()));

        return gameState;
    }
    /**
     * @param longestP1 the longest Trail of the player 1
     * @param longestP2 the longest Trail of the player 2
     * @return (int) 1 if P1 is the longest, 2, if P2 is the longest, 0 if both trails have the same length
     */
    private static int longest(Trail longestP1, Trail longestP2){
        return longestP1.length() > longestP2.length() ? 1 :
                longestP1.length() == longestP2.length() ? 0 : 2;
    }

    /**
     * update both players' states
     * @param players (Map<PlayerId, Player>) the players of the Tchu's play
     * @param gameState (GameState) the new updated version of the current gameState
     */
    private static void updateState(Map<PlayerId, Player> players, GameState gameState){
        players.forEach((id, player) -> {player.updateState(gameState, gameState.playerState(id));});
    }

    /**
     * communicates the info str to the players
     * @param players (Map<PlayerId, Player>) the players of the Tchu's play
     * @param str (String) the info to communicate to the players
     */
    private static void receiveInfo(Map<PlayerId, Player> players, String str){
        players.forEach((id,player) -> { player.receiveInfo(str);});
    }

    /**
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

    /**
     * @param gameState (GameState) : the state of the actual play
     * @param n (int) : number of topCards wanted with n excluded
     * @return (GameState) the GameState gameState without the n TopCards
     */
    private static GameState withoutTopCards(GameState gameState, int n){
        for (int i = 0; i < n; ++i) {
            gameState = gameState.withoutTopCard();
        }
        return gameState;
    }
}