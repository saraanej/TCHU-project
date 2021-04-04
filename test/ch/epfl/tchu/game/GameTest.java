package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameTest {
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
            List<Route> claimableRoutes = /* ... */;
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
}
