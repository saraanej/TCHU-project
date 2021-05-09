package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class Stage10Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void setState(GraphicalPlayer player) {
        PlayerState p1State =
                new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 7)),
                        SortedBag.of(3, Card.RED, 1, Card.WHITE),
                        ChMap.routes().subList(0, 3));
//        System.out.println(SortedBag.of(4, Card.RED,3,Card.BLUE));
//        System.out.println(SortedBag.of(2, Card.RED, 3, Card.RED));
        //TODO : Demander aux assistants prq SortedBag.of(2, red, 2, red) != SortedBag.of(4,Red)

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
        PublicCardState cardState =
                new PublicCardState(SortedBag.of(Card.ALL.subList(0, 4)).union(SortedBag.of(Card.LOCOMOTIVE)).toList(), 110 - 2 * 4 - 5, 0);
        PublicGameState publicGameState =
                new PublicGameState(36, cardState, PLAYER_1, pubPlayerStates, null);
        player.setState(publicGameState, p1State);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        ActionHandlers.DrawTicketsHandler drawTicketsH =
                () -> p.receiveInfo("Je tire des billets !");
        ActionHandlers.DrawCardHandler drawCardH =
                s -> p.receiveInfo(String.format("Je tire une carte de %s !", s));
        ActionHandlers.ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s", rn, cs));
                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);
    }


}

