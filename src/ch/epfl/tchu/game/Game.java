package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.awt.*;
import java.util.*;
import java.util.List;

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

        Map<PlayerId, Info> playersInfo = new HashMap<>();
        for (PlayerId playerId: PlayerId.values()) {
            playersInfo.put(playerId, new Info(playerNames.get(playerId)));
        }

        GameState gameState = GameState.initial(tickets,rng);
        gameState = Begin(playerNames,playersInfo,players,gameState);

        boolean lastTurnPlayed = false;
        while(!lastTurnPlayed) {
            receiveInfo(players, playersInfo.get(gameState.currentPlayerId()).canPlay());
            updateState(players,gameState);
            switch (players.get(gameState.currentPlayerId()).nextTurn()) {
                case DRAW_TICKETS:
                    gameState = drawTickets(playersInfo,players,gameState);
                    break;
                case DRAW_CARDS:
                    gameState = drawCards(playersInfo,players,gameState,rng);
                    break;
                case CLAIM_ROUTE:
                    gameState = claimRoute(playersInfo,players,gameState,rng);
                    break;
            }
            if(gameState.lastTurnBegins())
                receiveInfo(players, playersInfo.get(gameState.currentPlayerId())
                                     .lastTurnBegins(gameState.currentPlayerState().carCount()));
            if (gameState.lastPlayer() != null && gameState.currentPlayerId() == gameState.lastPlayer())
                lastTurnPlayed = true;
            gameState = gameState.forNextTurn();
        }

        finish(playerNames,playersInfo,players,gameState);
    }



    private static GameState Begin(Map<PlayerId, String> playerNames, Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState){
        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        players.forEach((id,player) -> player.initPlayers(id,playerNames));
        receiveInfo(players, playersInfo.get(gameState.currentPlayerId()).willPlayFirst());

        //ESSAYER AVEC LAMBDA
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

    private static GameState drawTickets(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState){
        PlayerId currentPlayerId = gameState.currentPlayerId();

        SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
        receiveInfo(players, playersInfo.get(currentPlayerId).drewTickets(Constants.IN_GAME_TICKETS_COUNT));

        SortedBag<Ticket> chosenTickets = players.get(currentPlayerId).chooseTickets(drawnTickets);
        gameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);

        receiveInfo(players, playersInfo.get(currentPlayerId).keptTickets(chosenTickets.size()));

        return gameState;
    }

    private static GameState drawCards(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState, Random rng){
        PlayerId currentPlayerId = gameState.currentPlayerId();

        for(int i = 0 ; i < DRAWN_CARDS_COUNT; ++i){
            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
            updateState(players,gameState);

            int slot = players.get(currentPlayerId).drawSlot();
            if(slot == Constants.DECK_SLOT) {
                gameState = gameState.withBlindlyDrawnCard();
                receiveInfo(players, playersInfo.get(currentPlayerId).drewBlindCard());
            }
            else if( slot < Constants.FACE_UP_CARDS_COUNT && slot > Constants.DECK_SLOT) {
                receiveInfo(players, playersInfo.get(currentPlayerId)
                        .drewVisibleCard(gameState.cardState().faceUpCard(slot)));
                gameState = gameState.withDrawnFaceUpCard(slot);
            }
        }

        return gameState;
    }

    private static GameState claimRoute(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState, Random rng){
        PlayerId currentPlayerId = gameState.currentPlayerId();
        Player currentPlayer = players.get(currentPlayerId);

        Route route = currentPlayer.claimedRoute();
        if(gameState.currentPlayerState().canClaimRoute(route)) {
            SortedBag<Card> claimCards = currentPlayer.initialClaimCards();

            if (route.level().equals(Route.Level.UNDERGROUND)){
                receiveInfo(players, playersInfo.get(currentPlayerId).attemptsTunnelClaim(route, claimCards));

                List<Card> drawn = new ArrayList<>();
                for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    drawn.add(gameState.topCard());
                    gameState = gameState.withoutTopCard();
                }
                SortedBag<Card> drawnCards = SortedBag.of(drawn);
                gameState = gameState.withMoreDiscardedCards(drawnCards);

                int nbAdditionalCards = route.additionalClaimCardsCount(claimCards, drawnCards);
                receiveInfo(players, playersInfo.get(currentPlayerId).drewAdditionalCards(drawnCards, nbAdditionalCards));

                if(nbAdditionalCards > 0) {
                    List<SortedBag<Card>> options = gameState
                            .currentPlayerState()
                            .possibleAdditionalCards(nbAdditionalCards,claimCards,drawnCards);
                    if(!options.isEmpty()){
                        SortedBag<Card> additional = currentPlayer.chooseAdditionalCards(options);
                        if(!additional.isEmpty()) {
                            gameState = gameState.withClaimedRoute(route, claimCards.union(additional));
                            receiveInfo(players, playersInfo.get(currentPlayerId)
                                    .claimedRoute(route, claimCards.union(additional)));
                        } else {
                            receiveInfo(players, playersInfo.get(currentPlayerId).didNotClaimRoute(route));
                        }
                    } else {
                        receiveInfo(players, playersInfo.get(currentPlayerId).didNotClaimRoute(route));
                    }
                    return gameState;
                }
            }
            gameState = gameState.withClaimedRoute(route,claimCards);
            receiveInfo(players, playersInfo.get(currentPlayerId).claimedRoute(route, claimCards));
        }
        return gameState;
    }

    /**
     * 
     * @param playerNames
     * @param playersInfo
     * @param players
     * @param gameState
     */
    private static void finish(Map<PlayerId, String> playerNames, Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState){
        updateState(players,gameState);

        Map<PlayerId,Integer> playersPoints = new EnumMap<>(PlayerId.class);
        Map<PlayerId,Trail> playersTrail = new EnumMap<>(PlayerId.class);

        for (PlayerId playerId: PlayerId.values()) {
            playersPoints.put(playerId, gameState.playerState(playerId).finalPoints());
            playersTrail.put(playerId,Trail.longest(gameState.playerState(playerId).routes()));
        }

        PlayerId bonusPlayer = longest(playersTrail);

        if (bonusPlayer == null) {
            playersPoints.forEach(((playerId, integer) ->
                    playersPoints.put(playerId, integer + Constants.LONGEST_TRAIL_BONUS_POINTS)));
            playersTrail.forEach(((playerId, trail) ->
                    receiveInfo(players, playersInfo.get(playerId).getsLongestTrailBonus(trail))));
        } else {
            playersPoints.put(bonusPlayer, playersPoints.get(bonusPlayer) + Constants.LONGEST_TRAIL_BONUS_POINTS);
            receiveInfo(players, playersInfo.get(bonusPlayer).getsLongestTrailBonus(playersTrail.get(bonusPlayer)));
        }

        int minPoints = playersPoints.values()
                .stream()
                .min(Integer::compare).get();
        Map.Entry<PlayerId, Integer> winner = winner(playersPoints,minPoints);

        List<String> names = List.copyOf(playerNames.values());
        if(winner.getKey() == null) receiveInfo(players, Info.draw(names, minPoints));
        else receiveInfo(players, playersInfo.get(winner.getKey()).won(winner.getValue(), minPoints));
    }

    /**
     *
     * @param playersPoints
     * @param minPoints
     * @return
     */
    private static Map.Entry<PlayerId, Integer> winner(Map<PlayerId,Integer> playersPoints, int minPoints){
        Map.Entry<PlayerId, Integer> winner = null;

        for (Map.Entry<PlayerId,Integer> entry : playersPoints.entrySet()) {
            if(entry.getValue() > minPoints) {
                minPoints = entry.getValue();
                winner = entry;
            }
        }
        return winner;
    }

    /**
     * @param playersTrail (Map<PlayerId,Trail>) the longest Trail of the player
     * @return (int) 1 if P1 is the longest, 2, if P2 is the longest, 0 if both trails have the same length
     */
    private static PlayerId longest(Map<PlayerId,Trail> playersTrail){
        PlayerId winner = null;
        int length = playersTrail.values()
                .stream()
                .min(Comparator.comparingInt(Trail::length)).get().length();;

        for (Map.Entry<PlayerId,Trail> entry : playersTrail.entrySet()) {
            if(entry.getValue().length() > length) {
                length = entry.getValue().length();
                winner = entry.getKey();
            }
        }
        return winner;
    }

    /**
     * update both players' states
     * @param players (Map<PlayerId, Player>) the players of the Tchu's play
     * @param gameState (GameState) the new updated version of the current gameState
     */
    private static void updateState(Map<PlayerId, Player> players, GameState gameState){
        players.forEach((id, player) -> player.updateState(gameState, gameState.playerState(id)));
    }

    /**
     * communicates the info str to the players
     * @param players (Map<PlayerId, Player>) the players of the Tchu's play
     * @param info (String) the info to communicate to the players
     */
    private static void receiveInfo(Map<PlayerId, Player> players, String info){
        players.forEach((id,player) -> player.receiveInfo(info));
    }
}