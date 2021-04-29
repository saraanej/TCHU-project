package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class TestClient1 {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(),
                        "localhost",
                        5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {

        SortedBag<Ticket> initTickets;
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
            System.out.printf("newState: %s\n", newState);
            System.out.printf("ownState: %s\n", ownState);
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            this.initTickets = tickets;
            System.out.printf("tickets: %s\n", tickets);
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            var s1 = new Station(0, "From");
            var s2 = new Station(1, "To");
            var t = new Ticket(List.of(new Trip(s1, s2, 15)));
            var S1 = new Station(2, "From");
            var S2 = new Station(4, "To");
            var T = new Ticket(List.of(new Trip(S1, S2, 5)));
            List<Ticket> lt = List.of(t, T);
            return SortedBag.of(initTickets.get(0));
        }

        @Override
        public TurnKind nextTurn() {
            return TurnKind.CLAIM_ROUTE ;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            var s1 = new Station(0, "From");
            var s2 = new Station(1, "To");
            var t = new Ticket(List.of(new Trip(s1, s2, 15)));
            var S1 = new Station(2, "From");
            var S2 = new Station(4, "To");
            var T = new Ticket(List.of(new Trip(S1, S2, 5)));
            List<Ticket> lt = List.of(t, T);
            return SortedBag.of(options.get(0));
        }

        @Override
        public int drawSlot() {
            return 3;
        }

        @Override
        public Route claimedRoute() {
            return ChMap.routes().get(0);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return SortedBag.of(Card.BLUE);
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            List<Card> listCard= List.of(Card.BLUE, Card.ORANGE, Card.RED, Card.GREEN, Card.YELLOW);
            return SortedBag.of(listCard);

        }
    }
}
