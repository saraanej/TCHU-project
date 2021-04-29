package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class TestServer1 {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);
            var playerNames = Map.of(PLAYER_1, "Hana",
                    PLAYER_2, "Zaineb");
            playerProxy.initPlayers(PLAYER_1, playerNames);
            playerProxy.receiveInfo("yey l'info a été reçue!");

            // create a new PublicGameState
            var faceUpCards = SortedBag.of(5, Card.LOCOMOTIVE).toList();
            var cardState = new PublicCardState(faceUpCards, 0, 0);
            var initialPlayerState = (PublicPlayerState) PlayerState.initial(SortedBag.of(4, Card.RED));
            var playerState = Map.of(
                    PLAYER_1, initialPlayerState,
                    PLAYER_2, initialPlayerState);

            var p1 = PLAYER_1;
            var p2 = PLAYER_2;

            PublicGameState newState= new PublicGameState(2, cardState, p1, playerState, p2);

            // Create a new playerState
            List<Card> CAR_CARDS=
                    Card.CARS.subList(0,Card.WHITE.ordinal() + 1);
            List<Route> Route = List.of(ChMap.routes().get(0),ChMap.routes().get(1), ChMap.routes().get(2));
            var s1 = new Station(0, "From");
            var s2 = new Station(1, "To");
            var t = new Ticket(List.of(new Trip(s1, s2, 15)));
            var S1 = new Station(2, "From");
            var S2 = new Station(4, "To");
            var T = new Ticket(List.of(new Trip(S1, S2, 5)));
            List<Ticket> lt = ChMap.tickets().subList(0,2);

            PlayerState ownState= new PlayerState(SortedBag.of(lt), SortedBag.of(CAR_CARDS), Route);

            playerProxy.updateState(newState, ownState);

            playerProxy.setInitialTicketChoice(SortedBag.of(lt));

            System.out.println(playerProxy.nextTurn());

            System.out.println(playerProxy.chooseTickets(SortedBag.of(lt)));

            System.out.println(playerProxy.drawSlot());

            System.out.println(playerProxy.claimedRoute());

            System.out.println(playerProxy.initialClaimCards());

            System.out.println(playerProxy.chooseInitialTickets());

            // create new List(SortedBag<Card>)
            List<SortedBag<Card>> listSortedBagCard= List.of(SortedBag.of(List.of(Card.BLUE, Card.ORANGE, Card.RED, Card.GREEN, Card.YELLOW)));

            System.out.println(playerProxy.chooseAdditionalCards(listSortedBagCard));


        }
        System.out.println("Server done!");
    }
}