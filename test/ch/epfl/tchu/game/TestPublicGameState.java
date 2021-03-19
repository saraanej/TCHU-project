package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestPublicGameState {

    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station BER = new Station(3, "Berne");
    private static final Station LUC = new Station(16, "Lucerne");


    private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Route.Level.OVERGROUND, Color.BLACK);
    private static final Route E = new Route("BER_LUC_1", BER, LUC, 6, Route.Level.UNDERGROUND, null);

    List<Route> routes1 = List.of(A);
    List<Route> routes2 = List.of(E);
    List<Route> routes3 = List.of(E, A);


    List<Card> faceCards = List.of(Card.BLUE, Card.VIOLET,Card.BLUE,Card.RED,Card.WHITE);

    PublicCardState cardState = new PublicCardState(faceCards, 3, 6);

    PlayerId currentPlayerId = PlayerId.PLAYER_1;

    PlayerId lastPlayer = PlayerId.PLAYER_2;

    PublicPlayerState FirstPlayerState = new PublicPlayerState(4,6,routes1);

    PublicPlayerState lastPlayerState = new PublicPlayerState(5,2,routes2);

    PublicPlayerState anotherPlayerState = new PublicPlayerState(3,1, routes3);


    Map<PlayerId, PublicPlayerState> playerState = Map.of(currentPlayerId, FirstPlayerState, lastPlayer, lastPlayerState);

    PublicGameState tester = new PublicGameState(3,cardState,currentPlayerId, playerState, lastPlayer);

    @Test
    void ConstrcutorWithRightArguments(){
        //Nothing was thrown : Constructeur marche
        //assertThrows(NullPointerException .class, () -> {new PublicGameState(3,cardState,currentPlayerId, playerState, null);});
    }

    @Test
    void ConstructorWithNegativeDeckSize(){
        assertThrows(IllegalArgumentException .class, () -> {new PublicGameState(-2,cardState,currentPlayerId, playerState, null);});
    }


    @Test
    void ConstrcutorWithOnePairs(){
        Map<PlayerId, PublicPlayerState> playerStateOne = Map.of(currentPlayerId, FirstPlayerState);
        assertThrows(IllegalArgumentException .class, () -> {new PublicGameState(3,cardState,currentPlayerId, playerStateOne, null);});
    }

    @Test
    void ConstructorWithNullFirstPlayer(){
        assertThrows(NullPointerException .class, () -> {new PublicGameState(2,cardState,null, playerState, null);});
    }

    @Test
    void ConstructorWithNullcardState(){
        assertThrows(NullPointerException .class, () -> {new PublicGameState(2,null,currentPlayerId, playerState, null);});
    }

    @Test
    void ConstructorWithNullPlayerState(){
        assertThrows(NullPointerException .class, () -> {new PublicGameState(2,cardState,currentPlayerId, null, null);});
    }

    @Test
    void GetterTicketsCount(){
       assertEquals(3, tester.ticketsCount());
    }

    @Test
    void BooleanCanDrawTicketsTrue(){
        assertTrue(tester.canDrawTickets());
    }

    @Test
    void BooleanCanDrawTicketsFalse(){
        PublicGameState tester = new PublicGameState(0,cardState,currentPlayerId, playerState, null);
        assertFalse(tester.canDrawTickets());
    }

    @Test
    void CardStateWorks(){
        assertEquals(cardState,tester.cardState());
    }

    @Test
    void BooleanCanDrawCardsTrue(){
        PublicCardState cardState = new PublicCardState(faceCards, 3, 6);
        assertTrue(tester.canDrawCards());
    }

    @Test
    void BooleanCanDrawCardsFalse(){
        PublicCardState cardState = new PublicCardState(faceCards, 1, 3);
        PublicGameState tester = new PublicGameState(3,cardState,currentPlayerId, playerState, null);
        assertFalse(tester.canDrawCards());
    }

    @Test
    void TestCurrentPlayerId(){
        assertEquals(PlayerId.PLAYER_1, tester.currentPlayerId());
    }

    @Test
    void TestPlayerState(){
        assertEquals(FirstPlayerState, tester.playerState(currentPlayerId));
        assertEquals(lastPlayerState, tester.playerState(lastPlayer));
    }

    @Test
    void TestCurrentPlayerState(){
        assertEquals(FirstPlayerState, tester.currentPlayerState());
    }

    @Test
    void TestListofClaimedRoutes(){
        List<Route> rout = new ArrayList<>();
        rout.add(A);
        rout.add(E);
        assertEquals(rout, tester.claimedRoutes());
    }

    @Test
    void TestLastPlayerIdentityknown(){
        assertEquals(lastPlayer, tester.lastPlayer());
    }

    @Test
    void TestLastPlayerIdentityUnknown(){
        PublicGameState tester = new PublicGameState(3,cardState,currentPlayerId, playerState, null);
        assertEquals(null, tester.lastPlayer());
    }


}
