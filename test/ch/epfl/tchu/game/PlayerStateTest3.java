/*
 *	Author:      Malak Lahlou Nabil
 *	Date:        17 mars 2021
 */

package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStateTest3 {

    @Test
    void constructorWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1,
                ChMap.tickets().get(1));
        SortedBag<Card> cards = SortedBag.of(3, Card.RED, 5, Card.LOCOMOTIVE);
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets, cards, routes);

        assertEquals(tickets, p.tickets());
        assertEquals(cards, p.cards());
        assertEquals(routes, p.routes());

    }

    @Test
    void methodInitialthrows() {

        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState.initial(SortedBag.of(1, Card.BLACK, 2, Card.WHITE));
        });

    }

    @Test
    void methodInitialWorks() {
        SortedBag<Card> cards = SortedBag.of(2, Card.RED, 2, Card.LOCOMOTIVE);
        assertEquals(cards, PlayerState.initial(cards).cards());
        assertEquals(SortedBag.of().size(),
                PlayerState.initial(cards).ticketCount());
        assertEquals(SortedBag.of().size(),
                PlayerState.initial(cards).routes().size());
    }

    @Test
    void withAddedTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(14), 1,
                ChMap.tickets().get(15));
        SortedBag<Card> cards = SortedBag.of(3, Card.RED, 5, Card.LOCOMOTIVE);
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets, cards, routes);
        Ticket addedTicket = ChMap.tickets().get(3);
        assertEquals(tickets.union(SortedBag.of(addedTicket)),
                p.withAddedTickets(SortedBag.of(addedTicket)).tickets());
        SortedBag<Ticket> s = SortedBag.of(1, ChMap.tickets().get(3), 1,
                ChMap.tickets().get(5));
        assertEquals(tickets.union(s),
                p.withAddedTickets(SortedBag.of(s)).tickets());

    }

    @Test
    void WithAddedCardWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(14), 1,
                ChMap.tickets().get(15));
        SortedBag<Card> cards = SortedBag.of(3, Card.RED, 5, Card.LOCOMOTIVE);
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets, cards, routes);
        Card c = Card.BLACK;
        assertEquals(cards.union(SortedBag.of(c)), p.withAddedCard(c).cards());

    }

    @Test
    void WithAddedCardsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(14), 1,
                ChMap.tickets().get(15));
        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 5,
                Card.LOCOMOTIVE);
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets, cards, routes);
        SortedBag<Card> cards2 = SortedBag.of(2, Card.BLUE, 1, Card.ORANGE);
        assertEquals(cards.union(cards2), p.withAddedCards(cards2).cards());

    }

    @Test
    void canClaimRouteFalseIfNotEnoughCar() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1,
                ChMap.tickets().get(1));
        PlayerState p = new PlayerState(tickets,
                SortedBag.of(3, Card.BLACK, 2, Card.BLUE), ChMap.routes());

        assertEquals(false, p.canClaimRoute(ChMap.routes().get(3)));

    }

    @Test
    void canClaimRouteWorks() {
        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        PlayerState player1 = new PlayerState(tickets,
                SortedBag.of(List.of(Card.VIOLET, Card.YELLOW, Card.RED,
                        Card.RED, Card.LOCOMOTIVE, Card.LOCOMOTIVE)),
                List.of(ChMap.routes().get(0), ChMap.routes().get(1)));

        Route route1 = ChMap.routes().get(3);
        assertEquals(false, player1.canClaimRoute(route1));

        Route route2 = ChMap.routes().get(18);
        assertEquals(true, player1.canClaimRoute(route2));

        Route route3 = ChMap.routes().get(2);
        assertEquals(true, player1.canClaimRoute(route3));

        Route route4 = ChMap.routes().get(13);
        assertEquals(false, player1.canClaimRoute(route4));
    }

    @Test
    void possibleClaimCardsThrows() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1,
                ChMap.tickets().get(1));
        PlayerState p = new PlayerState(tickets,
                SortedBag.of(3, Card.BLACK, 2, Card.BLUE), ChMap.routes());

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleClaimCards(ChMap.routes().get(0));
        });

    }

    @Test
    void possibleClaimCardsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1,
                ChMap.tickets().get(1));
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets,
                SortedBag.of(3, Card.BLACK, 2, Card.BLUE), routes);
        Route r = ChMap.routes().get(8);
        assertEquals(List.of(SortedBag.of(Card.BLACK)),
                p.possibleClaimCards(r));

    }

    @Test
    void possibleClaimCardWorks2() {
        Station s = ChMap.stations().get(0);
        Station s1 = ChMap.stations().get(1);
        Route route = new Route("route", s, s1, 1, Route.Level.OVERGROUND,
                Color.YELLOW);

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW);
        List<Route> liste = List.of(ChMap.routes().get(0),
                ChMap.routes().get(2), ChMap.routes().get(4),
                ChMap.routes().get(6), ChMap.routes().get(8));
        PlayerState player = new PlayerState(tickets, cards, liste);

        List<SortedBag<Card>> play = List.of(SortedBag.of(1, Card.YELLOW));
        assertEquals(play, player.possibleClaimCards(route));

    }

    @Test
    void possibleClaimCardsWorks3() {

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        PlayerState player1 = new PlayerState(tickets,
                SortedBag.of(List.of(Card.VIOLET, Card.YELLOW, Card.RED,
                        Card.RED, Card.LOCOMOTIVE, Card.LOCOMOTIVE)),
                List.of(ChMap.routes().get(0), ChMap.routes().get(1)));

        Route route1 = ChMap.routes().get(3);
        assertEquals(List.of(), player1.possibleClaimCards(route1));

        Route route2 = ChMap.routes().get(18);
        SortedBag<Card> claimCards = SortedBag.of(2, Card.RED);
        assertEquals(List.of(claimCards), player1.possibleClaimCards(route2));

        Route route3 = ChMap.routes().get(2);
        List<SortedBag<Card>> liste = new ArrayList<SortedBag<Card>>();
        
        liste.add(SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE));
        liste.add(SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE));


        assertEquals(liste, player1.possibleClaimCards(route3));
    }

    @Test
    void possibleAdditionalCardsThrows() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1,
                ChMap.tickets().get(1));
        List<Route> routes = List.of();
        PlayerState p = new PlayerState(tickets,
                SortedBag.of(3, Card.BLACK, 2, Card.BLUE), routes);

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleAdditionalCards(0, SortedBag.of(Card.ORANGE),
                    SortedBag.of(List.of(Card.BLACK, Card.ORANGE, Card.BLUE)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleAdditionalCards(4, SortedBag.of(Card.ORANGE),
                    SortedBag.of(List.of(Card.BLACK, Card.ORANGE, Card.BLUE)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleAdditionalCards(2, SortedBag.of(),
                    SortedBag.of(List.of(Card.BLACK, Card.ORANGE, Card.BLUE)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleAdditionalCards(2,
                    SortedBag.of(List.of(Card.ORANGE, Card.BLACK, Card.BLUE)),
                    SortedBag.of(List.of(Card.BLACK, Card.ORANGE, Card.BLUE)));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            p.possibleAdditionalCards(2,
                    SortedBag.of(List.of(Card.ORANGE, Card.BLACK)),
                    SortedBag.of(List.of(Card.BLACK, Card.ORANGE)));
        });
    }

    @Test
    void possibleAdditionalCardsWorks1() {
        Station s = ChMap.stations().get(0);
        Station s1 = ChMap.stations().get(1);
        Route route = new Route("route", s, s1, 1, Route.Level.UNDERGROUND,
                Color.GREEN);

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        SortedBag<Card> cards = SortedBag
                .of(List.of(Card.GREEN, Card.GREEN, Card.GREEN, Card.BLUE,
                        Card.BLUE, Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        List<Route> liste = List.of();
        PlayerState player = new PlayerState(tickets, cards, liste);

        List<SortedBag<Card>> l = new ArrayList<>();
        l.add(SortedBag.of(2, Card.GREEN));
        l.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        l.add(SortedBag.of(2, Card.LOCOMOTIVE));
        assertEquals(l, player.possibleAdditionalCards(2,
                SortedBag.of(Card.GREEN),
                SortedBag.of(List.of(Card.GREEN, Card.ORANGE, Card.BLACK))));

    }

    @Test
    void possibleAdditionalCardsWorks2() {
        Station s = ChMap.stations().get(0);
        Station s1 = ChMap.stations().get(1);
        Route route = new Route("route", s, s1, 3, Route.Level.UNDERGROUND,
                Color.GREEN);

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        SortedBag<Card> cards = SortedBag.of(List.of(Card.GREEN, Card.GREEN,
                Card.GREEN, Card.GREEN, Card.GREEN, Card.BLUE, Card.BLUE));
        List<Route> liste = List.of();
        PlayerState player = new PlayerState(tickets, cards, liste);

        assertEquals(List.of(), player.possibleAdditionalCards(3,
                SortedBag.of(List.of(Card.GREEN, Card.GREEN, Card.GREEN)),
                SortedBag.of(List.of(Card.GREEN, Card.GREEN, Card.GREEN))));

    }

    @Test
    void possibleAdditionalCardsWorks3() {
        Station s = ChMap.stations().get(0);
        Station s1 = ChMap.stations().get(1);
        Route route = new Route("route", s, s1, 3, Route.Level.UNDERGROUND,
                Color.BLUE);

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        SortedBag<Card> cards = SortedBag.of(List.of(Card.BLUE, Card.BLUE,
                Card.LOCOMOTIVE, Card.GREEN, Card.GREEN, Card.BLUE, Card.BLUE));
        List<Route> liste = List.of();
        PlayerState player = new PlayerState(tickets, cards, liste);

        assertEquals(
                List.of(SortedBag.of(2, Card.BLUE),
                        SortedBag.of(1, Card.BLUE, 1, Card.LOCOMOTIVE)),
                player.possibleAdditionalCards(2,
                        SortedBag.of(List.of(Card.BLUE)), SortedBag.of(
                                List.of(Card.BLUE, Card.YELLOW, Card.GREEN))));

    }

    @Test
    void withClaimedRouteWorks() {
        Station s = ChMap.stations().get(0);
        Station s1 = ChMap.stations().get(1);
        Route route = new Route("route", s, s1, 1, Route.Level.UNDERGROUND,
                Color.GREEN);

        SortedBag.Builder<Ticket> build = new SortedBag.Builder<>();
        for (Ticket ticket : ChMap.tickets()) {
            build.add(ticket);
        }
        SortedBag<Ticket> tickets = build.build();
        SortedBag<Card> cards = SortedBag
                .of(List.of(Card.GREEN, Card.GREEN, Card.GREEN, Card.BLUE,
                        Card.BLUE, Card.LOCOMOTIVE, Card.LOCOMOTIVE));
        List<Route> liste = List.of();
        PlayerState player = new PlayerState(tickets, cards, liste);
        assertEquals(
                SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.GREEN,
                        Card.LOCOMOTIVE, Card.LOCOMOTIVE)),
                player.withClaimedRoute(route, SortedBag.of(2, Card.GREEN))
                        .cards());
        List<Route> route2 = new ArrayList<Route>();
        route2.addAll(player.routes());
        route2.add(route);
        assertEquals(route2, player
                .withClaimedRoute(route, SortedBag.of(2, Card.GREEN)).routes());
    }


}
