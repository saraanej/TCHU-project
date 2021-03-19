package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPublicGameState {

    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station BER = new Station(3, "Berne");
    private static final Station LUC = new Station(16, "Lucerne");


    private static final Route A = new Route("NEU_YVE_1", NEU, YVE, 2, Route.Level.OVERGROUND, Color.BLACK);
    private static final Route E = new Route("BER_LUC_1", BER, LUC, 6, Route.Level.UNDERGROUND, null);

    List<Route> routes = List.of(A);

    List<Card> faceCards = List.of(Card.BLUE, Card.VIOLET,Card.BLUE,Card.RED,Card.WHITE);

    PublicCardState cardState = new PublicCardState(faceCards, 3, 6);

    PlayerId currentPlayerId = PlayerId.PLAYER_1;

    PlayerId lastPlayer = PlayerId.PLAYER_2;

    PublicPlayerState FirstPlayerState = new PublicPlayerState(4,6,routes);

    Map<PlayerId, PublicPlayerState> playerState = Map.of(currentPlayerId, FirstPlayerState);

    @Test
    void ConstrcutorWithRightArguments(){

        //assertThrows(NullPointerException .class, () -> {new PublicGameState(3,cardState,currentPlayerId, playerState, null);});

    }

    @Test
    void ConstructorWithNegativeDeckSize(){
        int tickets = 0;


    }

    @Test
    void ConstrcutorWithMoreThanTwoPairs(){

    }

    @Test
    void ConstrcutorWithOnePairs(){
        Map<PlayerId, PublicPlayerState> playerStateOne = Map.of(currentPlayerId, FirstPlayerState);

        assertThrows(IllegalArgumentException .class, () -> {new PublicGameState(3,cardState,currentPlayerId, playerStateOne, null);});
    }



}
