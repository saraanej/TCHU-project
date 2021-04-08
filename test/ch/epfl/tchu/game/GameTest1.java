/*
 *	Author:      Malak Lahlou Nabil
 *	Date:        29 mars 2021
 */

package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import ch.epfl.tchu.SortedBag;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest1 {

    public static final Random NOM_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i - 1;
        }
    };

    @Test
    public void playGameThrows() {

        Map<PlayerId, Player> players = new HashMap<>();
        players.put(PlayerId.PLAYER_1, new TestPlayer(1, ChMap.routes()));
        Map<PlayerId, String> playerNames = new HashMap<>();
        playerNames.put(PlayerId.PLAYER_1, "Player_1");
        playerNames.put(PlayerId.PLAYER_2, "Player_2");
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());

        assertThrows(IllegalArgumentException.class, () -> {
            Game.play(players, playerNames, tickets, NOM_RANDOM);
        });

        players.put(PlayerId.PLAYER_2, new TestPlayer(1, ChMap.routes()));
        playerNames.remove(PlayerId.PLAYER_2);
        assertThrows(IllegalArgumentException.class, () -> {
            Game.play(players, playerNames, tickets, NOM_RANDOM);
        });

    }


    @Test 
    public void playWorks() {
        
        TestPlayer player1 = new TestPlayer(20,ChMap.routes());
        TestPlayer player2 = new TestPlayer(20,ChMap.routes());
        Map<PlayerId, Player> players = new HashMap<>();
        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);
        
        Map<PlayerId, String> playerNames = new HashMap<>();
        playerNames.put(PlayerId.PLAYER_1, "Player1");
        playerNames.put(PlayerId.PLAYER_2, "Player2");
        
        Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random());
        
    }

    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;
        
        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;
        private PlayerId ownId;
        private Map<PlayerId, String> playerNames;
        private StringBuilder receivedInfo;
        private SortedBag<Ticket> initialTickets;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;
        private int infoCounter;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
            this.infoCounter = 0;
            this.receivedInfo = new StringBuilder();
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public TurnKind nextTurn() {
            
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");
            
            List<Route> claimableRoutes = new ArrayList<>();
            for (Route route : allRoutes) {
               if (ownState.canClaimRoute(route)){

                    claimableRoutes.add(route);
                }
            }
            
            
            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
            

            /*
            
            if (claimableRoutes.isEmpty() && gameState.canDrawCards()) {
                return TurnKind.DRAW_CARDS;
            } else if(!claimableRoutes.isEmpty()){
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
            else if(gameState.ticketsCount() > 0){
                return TurnKind.DRAW_TICKETS;
            }
            return null;
            */
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.ownId=ownId;
            this.playerNames=playerNames;           
            
        }

        @Override
        public void receiveInfo(String info) {
            receivedInfo.append(info); 
            System.out.println(info);
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            this.initialTickets=tickets;            
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return initialTickets;
        }
        
        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {           
            return options;
        }

        @Override
        public int drawSlot() {
            return rng.nextInt(6)-1;
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            // TODO Auto-generated method stub
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(
                List<SortedBag<Card>> options) {
            return options.get(0);
        }
        
    }
    

}
