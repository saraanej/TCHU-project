package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private int nbInfosReceived = 0;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;
        private PlayerId ownId;
        private Map<PlayerId, String> playerNames;

        //For the initial ticket's choice
        private SortedBag<Ticket> initialTicketChoice;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.ownId = ownId;
            this.playerNames = playerNames;
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println(info);
            ++ nbInfosReceived;
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            initialTicketChoice = tickets;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            int numberOfTicketsToChoose = rng.nextInt(3) + 3;
            SortedBag.Builder<Ticket> chosenTickets = new SortedBag.Builder<>();
            for (int i = 0; i < numberOfTicketsToChoose; i++) {
                Ticket chosen = initialTicketChoice.get(rng.nextInt(initialTicketChoice.size()));
                chosenTickets.add(chosen);
                initialTicketChoice.difference(SortedBag.of(chosen));
            }
            return chosenTickets.build();
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = new ArrayList<>();
            for (Route r: allRoutes) {
                if (ownState.canClaimRoute(r)) claimableRoutes.add(r);
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
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            int numberOfTicketsToChoose = rng.nextInt(3) + 1;
            SortedBag.Builder<Ticket> chosenTickets = new SortedBag.Builder<>();
            for (int i = 0; i < numberOfTicketsToChoose; i++) {
                Ticket chosen = options.get(rng.nextInt(options.size()));
                chosenTickets.add(chosen);
                options.difference(SortedBag.of(chosen));
            }
            return chosenTickets.build();
        }

        @Override
        public int drawSlot() {
            return rng.nextInt(6);
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            int chosen = rng.nextInt(options.size());
            return options.get(chosen);
        }
    }
    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };

    public static Random RANDOM = new Random(6);


    @Test
    public void testPlay(){
        TestPlayer Yasmin = new TestPlayer(1, ChMap.routes());
        TestPlayer Sara = new TestPlayer(1, ChMap.routes());

        Map<PlayerId, Player> players = new EnumMap<PlayerId, Player>(PlayerId.class);
        players.put(PlayerId.PLAYER_1, Yasmin);
        players.put(PlayerId.PLAYER_2, Sara);

        Map<PlayerId, String> playerNames = new EnumMap<PlayerId, String>(PlayerId.class);
        playerNames.put(PlayerId.PLAYER_1, "Yasmin");
        playerNames.put(PlayerId.PLAYER_2, "Sara");

        Game.play(players,playerNames,SortedBag.of(ChMap.tickets()),NON_RANDOM);
        assertTrue(Yasmin.nbInfosReceived > 5 + 2* Yasmin.turnCounter);
        assertTrue(Sara.nbInfosReceived > 5 + 2* Sara.turnCounter);



    }

    @Test
    public void testPlayThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            //Game.play();
        });
    }
}
