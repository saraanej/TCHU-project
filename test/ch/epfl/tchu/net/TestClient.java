package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.*;
import java.util.stream.Collectors;

public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(),
                        "localhost",
                        5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {

        private Random random = new Random(10);
        private SortedBag<Ticket> initTickets;
        private PlayerState ownState;
        private PublicGameState gameState;

        private List<Route> allRoutes = ChMap.routes();
        private Route routeToClaim = allRoutes.get(0);
        private SortedBag<Card> initialClaimCards = SortedBag.of(Card.BLUE);

        private PlayerId ownId;
        private Map<PlayerId, String> playerNames;


        @Override
        public void initPlayers(PlayerId ownId,
                                Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.printf("info: %s\n", info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.printf("ownState: %s\n", ownState);
            System.out.printf("newState: %s\n", newState);
            this.ownState = ownState;
            gameState = newState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.printf("tickets: %s\n", tickets);
            initTickets = tickets;
        }

        @Override
        public int drawSlot() {
            return random.nextInt(6)-1;
        }

        @Override
        public TurnKind nextTurn() {
            var turn = doNextTurn();
            return turn;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.printf("options chooseTickets: %s\n", options);
            var shuffledOptions = new ArrayList<>(options.toList());
            Collections.shuffle(shuffledOptions, random);
            var ticketsToKeep = 1 + random.nextInt(options.size());
            SortedBag<Ticket> t = SortedBag.of(shuffledOptions.subList(0, ticketsToKeep));
            System.out.println("tickets to keep"+t);
            return t ;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            SortedBag<Ticket> t = SortedBag.of(1,initTickets.get(1),1,initTickets.get(2));
            System.out.println(t);
            return t;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            System.out.printf("Cards options: %s\n", options);
            return random.nextInt(10) == 0
                    ? SortedBag.of()
                    : options.get(random.nextInt(options.size()));
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim == null ? ChMap.routes().get(0) : routeToClaim ;
        }

        private TurnKind doNextTurn() {
            if (gameState.canDrawTickets())
                return TurnKind.DRAW_TICKETS;

            var claimedRoutes = new HashSet<>(gameState.claimedRoutes());
            var claimableRoutes = allRoutes.stream()
                    .filter(r -> !claimedRoutes.contains(r))
                    .filter(ownState::canClaimRoute)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (claimableRoutes.isEmpty() || ownState.cardCount() < 16) {
                return TurnKind.DRAW_CARDS;
            } else {
                var route = claimableRoutes.get(random.nextInt(claimableRoutes.size()));
                for (int i = 0; i < 3 && route.level() == Route.Level.OVERGROUND; i++) {
                    // slightly favor tunnels
                    route = claimableRoutes.get(random.nextInt(claimableRoutes.size()));
                }

                var cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.isEmpty() ? null : cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
        }
    }
}