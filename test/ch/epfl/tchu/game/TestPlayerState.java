package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class TestPlayerState {
    private List<Route> routes = List.of(ChMap.routes().get(1),
            ChMap.routes().get(2),
            ChMap.routes().get(3),
            ChMap.routes().get(14));

    private List<Ticket> tickets = ChMap.tickets();
    private List<Card> cards = Card.ALL;
    private List<Ticket> tickets1 = List.of(tickets.get(0),
            tickets.get(1),
            tickets.get(2),
            tickets.get(3),
            tickets.get(4),
            tickets.get(5));


    private  PlayerState player = new PlayerState(SortedBag.of(tickets1),SortedBag.of(cards), routes);


    @Test
    public void ConstructorWorks(){

        PlayerState player = new PlayerState(SortedBag.of(tickets1),SortedBag.of(cards), routes);
        assertEquals(routes,player.routes());
        assertEquals(SortedBag.of(tickets1), player.tickets());
        assertEquals(SortedBag.of(cards), player.cards());
    }

    @Test
    public void initialThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
           PlayerState.initial(SortedBag.of());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState.initial(SortedBag.of(Card.BLUE));
        });
    }

    @Test
    public void initialWorks(){
        List<Card> cards = List.of(Card.YELLOW,Card.BLACK,Card.LOCOMOTIVE,Card.RED);
        PlayerState player = PlayerState.initial(SortedBag.of(cards));
        assertEquals(SortedBag.of(cards), player.cards());
        assertEquals(0, player.routes().size());
        assertEquals(0, player.tickets().size());
    }

    @Test
    public void withAddedTickets(){
        List<Ticket> t = List.of(tickets.get(0),
                tickets.get(1),
                tickets.get(2),
                tickets.get(3),
                tickets.get(4),
                tickets.get(5),
                tickets.get(6));
        PlayerState player1 = player.withAddedTickets(SortedBag.of(tickets.get(6)));
        assertEquals(SortedBag.of(t), player1.tickets());
        assertEquals(player.cards(), player1.cards());
        assertEquals(player.routes(), player1.routes());
    }

    @Test
    public void withAddedCard(){
        PlayerState player1 = player.withAddedCard(Card.BLUE);
        List<Card> c = new ArrayList<>(cards);
        c.add(Card.BLUE);

        assertEquals(SortedBag.of(c), player1.cards());
        assertEquals(player.tickets(), player1.tickets());
        assertEquals(player.routes(), player1.routes());

    }

    @Test
    public void withAddedCards(){
        PlayerState player1 = player.withAddedCards(SortedBag.of(2, Card.BLUE));
        List<Card> c = new ArrayList<>(cards);
        c.add(Card.BLUE);
        c.add(Card.BLUE);

        assertEquals(SortedBag.of(c), player1.cards());
        assertEquals(player.tickets(), player1.tickets());
        assertEquals(player.routes(), player1.routes());

    }

    @Test
    public void possibleClaimCardThrows(){

    }
}
