package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicPlayerStateTest5 {

    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station WAS = new Station(29, "Wassen");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station INT = new Station(11, "Interlaken");

    Route route1 = new Route("BEL_WAS_1", BEL, WAS, 4, Route.Level.UNDERGROUND, null);
    Route route2 = new Route("BEL_WAS_2", BEL, WAS, 4, Route.Level.UNDERGROUND, null);
    Route route3 = new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE);
    Route route4 = new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW);
    Route route5 = new Route("BER_INT_1", BER, INT, 3, Route.Level.OVERGROUND, Color.BLUE);
    Route route6 = new Route("BER_LUC_1", BER, LUC, 4, Route.Level.OVERGROUND, null);
    Route route7 = new Route("BER_LUC_2", BER, LUC, 4, Route.Level.OVERGROUND, null);
    @Test
    void routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        PublicPlayerState player1 = new PublicPlayerState(5,8, routes);
        boolean mustBeTrue = routes.equals(player1.routes());
        assertTrue(mustBeTrue);
    }

    @Test
    void cardCount() {
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        PublicPlayerState player1 = new PublicPlayerState(5,8, routes);
        assertEquals(8, player1.cardCount());
    }

    @Test
    void ticketCount() {
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        PublicPlayerState player1 = new PublicPlayerState(5,8, routes);
        assertEquals(5, player1.ticketCount());
    }

    @Test
    void carCount() {
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        PublicPlayerState player1 = new PublicPlayerState(5,8, routes);
        assertEquals(19, player1.carCount());
    }

    @Test
    void claimPoints() {
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        PublicPlayerState player1 = new PublicPlayerState(5,8, routes);
        assertEquals(34, player1.claimPoints());
    }

    @Test
    void failWithLengthProblems(){
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        assertThrows(IllegalArgumentException .class, ()->{
            PublicPlayerState player1 = new PublicPlayerState(-1,8, routes);
        });
        assertThrows(IllegalArgumentException .class, ()->{
            PublicPlayerState player1 = new PublicPlayerState(4,-8, routes);
        });
    }

    @Test
    void noProblemWithNoRoute(){
        PublicPlayerState player1 = new PublicPlayerState(4,8, null);
        assertEquals(null, player1.routes() );
    }
}