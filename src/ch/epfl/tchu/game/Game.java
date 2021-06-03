package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import com.sun.source.tree.Tree;
import static ch.epfl.tchu.game.Constants.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Game class, public, final and non-instantiable, represents a part of tCHu.
 * It only offers one public and static method.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
public class Game {

    private static final int DRAWN_CARDS_COUNT = 2;
    private static final int NUMBER_OF_PLAYERS = PlayerId.count();


    /**
     * Simulates a Tchu's play for the given players.
     *
     * @param players the players of the Tchu's play
     * @param playerNames the player's names of the Tchu's play
     * @param tickets the available tickets for the game
     * @param rng a random generator used to shuffle the decks
     * @throws IllegalArgumentException if one of the maps' size is not equal to 2
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument(players.size() == NUMBER_OF_PLAYERS);
        Preconditions.checkArgument(playerNames.size() == NUMBER_OF_PLAYERS);

        Map<PlayerId, Info> playersInfo = new EnumMap<>(PlayerId.class);
        for (PlayerId playerId : PlayerId.all()) {
            playersInfo.put(playerId, new Info(playerNames.get(playerId)));
        }

        GameState gameState = GameState.initial(tickets, rng);
        gameState = begin(playerNames, playersInfo, players, gameState);

        boolean lastTurnPlayed = false;
        while (!lastTurnPlayed) {
            receiveInfo(players, playersInfo.get(gameState.currentPlayerId()).canPlay());
            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
            updateState(players, gameState);

            switch (players.get(gameState.currentPlayerId()).nextTurn()) {
                case DRAW_TICKETS:
                    gameState = drawTickets(playersInfo, players, gameState);
                    break;
                case DRAW_CARDS:
                    gameState = drawCards(playersInfo, players, gameState, rng);
                    break;
                case CLAIM_ROUTE:
                    gameState = claimRoute(playersInfo, players, gameState, rng);
                    break;
            }

            if (gameState.lastTurnBegins())
                receiveInfo(players, playersInfo.get(gameState.currentPlayerId())
                        .lastTurnBegins(gameState.currentPlayerState().carCount()));
            if (gameState.lastPlayer() != null && gameState.currentPlayerId() == gameState.lastPlayer())
                lastTurnPlayed = true;

            gameState = gameState.forNextTurn();
        }
        finish(playerNames, playersInfo, players, gameState);
    }


    /**
     * Called to initialize the game : initializes players and lets the players choose their initial Tickets.
     *
     * @return the gameState after initializing the game.
     */
    private static GameState begin(Map<PlayerId, String> playerNames, Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState) {
        players.forEach((id, player) -> player.initPlayers(id, playerNames));
        receiveInfo(players, playersInfo.get(gameState.currentPlayerId()).willPlayFirst());

        SortedBag<Ticket> initialTickets;
        for (PlayerId playerId : PlayerId.all()) {
            initialTickets = gameState.topTickets(INITIAL_TICKETS_COUNT);
            gameState = gameState.withoutTopTickets(INITIAL_TICKETS_COUNT);
            players.get(playerId).setInitialTicketChoice(initialTickets);
        }
        updateState(players, gameState);
        Map<PlayerId,SortedBag<Ticket>> pTickets = new EnumMap<>(PlayerId.class);
        for (PlayerId id : PlayerId.all()) {
            pTickets.put(id, players.get(id).chooseInitialTickets());
            gameState = gameState.withInitiallyChosenTickets(id, pTickets.get(id));
        }
        pTickets.forEach((i,t) ->
                receiveInfo(players, playersInfo.get(i).keptTickets(t.size())));
        return gameState;
    }

    /**
     * Called if the  current player wants to draw tickets.
     *
     * @return the gameState after the player finished drawing and choosing his tickets.
     */
    private static GameState drawTickets(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState) {
        PlayerId currentPlayerId = gameState.currentPlayerId();

        SortedBag<Ticket> drawnTickets = gameState.topTickets(IN_GAME_TICKETS_COUNT);
        receiveInfo(players, playersInfo.get(currentPlayerId).drewTickets(IN_GAME_TICKETS_COUNT));

        SortedBag<Ticket> chosenTickets = players.get(currentPlayerId).chooseTickets(drawnTickets);
        gameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);

        receiveInfo(players, playersInfo.get(currentPlayerId).keptTickets(chosenTickets.size()));

        return gameState;
    }

    /**
     * Called if the  current player wants to draw a card from the deck or from the face-up cards.
     *
     * @return the gameState after the player finished drawing cards.
     */
    private static GameState drawCards(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState, Random rng) {
        PlayerId currentPlayerId = gameState.currentPlayerId();

        for (int i = 0; i < DRAWN_CARDS_COUNT; ++i) {
            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
            updateState(players, gameState);

            int slot = players.get(currentPlayerId).drawSlot();
            if (slot == DECK_SLOT) {
                players.get(currentPlayerId).showCard(gameState.topCard());
                gameState = gameState.withBlindlyDrawnCard();
                receiveInfo(players, playersInfo.get(currentPlayerId).drewBlindCard());
            } else if (slot < FACE_UP_CARDS_COUNT) {
                receiveInfo(players, playersInfo.get(currentPlayerId)
                        .drewVisibleCard(gameState.cardState().faceUpCard(slot)));
                gameState = gameState.withDrawnFaceUpCard(slot);
            }
        }
        return gameState;
    }

    /**
     * Called if the  current player wants to claim a route.
     *
     * @return the gameState after the player finished tying to claim his route.
     */
    private static GameState claimRoute(Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState, Random rng) {
        PlayerId currentPlayerId = gameState.currentPlayerId();
        Player currentPlayer = players.get(currentPlayerId);

        Route route = currentPlayer.claimedRoute();
            SortedBag<Card> claimCards = currentPlayer.initialClaimCards();

            if (route.level().equals(Route.Level.UNDERGROUND)) {
                receiveInfo(players, playersInfo.get(currentPlayerId).attemptsTunnelClaim(route, claimCards));

                List<Card> drawn = new ArrayList<>();
                for (int i = 0; i < ADDITIONAL_TUNNEL_CARDS; ++i) {
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    drawn.add(gameState.topCard());
                    gameState = gameState.withoutTopCard();
                }
                SortedBag<Card> drawnCards = SortedBag.of(drawn);
                gameState = gameState.withMoreDiscardedCards(drawnCards);

                int nbAdditionalCards = route.additionalClaimCardsCount(claimCards, drawnCards);
                receiveInfo(players, playersInfo.get(currentPlayerId).drewAdditionalCards(drawnCards, nbAdditionalCards));

                if (nbAdditionalCards > 0) {
                    List<SortedBag<Card>> options = gameState
                            .currentPlayerState()
                            .possibleAdditionalCards(nbAdditionalCards, claimCards);
                    if (!options.isEmpty()) {
                        SortedBag<Card> additional = currentPlayer.chooseAdditionalCards(options);
                        if (!additional.isEmpty()) {
                            gameState = gameState.withClaimedRoute(route, claimCards.union(additional));
                            receiveInfo(players, playersInfo.get(currentPlayerId)
                                    .claimedRoute(route, claimCards.union(additional)));
                            return gameState;
                        }
                    }
                    receiveInfo(players, playersInfo.get(currentPlayerId).didNotClaimRoute(route));
                    return gameState;
                }
            }
            gameState = gameState.withClaimedRoute(route, claimCards);
            receiveInfo(players, playersInfo.get(currentPlayerId).claimedRoute(route, claimCards));
        return gameState;
    }

    /**
     * Finishes the game, communicates the final state of the game
     * and computes each player's points to determine and communicate the winner.
     *
     * NOTE : This code was written generally, presupposing that the number of players may be superior to two,
     * which explains the additional for loops and calculation of the minimal points
     * that may seem useless if the number of players were only 2.
     */
    private static void finish(Map<PlayerId, String> playerNames, Map<PlayerId, Info> playersInfo, Map<PlayerId, Player> players, GameState gameState) {
        updateState(players, gameState);
        Map<PlayerId, Integer> playersPoints = new EnumMap<>(PlayerId.class);
        Map<PlayerId, Trail> playersTrail = new EnumMap<>(PlayerId.class);

        for (PlayerId playerId : PlayerId.all()) {
            playersPoints.put(playerId, gameState.playerState(playerId).finalPoints());
            playersTrail.put(playerId, Trail.longest(gameState.playerState(playerId).routes()));
        }

        PlayerId bonusPlayer = longest(playersTrail);

        if (bonusPlayer == null) {
            playersPoints.forEach(((playerId, integer) ->
                    playersPoints.put(playerId, integer + LONGEST_TRAIL_BONUS_POINTS)));
            //playersTrail.forEach(((playerId, trail) ->
                    //receiveInfo(players, playersInfo.get(playerId).getsLongestTrailBonus(trail))));
        } else {
            playersPoints.put(bonusPlayer, playersPoints.get(bonusPlayer) + LONGEST_TRAIL_BONUS_POINTS);
            //receiveInfo(players, playersInfo.get(bonusPlayer).getsLongestTrailBonus(playersTrail.get(bonusPlayer)));
        }
        //Used this method (.min) to determine the points of the loser player since there might be more than 2 players in the future
        int minPoints = Collections.min(playersPoints.values());
        Map.Entry<PlayerId, Integer> winner = winner(playersPoints, minPoints);

        //if (winner.getKey() == null) receiveInfo(players, Info.draw(new ArrayList<>(playerNames.values()), minPoints));
        //else receiveInfo(players, playersInfo.get(winner.getKey())
                //.won(winner.getValue(), minPoints)); //todo receiveinfo a changer avec multijoueurs et fenetre de fin

        for(PlayerId playerId : PlayerId.all()) {
            players.
                    get(playerId).
                    endGame(
                            winner.getKey() == null ? PlayerId.PLAYER_1 : winner.getKey(),
                    playersPoints,
                    bonusPlayer == null ? PlayerId.PLAYER_1 : bonusPlayer,
                    playersTrail);
        }
    }

    /**
     * Updates both players' states.
     *
     * @param players the players of the Tchu's play
     * @param gameState the new updated version of the current gameState
     */
    private static void updateState(Map<PlayerId, Player> players, GameState gameState) {
        players.forEach((id, player) -> player.updateState(gameState, gameState.playerState(id)));
    }

    /**
     * Communicates the info str to the players.
     *
     * @param players the players of the Tchu's play.
     * @param info the info to communicate to the players.
     */
    private static void receiveInfo(Map<PlayerId, Player> players, String info) {
        players.forEach((id, player) -> player.receiveInfo(info));
    }

    /**
     * Determine the winner's playerId and his corresponding maxPoints.
     *
     * @return the winner of the game and the corresponding maxPoints
     * if null, both players are ex-quo.
     */
    private static Map.Entry<PlayerId, Integer> winner(Map<PlayerId, Integer> playersPoints, int minPoints) {
        Map.Entry<PlayerId, Integer> winner = playersPoints.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue())).get();
        return winner.getValue() == minPoints ? null : winner;
    }

    /**
     * Determines the player.
     *
     * @return the identity of the player who has the longest Trail,
     * if null, both players have the longestTrail.
     */
    private static PlayerId longest(Map<PlayerId, Trail> playersTrail) {
        //Used this method (.min) to determine the smallest Trail's length because there might be more than 2 players in the future
        Map.Entry<PlayerId, Trail> min = playersTrail.entrySet().stream()
                .min(Comparator.comparingInt(e -> e.getValue().length())).get();

        PlayerId bonusWinner = playersTrail.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().length())).get().getKey();

        return bonusWinner == min.getKey() ? null : bonusWinner;
    }

    /**
     * Private constructor of Game.
     * Mustn't be called.
     *
     * @throws UnsupportedOperationException if called
     */
    private Game() {
        throw new UnsupportedOperationException();
    }
}