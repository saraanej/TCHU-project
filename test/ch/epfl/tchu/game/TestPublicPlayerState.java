package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPublicPlayerState {

    private List<Route> routes = List.of(ChMap.routes().get(1),
            ChMap.routes().get(2),
            ChMap.routes().get(3),
            ChMap.routes().get(14));

    @Test
    public void ConstructorTrivialWorks(){
        PublicPlayerState player = new PublicPlayerState(2,5,routes);

        assertEquals(2, player.ticketCount());
        assertEquals(5, player.cardCount());
        assertEquals(routes, player.routes());
    }

    @Test
    public void ConstructorThrowsTicket(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-8,0,routes);
        });
    }

    @Test
    public void ConstructorThrowsCar(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(6,-7,routes);
        });
    }

    @Test
    public void CarCount(){
        PublicPlayerState player = new PublicPlayerState(2,5,List.of());
        PublicPlayerState player1 = new PublicPlayerState(2,5,List.of(routes.get(0), routes.get(1)));
        assertEquals(40, player.carCount());
        assertEquals(40-routes.get(0).length() - routes.get(1).length(), player1.carCount() );
    }

    @Test
    public void claimPoints(){
        PublicPlayerState player = new PublicPlayerState(2,5,List.of());
        PublicPlayerState player1 = new PublicPlayerState(2,5,List.of(routes.get(0), routes.get(1)));
        assertEquals(0, player.claimPoints());
        int points = routes.get(0).claimPoints() + routes.get(1).claimPoints();
        assertEquals(points,player1.claimPoints());
    }
}
