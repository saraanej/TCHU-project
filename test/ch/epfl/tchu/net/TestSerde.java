package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSerde {

    List<Route> routes1 = List.of(ChMap.routes().get(4));
    List<Route> routes2 = List.of(ChMap.routes().get(2));
    List<Route> routes3 = List.of(ChMap.routes().get(8), ChMap.routes().get(17));

    List<Card> faceCards = List.of(Card.BLUE, Card.VIOLET,Card.BLUE,Card.RED,Card.WHITE);

    PublicCardState cardState = new PublicCardState(faceCards, 3, 6);

    PlayerId currentPlayerId = PlayerId.PLAYER_1;

    PlayerId lastPlayer = PlayerId.PLAYER_2;

    PublicPlayerState FirstPlayerState = new PublicPlayerState(4,6,routes1);

    PublicPlayerState lastPlayerState = new PublicPlayerState(5,2,routes2);
    PublicPlayerState nullSerde = new PublicPlayerState(5,2,null);


    SortedBag<Ticket> tickets = SortedBag.of(4, ChMap.tickets().get(5), 1, ChMap.tickets().get(6));

    SortedBag<Card> cards = SortedBag.of(4, Card.VIOLET, 2, Card.BLUE);

    PlayerState playerStateSerde = new PlayerState(tickets, cards, routes3);

    Map<PlayerId, PublicPlayerState> playerState = Map.of(currentPlayerId, FirstPlayerState, lastPlayer, lastPlayerState);

    PublicGameState tester = new PublicGameState(3,cardState,currentPlayerId, playerState, lastPlayer);

    @Test
    public void profTest(){
        Serde<Integer> intSerde = Serde.of(
                i -> Integer.toString(i),
                Integer::parseInt);
        assertEquals("2021", intSerde.serialize(2021));
        assertEquals(2021, intSerde.deserialize("2021"));
    }

    @Test
    public void INTEGER(){
        Serde<Integer> serde = Serdes.INTEGER_SERDE;
        int x = -12;
        assertEquals("-12", serde.serialize(-12));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }
    
    @Test
    public void STRING(){
        Serde<String> serde = Serdes.STRING_SERDE;
        String x = "Yasmine est moche!";
        String ser = Base64.getEncoder().encodeToString(x.getBytes(StandardCharsets.UTF_8));
        assertEquals(ser, serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void PLAYERID(){
        Serde<PlayerId> serde = Serdes.PLAYER_ID_SERDE;
        PlayerId x = PlayerId.PLAYER_1;
        assertEquals("0", serde.serialize(x));
        PlayerId y = PlayerId.PLAYER_2;
        assertEquals("1", serde.serialize(y));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
        assertEquals(y, serde.deserialize(serde.serialize(y)));
    }

    @Test
    public void TURNKIND(){
        Serde<Player.TurnKind> serde = Serdes.TURN_KIND_SERDE;
        Player.TurnKind y = Player.TurnKind.CLAIM_ROUTE;
        assertEquals("2", serde.serialize(y));
        assertEquals(y, serde.deserialize(serde.serialize(y)));
        Player.TurnKind x = Player.TurnKind.DRAW_CARDS;
        assertEquals("1", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
        Player.TurnKind w = Player.TurnKind.DRAW_TICKETS;
        assertEquals("0", serde.serialize(w));
        assertEquals(w, serde.deserialize(serde.serialize(w)));
    }

    @Test
    public void CARD(){
        Serde<Card> serde = Serdes.CARD_SERDE;
        Card x = Card.BLUE;
        assertEquals("2", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
        Card black = Card.BLACK;
        assertEquals("0", serde.serialize(black));
        assertEquals(black, serde.deserialize(serde.serialize(black)));
        Card loco = Card.LOCOMOTIVE;
        assertEquals("8", serde.serialize(loco));
        assertEquals(loco, serde.deserialize(serde.serialize(loco)));
    }

    @Test
    public void ROUTE(){
        Serde<Route> serde = Serdes.ROUTE_SERDE;
        Route x = ChMap.routes().get(5);
        assertEquals("5", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void TICKET(){
        Serde<Ticket> serde = Serdes.TICKET_SERDE;
         Ticket x = ChMap.tickets().get(4);
        assertEquals("4", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void STRINGLIST(){
        Serde<List<String>> serde = Serdes.LIST_STRING_SERDE;
        List<String> x = List.of("Bonsoir", "a toi", "qui lit", "cette classe", "de test.");
        List<String> aff = new ArrayList<>();
        for(String string : x){
            aff.add(Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8)));
        }
        String text = String.join(",", aff);
        assertEquals(text, serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void CARDLIST(){
        Serde<List<Card>> serde = Serdes.LIST_CARD_SERDE;
        List<Card> x = List.of(Card.BLUE,Card.BLACK,Card.GREEN,Card.BLUE);
        assertEquals("2,0,3,2", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void ROUTELIST(){
        Serde<List<Route>> serde = Serdes.LIST_ROUTE_SERDE;
        List<Route> x = List.of(ChMap.routes().get(3), ChMap.routes().get(7), ChMap.routes().get(12)) ;
        assertEquals("3,7,12", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void SORTEDCARD(){
        Serde<SortedBag<Card>> serde = Serdes.SORTED_CARD_SERDE;
        SortedBag<Card> x = SortedBag.of(4, Card.BLUE, 2, Card.GREEN) ;
        assertEquals("2,2,2,2,3,3", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void SORTEDTICKET(){
        Serde<SortedBag<Ticket>> serde = Serdes.SORTED_TICKET_SERDE;
        SortedBag<Ticket> x = SortedBag.of(1,ChMap.tickets().get(19), 1,ChMap.tickets().get(4));
        assertEquals("4,19", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void LISTSORTEDCARD(){
        Serde<List<SortedBag<Card>>> serde = Serdes.LIST_SORTED_CARD_SERDE;
        List<SortedBag<Card>> x = List.of(SortedBag.of(2, Card.BLUE, 3, Card.VIOLET), SortedBag.of(4, Card.RED, 1, Card.BLUE));
        assertEquals("1,1,1,2,2;2,6,6,6,6", serde.serialize(x));
        assertEquals(x, serde.deserialize(serde.serialize(x)));
    }

    @Test
    public void PUBLICCARDSTATE(){
        Serde<PublicCardState> serde = Serdes.PUBLIC_CARDSTATE_SERDE;
        PublicCardState x = new PublicCardState(faceCards, 4,2);
        PublicCardState des = serde.deserialize(serde.serialize(x));
        assertEquals("2,1,2,6,7;4;2", serde.serialize(x));
        assertEquals(x.deckSize(), des.deckSize());
        assertEquals(x.discardsSize(), des.discardsSize());
        assertEquals(x.faceUpCards(),des.faceUpCards());
    }

    @Test
    public void PUBLICPLAYERSTATE(){
        Serde<PublicPlayerState> serde = Serdes.PUBLIC_PLAYERSTATE_SERDE;
        PublicPlayerState x = FirstPlayerState;
        PublicPlayerState des = serde.deserialize(serde.serialize(x));
        assertEquals("4;6;4", serde.serialize(x));
        assertEquals(x.routes(), des.routes());
        assertEquals(x.cardCount(), des.cardCount());
        assertEquals(x.ticketCount(),des.ticketCount());
        assertEquals(x.carCount(),des.carCount());
        assertEquals(x.claimPoints(),des.claimPoints());

        PublicPlayerState nullSerde = new PublicPlayerState(5,2,null);
        des = serde.deserialize(serde.serialize(nullSerde));
        assertEquals("5;2;", serde.serialize(nullSerde));
    }

    @Test
    public void PLAYERSTATE(){
        Serde<PlayerState> serde = Serdes.PLAYERSTATE_SERDE;
        PlayerState x = playerStateSerde;
        PlayerState des = serde.deserialize(serde.serialize(x));
        assertEquals("5,5,5,5,6;1,1,1,1,2,2;8,17", serde.serialize(x));
        assertEquals(x.cards(), des.cards());
        assertEquals(x.tickets(), des.tickets());
    }

    @Test
    public void PUBLICGAMESTATE(){
        Serde<PublicGameState> serde = Serdes.PUBLIC_GAMESTATE_SERDE;
        PublicGameState x = tester;
        assertEquals("3:2,1,2,6,7;3;6:0:4;6;4:5;2;2:1", serde.serialize(x));
        PublicGameState des = serde.deserialize(serde.serialize(x));
        assertEquals(x.ticketsCount(), des.ticketsCount());
        assertEquals(x.cardState(),des.cardState());
        assertEquals(x.currentPlayerId(),des.currentPlayerId());
        assertEquals(x.lastPlayer(),des.lastPlayer());
    }
}
