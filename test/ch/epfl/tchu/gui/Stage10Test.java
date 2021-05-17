package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;


import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicCardState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.PublicPlayerState;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.gui.ActionHandlers.ChooseTicketsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicCardState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.PublicPlayerState;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.gui.ActionHandlers.ChooseTicketsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class Stage10Test extends Application {

    private PlayerState p1State;

    public static void main(String[] args) {
        launch(args);
    }

    private void setState(GraphicalPlayer player) {
        // … construit exactement les mêmes états que la méthode setState
        // du test de l'étape 9
        p1State =
                new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
                      //  SortedBag.of(3, Card.WHITE, 3, Card.RED),
                        SortedBag.of(Card.ALL),
                        ChMap.routes().subList(0, 3));

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
        PublicCardState cardState =
                new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
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
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(ChMap.tickets().get(0)).add(ChMap.tickets().get(1)).add(ChMap.tickets().get(2)).add(ChMap.tickets().get(3)).add(ChMap.tickets().get(4));

//        SortedBag.Builder<Card> claimCards =  new SortedBag.Builder<>();
//        claimCards.add(new )

        ChooseTicketsHandler chooseTicketsH = (e) -> {
            System.out.println("J'ai pris des billets");
        };

        ActionHandlers.ChooseCardsHandler chooseCardsH = o -> {
            System.out.println("J'ai choisi des cartes");
        };

        DrawTicketsHandler drawTicketsH =
                () -> {
                    p.receiveInfo("Je tire des billets !");
                    p.chooseTickets(builder.build(), chooseTicketsH);
                };
        DrawCardHandler drawCardH =
                s -> p.receiveInfo(String.format("Je tire une carte de %s !", s));
        ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s", rn, cs));
                    p.chooseClaimCards(p1State.possibleClaimCards(r),chooseCardsH);
                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);

        for (int i = 0; i <= 5 ; i++) {
            p.receiveInfo(i + "\n");
        }
    }

}

